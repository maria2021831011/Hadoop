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

public class MinValue {

    // Mapper
    public static class MinMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static Text keyOut = new Text("Min");

        public void map(Object key,
                        Text value,
                        Context context)
                throws IOException, InterruptedException {

            int number = Integer.parseInt(value.toString());

            context.write(keyOut, new IntWritable(number));
        }
    }

    // Reducer
    public static class MinReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text key,
                           Iterable<IntWritable> values,
                           Context context)
                throws IOException, InterruptedException {

            int min = Integer.MAX_VALUE;

            for (IntWritable val : values) {
                min = Math.min(min, val.get());
            }

            context.write(key, new IntWritable(min));
        }
    }

    // Main
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "min value");

        job.setJarByClass(MinValue.class);

        job.setMapperClass(MinMapper.class);
        job.setReducerClass(MinReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}