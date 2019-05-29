package cn.itcast.bigdata;

import java.util.HashMap;

import org.apache.hadoop.hive.ql.exec.UDF;

public class PhoneNBToArea extends UDF {

	
	private static HashMap<String,String> areaMap = new HashMap<>();
	
	static {
		areaMap.put("138", "beijing");
		areaMap.put("139", "tianjing");
		areaMap.put("136", "nanjing");		
	}
	
	
	public String evaluate(String pnb) {
		String ext = pnb.toString().substring(0, 3);
		String result = areaMap.get(ext) == null ? (pnb + " huoxing") : (pnb + " "+areaMap.get(ext));
		return result;
	}
	
}
