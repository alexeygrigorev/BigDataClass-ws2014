package bdc.hw1;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
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

import bdc.ex1.Tweet;

public class FilterTweets extends Configured implements Tool {

    public static class TextToDoubleMapper extends Mapper<Object, Text, LongWritable, Text> {
        private final Collection<String> users = new HashSet<>();

        @Override
        protected void setup(Context ctx) throws IOException, InterruptedException {
            super.setup(ctx);
            Configuration config = ctx.getConfiguration();
            String usersParam = config.get("users");
            users.addAll(Arrays.asList(usersParam.split(",")));
        }

        @Override
        public void map(Object key, Text jsonText, Context context) throws IOException, InterruptedException {
            Tweet tweet = Tweet.fromJson(jsonText.toString());

            if (users.contains(tweet.getUserName()) || tweet.containsMentionOf(users)) {
                Text value = new Text(tweet.getUserName() + "\t" + tweet.getText());
                context.write(new LongWritable(tweet.getId()), value);
            }
        }
    }

    public static class MinMaxMeanReducer extends
            Reducer<DoubleWritable, DoubleWritable, DoubleWritable, Text> {
        @Override
        public void reduce(DoubleWritable key, Iterable<DoubleWritable> values, Context context)
                throws IOException, InterruptedException {
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: <user1,user2,user3> <file1> <file2> <file3> ... <output>");
            System.exit(0);
        }

        Configuration conf = new Configuration(true);
        conf.set("users", args[0]);

        Job job = Job.getInstance(conf, "FilterTweets");
        job.setJarByClass(FilterTweets.class);

        job.setMapperClass(TextToDoubleMapper.class);

        // Specify key / value
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);

        // Input
        job.setInputFormatClass(TextInputFormat.class);
        String[] inputPaths = Arrays.copyOfRange(args, 1, args.length - 1);
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
        int exitCode = ToolRunner.run(new FilterTweets(), args);
        System.exit(exitCode);
    }

}
