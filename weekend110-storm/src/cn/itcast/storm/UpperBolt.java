package cn.itcast.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class UpperBolt extends BaseBasicBolt {

	/**
	 * ҵ���߼�
	 */
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		// �Ȼ�ȡ��һ���齨���������� ������tuple���� ���Ǳ�ȡ
		String godName = tuple.getString(0);
		// ����Ʒ��ת���ɴ�д
		String godName_Upper = godName.toUpperCase();
		// ��ת����ɵ���Ʒ�����ͳ�ȥ
		collector.emit(new Values(godName_Upper));
	}

	/**
	 * ����bolt���ͳ�ȥ����Ϣ����
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("upperName"));
	}

}
