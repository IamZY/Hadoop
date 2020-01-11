package com.ntuzy.hadoop.mapreduce.DataClean.UserId;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


/**
 * @Author IamZY
 * @create 2020/1/11 11:34
 */
public class UserIdMap extends Mapper<LongWritable, Text, Text, IntWritable> {


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] fields = line.split("\t");
        context.write(new Text(fields[1]),new IntWritable(1));
    }


}
