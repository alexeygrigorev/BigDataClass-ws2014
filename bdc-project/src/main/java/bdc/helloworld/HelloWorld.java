package bdc.helloworld;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * <one line description>
 * <p/>
 * <longer description>
 * <p/>
 * User: mikio
 * Date: 2/13/15
 * Time: 2:45 PM
 */
public class HelloWorld {
    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration(true);

        FileSystem fs = FileSystem.get(conf);

        FSDataOutputStream os = fs.create(new Path("hellworld.txt"), true);
        os.write("Hello, world!\n".getBytes("UTF-8"));

        os.close();
    }
}
