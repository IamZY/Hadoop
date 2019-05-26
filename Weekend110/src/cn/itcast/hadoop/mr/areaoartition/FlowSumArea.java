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
 * ������ԭʼ��־��������ͳ�� ����ͬʡ�ݵ��û�ͳ�ƽ���������ͬ�ļ� ��Ҫ�Զ�������������� -- ����������߼� �Զ���partitioner --
 * �Զ���reducer task�Ĳ�������
 * 
 * @author IamZY
 *
 */
public class FlowSumArea {

	public static class FlowSumAreaMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, FlowBean>.Context context)
				throws IOException, InterruptedException {

			// һ������
			String line = value.toString();
			// �з�����
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
		
		// �����Զ������
		job.setPartitionerClass(AreaPartitioner.class);
		
		// ����reduce��task������ Ӧ�úͷ������������һ��
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
