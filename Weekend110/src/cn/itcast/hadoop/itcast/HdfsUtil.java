package cn.itcast.hadoop.itcast;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Before;
import org.junit.Test;

public class HdfsUtil {

	private FileSystem fs;
	
	@Before
	public void Init() throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.default.name","hdfs://192.168.52.100:9000");
		
		fs = FileSystem.get(new URI("hdfs://192.168.52.100:9000"),conf,"hadoop");
	}
	
	
	/**
	 * 上传文件
	 * @throws IOException 
	 */
	@Test
	public void upload() throws IOException {
		Path dst = new Path("hdfs://192.168.52.100:9000/aa/qingshu.txt");
		FSDataOutputStream os = fs.create(dst);
		FileInputStream in = new FileInputStream("c:/qingshu.txt");
		IOUtils.copy(in, os);
	}
	
	
	/**
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * 
	 */
	@Test
	public void upload2() throws IllegalArgumentException, IOException {
		
		fs.copyFromLocalFile(new Path("c:/qingshu.txt"), new Path("hdfs://192.168.52.100:9000/aa/qingshu2.txt"));
		
	}
	
	
	
	/**
	 * 下载文件
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void download() throws IllegalArgumentException, IOException {
		fs.copyToLocalFile(false,new Path("hdfs://192.168.52.100:9000/aa/qingshu.txt"), new Path("e:/qingshu2.txt"),true);
	}
	
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws FileNotFoundException 
	 */
	public void listFiles() throws FileNotFoundException, IllegalArgumentException, IOException {
		
		RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/"), true);
		
		while (files.hasNext()) {
			LocatedFileStatus file = files.next();
			Path filePath = file.getPath();
			String fileName = filePath.getName();
			System.out.println(fileName);
		}
		
		
		System.out.println("-------------------------");
		
		FileStatus[] listStatus = fs.listStatus(new Path("/"));
		
		for(FileStatus status : listStatus) {
			String name = status.getPath().getName();
		}
		
		
	}
	
	/**
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * 
	 */
	@Test
	public void mkdir() throws IllegalArgumentException, IOException {
		fs.mkdirs(new Path("/aaa/bbb/ccc"));
	}
	
	/**
	 * 
	 */
	public void rm() {
		
	}
	
	
	
	public static void main(String[] args) {
		
		
		
	}
	
}
