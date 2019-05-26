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
 * ��������һ���ض���job
 * ָ���߼������map �� reduce
 * @author IamZY
 *
 */
public class WCRunner {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Configuration conf = new Configuration();
//		conf.set("", "");
		Job job = Job.getInstance(conf);
		
		// ��������job���õ������ĸ�jar��
		job.setJarByClass(WCRunner.class);
		
		// ��jiobʹ�õ�mapper��reducer��
		job.setMapperClass(WCMapper.class);
		job.setReducerClass(WCReducer.class);
		
		//ָ��recude���������
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		//ָ��mapper���������
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		
		// ָ��ԭʼ���ݵĴ��·��
		FileInputFormat.setInputPaths(job, new Path("e:/wc/srcdata/"));
		
		
		// ��������������ݴ��·��
		FileOutputFormat.setOutputPath(job, new Path("e:/wc/output"));
		
		// ��job�ύ����Ⱥ����
		job.waitForCompletion(true);
		
	}
	
	
}
