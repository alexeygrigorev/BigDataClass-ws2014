package bdc.ex1;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
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

public class MinMaxMeanValue extends Configured implements Tool {

    public static class TextToDoubleMapper extends Mapper<Object, Text, DoubleWritable, DoubleWritable> {
        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] csv = value.toString().split("\\s+");
            DoubleWritable keyOut = new DoubleWritable(Double.parseDouble(csv[0]));
            DoubleWritable valueOut = new DoubleWritable(Double.parseDouble(csv[1]));
            context.write(keyOut, valueOut);
        }
    }

    public static class MinMaxMeanReducer extends
            Reducer<DoubleWritable, DoubleWritable, DoubleWritable, Text> {
        @Override
        public void reduce(DoubleWritable key, Iterable<DoubleWritable> values, Context context)
                throws IOException, InterruptedException {
            double sum = 0;
            int cnt = 0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            for (DoubleWritable writable : values) {
                double value = writable.get();
                sum = sum + value;
                cnt++;

                if (value > max) {
                    max = value;
                }

                if (value < min) {
                    min = value;
                }
            }

            double mean = sum / cnt;
            String output = MessageFormat.format("min: {0}, max: {1}, mean: {2}", min, max, mean);
            context.write(key, new Text(output));
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: <in> <out>");
            System.exit(0);
        }

        Path inputPath = new Path(args[0]);
        Path outputDir = new Path(args[1]);

        // Create configuration
        Configuration conf = new Configuration(true);

        // Create job
        Job job = Job.getInstance(conf, "MinMaxMeanValue");
        job.setJarByClass(MinMaxMeanValue.class);

        // Setup MapReduce
        job.setMapperClass(TextToDoubleMapper.class);
        job.setReducerClass(MinMaxMeanReducer.class);
        job.setNumReduceTasks(1);

        // Specify key / value
        job.setMapOutputKeyClass(DoubleWritable.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        job.setOutputKeyClass(DoubleWritable.class);
        job.setOutputValueClass(Text.class);

        // Input
        FileInputFormat.addInputPath(job, inputPath);
        job.setInputFormatClass(TextInputFormat.class);

        // Output
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
        int exitCode = ToolRunner.run(new MinMaxMeanValue(), args);
        System.exit(exitCode);
    }

}
