package cn.itcast.hadoop.mr.areaoartition;

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

/**
 * 对流量原始日志进行流量统计 将不同省份的用户统计结果输出道不同文件 需要自定义改造两个机制 -- 改造分区的逻辑 自定义partitioner --
 * 自定义reducer task的并发任务
 * 
 * @author IamZY
 *
 */
public class FlowSumArea {

	public static class FlowSumAreaMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, FlowBean>.Context context)
				throws IOException, InterruptedException {

			// 一行数据
			String line = value.toString();
			// 切分数据
			String[] fileds = StringUtils.split(line,'\t');
			
			String phoneNB = fileds[1];
			Long u_flow = Long.valueOf(fileds[7]);
			Long d_flow = Long.valueOf(fileds[8]);

			
			context.write(new Text(phoneNB), new FlowBean(phoneNB,u_flow,d_flow));

		}

	}
	
	
	public static class FlowSumAreaReducer extends Reducer<Text, FlowBean, Text, FlowBean>{
		
		@Override
		protected void reduce(Text key, Iterable<FlowBean> values, Reducer<Text, FlowBean, Text, FlowBean>.Context content)
				throws IOException, InterruptedException {
			
			long up_flow_counter = 0;
			long d_flow_counter = 0;
			
			for(FlowBean bean : values) {
				up_flow_counter += bean.getUp_flow();
				d_flow_counter += bean.getD_flow();
			}
			

			content.write(new Text(key), new FlowBean(key.toString(),up_flow_counter,d_flow_counter));
		}
		
		
	}
	
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf);
		
		job.setJarByClass(FlowSumArea.class);
		
		job.setMapperClass(FlowSumAreaMapper.class);
		job.setReducerClass(FlowSumAreaReducer.class);
		
		// 设置自定义分组
		job.setPartitionerClass(AreaPartitioner.class);
		
		// 设置reduce的task并发数 应该和分组的数量保持一致
		job.setNumReduceTasks(6);
		
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);
		
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);
		
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		
		System.exit(job.waitForCompletion(true) ? 0:1);
	}
	
	

}
