package cn.itcast.haddp.mr.flowsum;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

public class FlowSumMapper extends Mapper<LongWritable, Text, Text, FlowBean>{

	/**
	 * 读取拿到数据中的一行数据 切分各个字段 抽取出我们需要的数据 封装成需要发送的数据
	 */
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
