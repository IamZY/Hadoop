package cn.itcast.haddp.mr.flowsum;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class FlowSumRunner extends Configured implements Tool{

	@Override
	public int run(String[] arg0) throws Exception {
		
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf);
		
		job.setJarByClass(FlowSumRunner.class);
		
		job.setMapperClass(FlowSumMapper.class);
		job.setReducerClass(FlowSumReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);
		
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);
		
		
		FileInputFormat.setInputPaths(job, new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
		
//		InputFormat fi = null; 
//		OutputFormat of = null;
		
		
		return job.waitForCompletion(true) ? 0:1;
		
	}

	
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new FlowSumRunner(), args);
		System.exit(res);
	}
	
	
}
