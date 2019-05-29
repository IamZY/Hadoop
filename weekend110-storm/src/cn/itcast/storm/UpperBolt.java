package cn.itcast.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class UpperBolt extends BaseBasicBolt {

	/**
	 * 业务逻辑
	 */
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		// 先获取上一个组建传来的数据 数据在tuple上面 根角标取
		String godName = tuple.getString(0);
		// 将商品名转换成大写
		String godName_Upper = godName.toUpperCase();
		// 将转换完成的商品名发送出去
		collector.emit(new Values(godName_Upper));
	}

	/**
	 * 声明bolt发送出去的消息类型
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("upperName"));
	}

}
