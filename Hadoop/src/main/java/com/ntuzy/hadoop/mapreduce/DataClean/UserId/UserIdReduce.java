package com.ntuzy.hadoop.mapreduce.DataClean.UserId;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Author IamZY
 * @create 2020/1/11 11:34
 */
public class UserIdReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        int sum = 0;

        for (IntWritable i : values) {
            sum += i.get();
        }

        context.write(key, new IntWritable(sum));

    }
}
