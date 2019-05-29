package cn.itcast.storm;

import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.Validate;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class RandomWordSpout extends BaseRichSpout {

	private SpoutOutputCollector collector;

	String[] words = { "iphone", "xiaomi", "mate", "sony", "sumsung", "moto", "meizu" };

	/**
	 * 消息封装成tuple
	 */
	@Override
	public void nextTuple() {
		Random random = new Random();
		
		int index = random.nextInt(words.length);
		
		// 通过随机数拿到一个单词
		String goodName = words[index];
		
		// 将商品名封装成tuple 发送消息给下一个组件
		collector.emit(new Values(goodName));
//		collector.emit(new Values(goodName,"10","1000"));
		
		// 没法送一份消息 休眠0.5秒 不然会一致发送消息
		Utils.sleep(500);
	}

	/**
	 * 初始化
	 */
	@Override
	public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {

		this.collector = collector;
		
		
	}

	/**
	 * 声明字段的定义 定义tuple
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

		declarer.declare(new Fields("orignName"));
//		declarer.declare(new Fields("orignName","id","price"));
		
	}

}
