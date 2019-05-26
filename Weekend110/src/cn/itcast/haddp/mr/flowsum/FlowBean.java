package cn.itcast.haddp.mr.flowsum;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class FlowBean implements WritableComparable<FlowBean> {

	private String phoneNB;
	private Long up_flow;
	private Long d_flow;
	private Long s_flow;
	
	/**
	 * 为了对象数据初始化方便
	 * @param phoneNB
	 * @param up_flow
	 * @param d_flow
	 * @param s_flow
	 */
	public FlowBean(String phoneNB, Long up_flow, Long d_flow) {
		super();
		this.phoneNB = phoneNB;
		this.up_flow = up_flow;
		this.d_flow = d_flow;
		this.s_flow = up_flow + d_flow;
	}
	
	

	/**
	 * 在反序列化时 反射机制需要调用空参 构造函数
	 */
	public FlowBean() {
	}



	public String getPhoneNB() {
		return phoneNB;
	}

	public void setPhoneNB(String phoneNB) {
		this.phoneNB = phoneNB;
	}

	public Long getUp_flow() {
		return up_flow;
	}

	public void setUp_flow(Long up_flow) {
		this.up_flow = up_flow;
	}

	public Long getD_flow() {
		return d_flow;
	}

	public void setD_flow(Long d_flow) {
		this.d_flow = d_flow;
	}

	public Long getS_flow() {
		return s_flow;
	}

	public void setS_flow(Long s_flow) {
		this.s_flow = s_flow;
	}

	/**
	 * 从数据中反序列化读出数据
	 * 必须和序列化的顺序保持一致
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		
		phoneNB = in.readUTF();
		up_flow = in.readLong();
		d_flow = in.readLong();
		s_flow = in.readLong();
		
	}

	/**
	 * 将对象数据序列化到流中
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(phoneNB);
		out.writeLong(up_flow);
		out.writeLong(d_flow);
		out.writeLong(s_flow);		
	}



	@Override
	public String toString() {
		return "" + up_flow + "\t" + d_flow + "\t" + s_flow;
	}



	@Override
	public int compareTo(FlowBean o) {
		return s_flow > o.getS_flow() ? -1 : 1;
	}

	
	
	
	
}
