package bdc.minimal;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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

import java.io.IOException;

/**
 * Minimal example of submitting a map-reduce job
 *
 * Uses Tool interface and ToolRunner so you can us standard
 * command line arguments
 */
public class MinimalMRJob extends Configured implements Tool {

  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new MinimalMRJob(), args);
    System.exit(exitCode);
  }

  static class MyMapper
      extends Mapper<Object, Text, LongWritable, Text> {
    @Override
    public void map(Object key, Text value, Context context) {

    }
  }

  static class MyReducer
    extends Reducer<LongWritable, Text, LongWritable, Text> {
    @Override
    public void reduce(LongWritable timestamp, Iterable<Text> tweets, Context context) {
    }
  }

  @Override
  public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
    Configuration conf = getConf();

    Path input = new Path(args[0]);
    Path outputDir = new Path("output");

    Job job = Job.getInstance(conf, "MinimalMRJob");
    job.setJarByClass(MinimalMRJob.class);
    job.setMapperClass(MyMapper.class);
    job.setReducerClass(MyReducer.class);

    job.setOutputKeyClass(LongWritable.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.addInputPath(job, input);
    job.setInputFormatClass(TextInputFormat.class);

    FileOutputFormat.setOutputPath(job, outputDir);
    job.setOutputFormatClass(TextOutputFormat.class);

    // Delete output if exists
    FileSystem hdfs = FileSystem.get(conf);
    if (hdfs.exists(outputDir) && hdfs.isDirectory(outputDir))
        hdfs.delete(outputDir, true);

    // Execute job
    return job.waitForCompletion(true) ? 0 : 1;
  }
}
