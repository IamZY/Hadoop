package cn.itcast.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * ��֯������������γ�һ�������Ĵ������� ������ν��topology ����mp�е�job ���ҽ���topology�ύ��storm��Ⱥȥ����
 * �ύ��Ⱥ��ͽ�����ֹ���������� �����쳣�˳�
 * 
 * @author IamZY
 *
 */
public class TopoMain {

	public static void main(String[] args) throws Exception {

		TopologyBuilder builder = new TopologyBuilder();

		// �����ǵ�spout������õ�topology�� 4�ǲ�����
		// parallelism_hint:4  ��ʾ��4��exector��ִ��������
		// setNumTask(8) ���õ��Ǹ����ִ��ʱ�Ĳ���task���� Ҳ������ζ��1��exector����2��task
		builder.setSpout("randomspout", new RandomWordSpout(), 4);
		// ����дת��bolt������õ�topology ����ָ��������randomspout�������Ϣ �����������
		// .shuffleGrouping("randomspout")������˼
		// 		���������ܵ�tuple��Ϣһ��������randomspout
		// 		randomspout�����upperbolt�����������taskʵ��֮���շ���Ϣʱ���õķ������������������shuffleGrouping
		builder.setBolt("upperbolt", new UpperBolt(), 4).shuffleGrouping("randomspout");

		builder.setBolt("suffixbolt", new SuffixBolt(), 4).shuffleGrouping("upperbolt");

		// ����topology
		StormTopology topology = builder.createTopology();

		// ����һЩtopology�ڼ�Ⱥ������ʱ�Ĳ���
		Config conf = new Config();
		conf.setNumWorkers(4);
		conf.setDebug(true);
		conf.setNumAckers(0);

		// �ύStorm��Ⱥ
		StormSubmitter.submitTopology("demotopo", conf, topology);

	}

}
