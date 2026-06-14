package org.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

public class StudentAverage {

    // Mapper
    public static class AvgMapper
            extends Mapper<Object, Text, Text, FloatWritable> {

        private Text student = new Text();

        public void map(Object key,
                        Text value,
                        Context context)
                throws IOException, InterruptedException {

            String[] parts = value.toString().split(" ");

            student.set(parts[0]);
            float marks = Float.parseFloat(parts[1]);

            context.write(student, new FloatWritable(marks));
        }
    }

    // Reducer
    public static class AvgReducer
            extends Reducer<Text, FloatWritable, Text, FloatWritable> {

        public void reduce(Text key,
                           Iterable<FloatWritable> values,
                           Context context)
                throws IOException, InterruptedException {

            float sum = 0;
            int count = 0;

            for (FloatWritable v : values) {
                sum += v.get();
                count++;
            }

            float avg = sum / count;

            context.write(key, new FloatWritable(avg));
        }
    }

    // Main
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "student average");

        job.setJarByClass(StudentAverage.class);

        job.setMapperClass(AvgMapper.class);
        job.setReducerClass(AvgReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}