package com.kovi.wordcount;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Emits (word, 1) for every normalized token in the input split.
 * Lowercases and strips non-alphanumeric characters so counts are clean.
 */
public class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

    private static final IntWritable ONE = new IntWritable(1);
    private final Text word = new Text();

    @Override
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
        String line = value.toString().toLowerCase();
        for (String token : line.split("\\s+")) {
            String cleaned = token.replaceAll("[^a-z0-9]", "");
            if (!cleaned.isEmpty()) {
                word.set(cleaned);
                context.write(word, ONE);
            }
        }
    }
}
