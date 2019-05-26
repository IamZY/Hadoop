package cn.itcast.hadoop.mr.worldcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 用来描述一个特定的job
 * 指定逻辑处理的map 和 reduce
 * @author IamZY
 *
 */
public class WCRunner {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
//		conf.set("", "");
		Job job = Job.getInstance(conf);
		
		// 设置整个job所用的类在哪个jar包
		job.setJarByClass(WCRunner.class);
		
		// 本jiob使用的mapper和reducer类
		job.setMapperClass(WCMapper.class);
		job.setReducerClass(WCReducer.class);
		
		//指定recude的输出类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		//指定mapper的输出类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		
		// 指定原始数据的存放路径
		FileInputFormat.setInputPaths(job, new Path("e:/wc/srcdata/"));
		
		
		// 处理结果的输出数据存放路径
		FileOutputFormat.setOutputPath(job, new Path("e:/wc/output"));
		
		// 将job提交给集群运行
		job.waitForCompletion(true);
		
	}
	
	
}
