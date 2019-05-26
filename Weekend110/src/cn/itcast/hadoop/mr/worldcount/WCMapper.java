package cn.itcast.hadoop.mr.worldcount;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

/**
 * 四个泛型中前两个是mapper输入数据的类型 后面两个是mapper输出的数据的类型
 * maphereduce的数据输入输出都是key-value对的形式封装的
 * 
 * 默认情况下 框架传递给我们mapper的输入数据 key是要处理的文本的一行起始偏移量 这一行的内容作为value
 * 
 * @author IamZY
 *
 */
public class WCMapper extends Mapper<LongWritable, Text, Text, LongWritable> {


	/**
	 * mapreduce  每读一行数据就会调用一次map方法
	 */
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context)
			throws IOException, InterruptedException {
		
		// 具体业务逻辑
		// key是一行数据的起始偏移量 value这一行的文本内容
		String line = value.toString();
		
		String[] words = StringUtils.split(line, ' ');
		
		for(String word: words) {
			context.write(new Text(word), new LongWritable(1));
		}
		
	}

	
}
