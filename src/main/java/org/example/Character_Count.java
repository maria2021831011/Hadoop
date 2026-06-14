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
public class Character_Count {



        // Mapper
        public static class CharacterMapper
                extends Mapper<Object, Text, Text, IntWritable> {

            private final static IntWritable one = new IntWritable(1);
            private Text character = new Text();

            public void map(Object key,
                            Text value,
                            Context context)
                    throws IOException, InterruptedException {

                String line = value.toString();

                for (char c : line.toCharArray()) {

                    if (c != ' ') {

                        character.set(String.valueOf(c));

                        context.write(character, one);
                    }
                }
            }
        }

        // Reducer
        public static class CharacterReducer
                extends Reducer<Text, IntWritable,
                Text, IntWritable> {

            public void reduce(Text key,
                               Iterable<IntWritable> values,
                               Context context)
                    throws IOException, InterruptedException {

                int sum = 0;

                for (IntWritable val : values) {
                    sum += val.get();
                }

                context.write(key, new IntWritable(sum));
            }
        }

        // Main Method
        public static void main(String[] args)
                throws Exception {

            Configuration conf = new Configuration();

            Job job = Job.getInstance(conf, "Character Count");

            job.setJarByClass(Character_Count.class);

            job.setMapperClass(CharacterMapper.class);

            job.setCombinerClass(CharacterReducer.class);

            job.setReducerClass(CharacterReducer.class);

            job.setOutputKeyClass(Text.class);

            job.setOutputValueClass(IntWritable.class);

            FileInputFormat.addInputPath(
                    job,
                    new Path(args[0]));

            FileOutputFormat.setOutputPath(
                    job,
                    new Path(args[1]));

            System.exit(
                    job.waitForCompletion(true)
                            ? 0 : 1);
        }
    }

