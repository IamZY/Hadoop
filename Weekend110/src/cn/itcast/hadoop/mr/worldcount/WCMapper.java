package cn.itcast.hadoop.mr.worldcount;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

/**
 * �ĸ�������ǰ������mapper�������ݵ����� ����������mapper��������ݵ�����
 * maphereduce�����������������key-value�Ե���ʽ��װ��
 * 
 * Ĭ������� ��ܴ��ݸ�����mapper���������� key��Ҫ������ı���һ����ʼƫ���� ��һ�е�������Ϊvalue
 * 
 * @author IamZY
 *
 */
public class WCMapper extends Mapper<LongWritable, Text, Text, LongWritable> {


	/**
	 * mapreduce  ÿ��һ�����ݾͻ����һ��map����
	 */
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context)
			throws IOException, InterruptedException {
		
		// ����ҵ���߼�
		// key��һ�����ݵ���ʼƫ���� value��һ�е��ı�����
		String line = value.toString();
		
		String[] words = StringUtils.split(line, ' ');
		
		for(String word: words) {
			context.write(new Text(word), new LongWritable(1));
		}
		
	}

	
}
