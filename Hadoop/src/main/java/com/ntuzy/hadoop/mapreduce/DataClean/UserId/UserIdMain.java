package com.ntuzy.hadoop.mapreduce.DataClean.UserId;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @Author IamZY
 * @create 2020/1/11 11:33
 */
public class UserIdMain {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //判断一下，输入参数是否是两个，分别表示输入路径、输出路径
        if (args.length != 2 || args == null) {
            System.out.println("please input Path!");
            System.exit(0);
        }

        Configuration configuration = new Configuration();
        configuration.set("mapreduce.framework.name", "local");


        //告诉程序，要运行的jar包在哪
        //configuration.set("mapreduce.job.jar","/home/hadoop/IdeaProjects/Hadoop/target/com.kaikeba.hadoop-1.0-SNAPSHOT.jar");

        //调用getInstance方法，生成job实例
        Job job = Job.getInstance(configuration, UserIdMain.class.getSimpleName());

        //设置job的jar包，如果参数指定的类包含在一个jar包中，则此jar包作为job的jar包； 参数class跟主类在一个工程即可；一般设置成主类
//        job.setJarByClass(WordCountMain.class);
        job.setJarByClass(UserIdMain.class);

        //通过job设置输入/输出格式
        //MR的默认输入格式是TextInputFormat，输出格式是TextOutputFormat；所以下两行可以注释掉
//        job.setInputFormatClass(TextInputFormat.class);
//        job.setOutputFormatClass(TextOutputFormat.class);

        //设置输入/输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

//        FileInputFormat.setInputPaths(job, new Path("E:/test.txt"));
//        FileOutputFormat.setOutputPath(job, new Path("E:/output.txt"));


        //设置处理Map阶段的自定义的类
        job.setMapperClass(UserIdMap.class);
        //设置map combine类，减少网路传出量
        job.setCombinerClass(UserIdReduce.class);
        //设置处理Reduce阶段的自定义的类
        job.setReducerClass(UserIdReduce.class);
        //注意：如果map、reduce的输出的kv对类型一致，直接设置reduce的输出的kv对就行；如果不一样，需要分别设置map, reduce的输出的kv类型
        //注意：此处设置的map输出的key/value类型，一定要与自定义map类输出的kv对类型一致；否则程序运行报错
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //设置reduce task最终输出key/value的类型
        //注意：此处设置的reduce输出的key/value类型，一定要与自定义reduce类输出的kv对类型一致；否则程序运行报错
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 提交作业
        job.waitForCompletion(true);
    }
}
