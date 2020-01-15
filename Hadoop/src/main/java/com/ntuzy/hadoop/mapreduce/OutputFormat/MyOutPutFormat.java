package com.ntuzy.hadoop.mapreduce.OutputFormat;


import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * - 现在有一些订单的评论数据，要将订单的好评与其它级别的评论进行区分开来，将最终的数据分开到不同的文件夹下面去
 * - 数据第九个字段表示评分等级：0 好评，1 中评，2 差评
 */

public class MyOutPutFormat extends FileOutputFormat<Text, NullWritable> {
    /**
     * 两个输出文件
     * 根据实际情况修改path;node01及段口号8020
     */
    String path1 = "hdfs://node01:9000/outputformat/out1/r.txt";
    String path2 = "hdfs://node01:9000/outputformat/out2/r.txt";

    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
        FileSystem.get(context.getConfiguration());
        FileSystem fs = FileSystem.get(context.getConfiguration());
        Path enhancePath = new Path(path1);
        Path toCrawlPath = new Path(path2);
        FSDataOutputStream enhanceOut = fs.create(enhancePath);
        FSDataOutputStream toCrawlOut = fs.create(toCrawlPath);
        return new MyRecordWriter(enhanceOut, toCrawlOut);

    }

    /**
     * 泛型表示reduce输出的键值对类型；要保持一致
     */
    static class MyRecordWriter extends RecordWriter<Text, NullWritable> {

        FSDataOutputStream enhanceOut = null;
        FSDataOutputStream toCrawlOut = null;

        public MyRecordWriter(FSDataOutputStream enhanceOut, FSDataOutputStream toCrawlOut) {
            this.enhanceOut = enhanceOut;
            this.toCrawlOut = toCrawlOut;
        }

        /**
         * 自定义输出kv对逻辑
         *
         * @param key
         * @param value
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void write(Text key, NullWritable value) throws IOException, InterruptedException {
            if (key.toString().split("\t")[9].equals("0")) {
                toCrawlOut.write(key.toString().getBytes());
                //TODO
                toCrawlOut.write("\r\n".getBytes());
            } else {
                enhanceOut.write(key.toString().getBytes());
                enhanceOut.write("\r\n".getBytes());
            }
        }

        /**
         * 关闭流
         *
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            if (toCrawlOut != null) {
                toCrawlOut.close();
            }
            if (enhanceOut != null) {
                enhanceOut.close();
            }
        }
    }
}

