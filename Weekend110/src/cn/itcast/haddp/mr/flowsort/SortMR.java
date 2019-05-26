package cn.itcast.haddp.mr.flowsort;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;

import cn.itcast.haddp.mr.flowsum.FlowBean;
import cn.itcast.haddp.mr.flowsum.FlowSumMapper;
import cn.itcast.haddp.mr.flowsum.FlowSumReducer;
import cn.itcast.haddp.mr.flowsum.FlowSumRunner;

public class SortMR {

	public static class SortMapper extends Mapper<LongWritable, Text, FlowBean, NullWritable>{
		
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, FlowBean, NullWritable>.Context context)
				throws IOException, InterruptedException {
			
			String line = value.toString();
			
			String[] fileds = StringUtils.split(line,'\t');
			
			String phoneNB = fileds[1];
			Long u_flow = Long.parseLong(fileds[1]);
			Long d_flow = Long.parseLong(fileds[2]);
			
			context.write(new FlowBean(phoneNB,u_flow,d_flow),NullWritable.get());
		}
		
	}
	
	
	public static class SortReducer extends Reducer<FlowBean, NullWritable, FlowBean, NullWritable>{
		@Override
		protected void reduce(FlowBean key, Iterable<NullWritable> values,
				Reducer<FlowBean, NullWritable, FlowBean, NullWritable>.Context content)
				throws IOException, InterruptedException {
			
			content.write(key, NullWritable.get());
			
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf);
		
		job.setJarByClass(SortMR.class);
		
		job.setMapperClass(SortMapper.class);
		job.setReducerClass(SortReducer.class);
		
		job.setMapOutputKeyClass(FlowBean.class);
		job.setMapOutputValueClass(NullWritable.class);
		
		
		job.setOutputKeyClass(FlowBean.class);
		job.setOutputValueClass(NullWritable.class);
		
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		
		job.waitForCompletion(true);
		
		
	}
	
	
	
	
}
