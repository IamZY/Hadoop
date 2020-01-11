package com.ntuzy.hadoop.mapreduce.partition;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import javax.swing.*;
import java.util.HashMap;

/**
 * @Author IamZY
 * @create 2020/1/11 17:14
 */
public class CustomPartition extends Partitioner<Text, IntWritable> {

    public static HashMap<String, Integer> dict = new HashMap<>();

    static {
        dict.put("Dear", 0);
        dict.put("Bear", 1);
        dict.put("River", 2);
        dict.put("Car", 3);
    }

    @Override
    public int getPartition(Text key, IntWritable value, int i) {
        // 分别落到 0 1 2 3 对应的分区中
        int partitionIndex = dict.get(key.toString());
        return partitionIndex;
    }
}
