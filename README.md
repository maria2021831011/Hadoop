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

🏁 END

👉 This file contains complete Hadoop lab exam preparation:

Commands

Code

Theory

Execution flow
