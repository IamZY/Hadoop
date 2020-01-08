package com.ntuzy.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * @Author IamZY
 * @create 2020/1/8 19:58
 */
public class CopyFileFromHdfs {
    public static void main(String[] args) {
        try {
            //源文件
            String srcFile = "hdfs://node01:8020/test.txt";

            Configuration conf = new Configuration();

            FileSystem fs = FileSystem.get(URI.create(srcFile), conf);
            FSDataInputStream hdfsInStream = fs.open(new Path(srcFile));

            //本地文件
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("E:/test.txt"));

            IOUtils.copyBytes(hdfsInStream, outputStream, 4096, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
