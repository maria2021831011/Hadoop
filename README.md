1. Overview

This document contains complete Hadoop setup commands, HDFS operations, MapReduce WordCount program flow, IntelliJ + WSL execution, and important theory notes for lab exams.


⚙️ 2. Hadoop Environment Setup

🔹 Start Hadoop Services

source ~/.bashrc

start-dfs.sh

start-yarn.sh

jps

🔹 Expected Processes

NameNode

DataNode

SecondaryNameNode

ResourceManager

NodeManager

❗ If NameNode Not Working

mkdir -p /tmp/hadoop-asus/dfs/name

mkdir -p /tmp/hadoop-asus/dfs/data

chmod -R 777 /tmp/hadoop-asus


hdfs namenode -format

stop-dfs.sh

start-dfs.sh

jps

🌐 Hadoop Web UI

NameNode: http://localhost:9870

ResourceManager: http://localhost:8088

📁 3. HDFS Basic Commands

📌 Directory

hdfs dfs -mkdir /input

hdfs dfs -mkdir -p /data/test

📌 Upload File

hdfs dfs -put input.txt /input

📌 List Files

hdfs dfs -ls /

hdfs dfs -ls /input

📌 Read File

hdfs dfs -cat /input/input.txt

📌 Download File

hdfs dfs -get /input/input.txt

📌 Delete File/Folder

hdfs dfs -rm /input/input.txt

hdfs dfs -rm -r /input

🧾 4. WordCount Input Example

echo "hello hadoop hello world" > input.txt

🚀 5. WordCount MapReduce Program

📌 Compile

javac -classpath $(hadoop classpath) -d . WordCount.java

📌 Create JAR

jar -cvf wordcount.jar *.class

📌 Run Job

hadoop jar wordcount.jar WordCount /input/input.txt /output

📌 Output

hdfs dfs -cat /output/part-r-00000

💻 6. IntelliJ + WSL Run Example

hadoop jar target/word_count-1.0-SNAPSHOT.jar org.example.WC_Runner /input/input.txt /output

📤 7. HDFS File Example (Custom Folder)

hdfs dfs -mkdir -p /maria

echo "Hello Hadoop Hello Maria" > maria.txt

hdfs dfs -put maria.txt /maria/

hdfs dfs -cat /maria/maria.txt

🧩 8. MapReduce Flow

Input Data

   ↓

Mapper

   ↓

Shuffle & Sort
 
  ↓

Reducer
  
 ↓

Output

🧠 9. Theory Questions

📌 Hadoop


Hadoop is a distributed framework for storing and processing large datasets.

📌 HDFS

Hadoop Distributed File System for storing data in blocks.

📌 NameNode

Manages metadata (file location, structure).

📌 DataNode

Stores actual data blocks.

📌 Secondary NameNode

Creates checkpoints (backup metadata).

📌 YARN

Resource management system in Hadoop.

🔥 10. WordCount Logic

Input:

Hello Hadoop Hello World

Output:

Hadoop 1

Hello 2

World 1

🧠 11. MapReduce Core Concept

Mapper:

Splits words

Emits (word, 1)

Reducer:

Sums values

Produces final count

⚡ 12. Important Notes

✔ Always Same:

Mapper class structure

Reducer class structure

context.write()

❌ Changes:

Key/Value type

Logic (sum, max, avg)

Input parsing

🔧 13. Key Imports (Must Remember)

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
import java.util.StringTokenizer;

🎯 14. Exam Quick Commands

start-dfs.sh

start-yarn.sh

jps

hdfs dfs -ls /

hdfs dfs -put file /input

hadoop jar file.jar Main /input /output

hdfs dfs -cat /output/part-r-00000

💡 15. Output Prediction Practice

Input:

cat dog cat

dog bird

Output:

bird 1

cat 2

dog 2




Hadoop MapReduce Java Quick Reference
1. Basic Java Syntax
   Variable
   int age = 22;
   String name = "Maria";
   Object Creation
   ClassName objectName = new ClassName();

Example:

Text word = new Text();

IntWritable one = new IntWritable(1);

Method
public void hello() {
System.out.println("Hello");
}
If-Else
if (x > 10) {
System.out.println("Big");
} else {
System.out.println("Small");
}
For Loop
for (int i = 0; i < 5; i++) {
System.out.println(i);
}
For-Each Loop
for (IntWritable val : values) {
sum += val.get();
}
While Loop
while (itr.hasMoreTokens()) {
word.set(itr.nextToken());
}
2. Hadoop Data Types
   Java Type	Hadoop Type
   String	Text
   int	IntWritable
   long	LongWritable
   float	FloatWritable
   double	DoubleWritable

Example:

Text word = new Text("Hello");
IntWritable count = new IntWritable(1);
3. Hadoop Mapper Syntax
   Template
   public static class MyMapper extends Mapper<InputKey, InputValue, OutputKey, OutputValue> {
   }
   WordCount Example
   extends Mapper<Object, Text, Text, IntWritable>
   Meaning
   Type	Purpose
   Object	Input Key
   Text	Input Line
   Text	Output Word
   IntWritable	Output Count
4. Mapper Method
   Template
   public void map(Object key, Text value, Context context)
   Parameters
   Parameter	Description
   key	Input line offset
   value	Actual input line
   context	Writes Mapper output
   Example

Input:

Hello Hadoop

Mapper Output:

context.write(new Text("Hello"), new IntWritable(1));
context.write(new Text("Hadoop"), new IntWritable(1));
5. context.write()
   Template
   context.write(key, value);
   Example
   context.write(word, one);

Output:

Hello -> 1
6. Hadoop Reducer Syntax
   Template
   public static class MyReducer extends Reducer<InputKey, InputValue, OutputKey, OutputValue> {
   }
   WordCount Example
   extends Reducer<Text, IntWritable, Text, IntWritable>
7. Reducer Method
   Template
   public void reduce(Text key, Iterable<IntWritable> values, Context context)
   Example

Input:

Hello -> [1,1,1]

Reducer Logic:

int sum = 0;

for (IntWritable val : values) {
sum += val.get();
}

Output:

Hello -> 3
8. StringTokenizer

Used to split a sentence into words.

StringTokenizer itr = new StringTokenizer(value.toString());

Input:

Hello Hadoop World

Output:

Hello
Hadoop
World
9. Hadoop Job Configuration
   Configuration conf = new Configuration();

Job job = Job.getInstance(conf, "word count");

Creates a new MapReduce job.

10. Set Mapper
    job.setMapperClass(TokenizerMapper.class);
11. Set Reducer
    job.setReducerClass(IntSumReducer.class);
12. Combiner
    job.setCombinerClass(IntSumReducer.class);

Benefits:

Optional optimization
Reduces network traffic
Performs local aggregation
13. Output Types
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
14. Input Path
    FileInputFormat.addInputPath(job, new Path(args[0]));

Example:

hadoop jar wc.jar WordCount /input/input.txt /output
args[0] = /input/input.txt
15. Output Path
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    args[1] = /output
16. Run Job
    job.waitForCompletion(true);

Waits until the MapReduce job finishes.

Exam Shortcuts
Sum
int sum = 0;

for (IntWritable v : values) {
sum += v.get();
}
Maximum
int max = Integer.MIN_VALUE;

for (IntWritable v : values) {
max = Math.max(max, v.get());
}
Minimum
int min = Integer.MAX_VALUE;

for (IntWritable v : values) {
min = Math.min(min, v.get());
}
Average
sum += v.get();
count++;

avg = sum / count;
MapReduce Workflow
Input Data
|
v
Mapper
|
v
Shuffle & Sort
|
v
Reducer
|
v
Output
Common Hadoop Exam Topics
Word Count
Character Count
Maximum Value
Minimum Value
Average Calculation
Top N Records
HDFS Commands
NameNode and DataNode
YARN Architecture
MapReduce Workflow