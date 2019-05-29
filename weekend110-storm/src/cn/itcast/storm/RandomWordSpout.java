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
	 * ��Ϣ��װ��tuple
	 */
	@Override
	public void nextTuple() {
		Random random = new Random();
		
		int index = random.nextInt(words.length);
		
		// ͨ��������õ�һ������
		String goodName = words[index];
		
		// ����Ʒ����װ��tuple ������Ϣ����һ�����
		collector.emit(new Values(goodName));
//		collector.emit(new Values(goodName,"10","1000"));
		
		// û����һ����Ϣ ����0.5�� ��Ȼ��һ�·�����Ϣ
		Utils.sleep(500);
	}

	/**
	 * ��ʼ��
	 */
	@Override
	public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {

		this.collector = collector;
		
		
	}

	/**
	 * �����ֶεĶ��� ����tuple
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

		declarer.declare(new Fields("orignName"));
//		declarer.declare(new Fields("orignName","id","price"));
		
	}

}
