package org.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class Join {

    public static class JoinMapper
            extends Mapper<Object, Text, Text, Text> {

        public void map(Object key,
                        Text value,
                        Context context)
                throws IOException, InterruptedException {

            String[] parts = value.toString().split(" ");

            context.write(new Text(parts[0]),
                    new Text(parts[1]));
        }
    }

    public static class JoinReducer
            extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key,
                           Iterable<Text> values,
                           Context context)
                throws IOException, InterruptedException {

            StringBuilder sb = new StringBuilder();

            for (Text t : values) {
                sb.append(t.toString()).append(" ");
            }

            context.write(key, new Text(sb.toString()));
        }
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "join");

        job.setJarByClass(Join.class);

        job.setMapperClass(JoinMapper.class);
        job.setReducerClass(JoinReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}