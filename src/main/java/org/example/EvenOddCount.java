package org.example;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class EvenOddCount {

    // Mapper
    public static class EOMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text outKey = new Text();

        public void map(Object key,
                        Text value,
                        Context context)
                throws IOException, InterruptedException {

            int num = Integer.parseInt(value.toString());

            if (num % 2 == 0) {
                outKey.set("Even");
            } else {
                outKey.set("Odd");
            }

            context.write(outKey, one);
        }
    }

    // Reducer
    public static class EOReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text key,
                           Iterable<IntWritable> values,
                           Context context)
                throws IOException, InterruptedException {

            int sum = 0;

            for (IntWritable v : values) {
                sum += v.get();
            }

            context.write(key, new IntWritable(sum));
        }
    }

    // Main
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "even odd count");
        job.setJarByClass(EvenOddCount.class);
        job.setMapperClass(EOMapper.class);
        job.setReducerClass(EOReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}