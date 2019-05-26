package cn.itcast.haddp.mr.flowsum;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

public class FlowSumMapper extends Mapper<LongWritable, Text, Text, FlowBean>{

	/**
	 * ��ȡ�õ������е�һ������ �зָ����ֶ� ��ȡ��������Ҫ������ ��װ����Ҫ���͵�����
	 */
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
