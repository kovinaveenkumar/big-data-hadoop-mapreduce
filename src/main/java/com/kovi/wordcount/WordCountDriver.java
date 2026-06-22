package com.kovi.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * Driver that wires up and submits the MapReduce job.
 * Usage: hadoop jar wordcount.jar com.kovi.wordcount.WordCountDriver <input> <output>
 */
public class WordCountDriver {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] files = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (files.length < 2) {
            System.err.println("Usage: WordCountDriver <input path> <output path>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCountDriver.class);

        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(SumReducer.class);   // local pre-aggregation
        job.setReducerClass(SumReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(files[0]));
        FileOutputFormat.setOutputPath(job, new Path(files[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
