package cn.itcast.hadoop.mr.worldcount;


import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WCReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

	/**
	 * �����map�������֮�� ������kv�Ի������� ���з��� Ȼ�󴫵�һ����<eky,value> ����һ��reduce����
	 * <hello,{1,1,1,1,1,1....}>
	 */
	@Override
	protected void reduce(Text key, Iterable<LongWritable> values,
			Reducer<Text, LongWritable, Text, LongWritable>.Context content) throws IOException, InterruptedException {
		
		long count = 0;
		
		for(LongWritable value : values) {
			count += value.get();
		}
		
		content.write(key, new LongWritable(count));
		
	}

	
	
}
