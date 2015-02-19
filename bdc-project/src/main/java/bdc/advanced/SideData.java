package bdc.advanced;

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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;

/**
 * Example to show the use of side data.
 */
public class SideData extends Configured implements Tool {

  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new SideData(), args);
    System.exit(exitCode);
  }

  static class SideDataMapper extends Mapper<Text, IntWritable, Text, IntWritable> {
    protected String sideData = null;

    @Override
    protected void setup(Context context) {
      try {
        URI[] paths = context.getCacheFiles();

        if (paths != null) {
          for (URI p : paths) {
            System.out.println("Found Path " + p);
          }
        } else {
          System.out.println("paths = null!");
        }

        File f = new File("SIDE-DATA");
        System.out.println("Can I read this? " + f.canRead());
        BufferedReader r = new BufferedReader(new FileReader(f));
        sideData = r.readLine();
        System.out.println("Contents = \"" + sideData + "\"");
        r.close();
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }

    public void map(Text key, IntWritable value, Context context) throws java.io.IOException, InterruptedException {
        context.write(new Text(sideData), value);
    }
  }

  static class SideDataReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
  }

  public int run(String[] strings) throws Exception {
    Configuration conf = getConf();
    Job job = Job.getInstance(conf, "SideDate");

    job.setJarByClass(SideData.class);

    job.setMapperClass(SideDataMapper.class);
    job.setReducerClass(SideDataReducer.class);
    job.setNumReduceTasks(1);

    FileInputFormat.addInputPath(job, new Path("/README"));
    Path outputDir = new Path("out");
    FileOutputFormat.setOutputPath(job, outputDir);

    // Delete output if exists
    FileSystem hdfs = FileSystem.get(conf);
    if (hdfs.exists(outputDir) && hdfs.isDirectory(outputDir))
      hdfs.delete(outputDir, true);

    return job.waitForCompletion(true) ? 0 : 1;
  }
}
