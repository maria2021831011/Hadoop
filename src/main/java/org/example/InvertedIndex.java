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
import java.util.StringTokenizer;

public class InvertedIndex {

    public static class IndexMapper
            extends Mapper<Object, Text, Text, Text> {

        private Text word = new Text();
        private Text file = new Text();

        public void map(Object key,
                        Text value,
                        Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            String[] parts = line.split(":");

            String fileName = parts[0];
            file.set(fileName);

            StringTokenizer itr =
                    new StringTokenizer(parts[1]);

            while (itr.hasMoreTokens()) {

                word.set(itr.nextToken());

                context.write(word, file);
            }
        }
    }

    public static class IndexReducer
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
        Job job = Job.getInstance(conf, "inverted index");

        job.setJarByClass(InvertedIndex.class);

        job.setMapperClass(IndexMapper.class);
        job.setReducerClass(IndexReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}