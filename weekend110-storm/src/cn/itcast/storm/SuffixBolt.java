package cn.itcast.storm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import backtype.storm.tuple__init;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class SuffixBolt extends BaseBasicBolt {

	FileWriter fw = null;

	/**
	 * bolt ֮����һ��
	 */
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		try {
			fw = new FileWriter("/home/hadoop/stormoutput/" + UUID.randomUUID());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		String upper_name = tuple.getString(0);
		String suffix_name = upper_name + "_stock";

		try {
			FileWriter fw = new FileWriter("/home/hadoop/stormdemo");

			fw.write(suffix_name);
			fw.write("\n");
			fw.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ��bolt����Ҫ����tuple��Ϣ����һ����� ���Բ���Ҫ����tuple�ֶ�
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {

	}

}
