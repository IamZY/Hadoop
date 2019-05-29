package cn.itcast.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * 组织各个处理组件形成一个完整的处理流程 就是所谓的topology 类型mp中的job 并且将该topology提交给storm集群去运行
 * 提交后集群后就将永无止境的云运行 除非异常退出
 * 
 * @author IamZY
 *
 */
public class TopoMain {

	public static void main(String[] args) throws Exception {

		TopologyBuilder builder = new TopologyBuilder();

		// 将我们的spout组件设置到topology中 4是并发数
		// parallelism_hint:4  表示用4个exector来执行这个组件
		// setNumTask(8) 设置的是该组件执行时的并发task数量 也就是意味着1个exector运行2个task
		builder.setSpout("randomspout", new RandomWordSpout(), 4);
		// 将大写转换bolt组件设置到topology 并且指定他接受randomspout组件的消息 策略随机分组
		// .shuffleGrouping("randomspout")两层意思
		// 		这个组件接受的tuple消息一定来自于randomspout
		// 		randomspout组件和upperbolt组件大量并发task实例之间收法消息时采用的分组策略是随机分组策略shuffleGrouping
		builder.setBolt("upperbolt", new UpperBolt(), 4).shuffleGrouping("randomspout");

		builder.setBolt("suffixbolt", new SuffixBolt(), 4).shuffleGrouping("upperbolt");

		// 创建topology
		StormTopology topology = builder.createTopology();

		// 配置一些topology在集群中运行时的参数
		Config conf = new Config();
		conf.setNumWorkers(4);
		conf.setDebug(true);
		conf.setNumAckers(0);

		// 提交Storm集群
		StormSubmitter.submitTopology("demotopo", conf, topology);

	}

}
