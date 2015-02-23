package bdc.hw1;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import bdc.ex1.DateHelpers;
import bdc.ex1.Tweet;

public class HashtagTrends extends Configured implements Tool {

    public static class TweetToTimestampedHashtagMapper extends
            Mapper<Object, Text, TimestampedHashtag, IntWritable> {

        private final static IntWritable ONE = new IntWritable(1);

        @Override
        public void map(Object key, Text jsonText, Context context) throws IOException, InterruptedException {
            if ("".equals(jsonText.toString().trim())) {
                return;
            }

            Tweet tweet = Tweet.fromJson(jsonText.toString());
            Date createdAt = DateHelpers.floorToHour(tweet.getCreatedAt());
            Set<String> hashTags = tweet.getHashTags();

            for (String hashTag : hashTags) {
                context.write(new TimestampedHashtag(createdAt, hashTag), ONE);
            }
        }
    }

    public static class TimestampedHashtagCounter extends
            Reducer<TimestampedHashtag, IntWritable, Text, IntWritable> {

        private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss",
                Locale.ENGLISH);

        public void reduce(TimestampedHashtag key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int cnt = 0;

            for (IntWritable value : values) {
                cnt = cnt + value.get();
            }

            String text = DATE_FORMAT.format(key.getTimestamp()) + "\t" + key.getHashtag();
            context.write(new Text(text), new IntWritable(cnt));
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: <file1> <file2> <file3> ... <output>");
            System.exit(0);
        }

        Configuration conf = new Configuration(true);

        Job job = Job.getInstance(conf, "HashtagTrends");
        job.setJarByClass(HashtagTrends.class);

        job.setMapperClass(TweetToTimestampedHashtagMapper.class);

        // Specify key / value
        job.setMapOutputKeyClass(TimestampedHashtag.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // Input
        job.setInputFormatClass(TextInputFormat.class);
        String[] inputPaths = Arrays.copyOfRange(args, 0, args.length - 1);
        for (String inputPath : inputPaths) {
            FileInputFormat.addInputPath(job, new Path(inputPath));
        }

        // Output
        Path outputDir = new Path(args[args.length - 1]);
        FileOutputFormat.setOutputPath(job, outputDir);
        job.setOutputFormatClass(TextOutputFormat.class);

        // Delete output if exists
        FileSystem hdfs = FileSystem.get(conf);
        if (hdfs.exists(outputDir) && hdfs.isDirectory(outputDir)) {
            hdfs.delete(outputDir, true);
        }

        // Execute job
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new HashtagTrends(), args);
        System.exit(exitCode);
    }
}
