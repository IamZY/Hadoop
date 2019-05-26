package cn.itcast.haddp.mr.ii;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import cn.itcast.haddp.mr.flowsort.SortMR;
import cn.itcast.haddp.mr.flowsort.SortMR.SortMapper;
import cn.itcast.haddp.mr.flowsort.SortMR.SortReducer;
import cn.itcast.haddp.mr.flowsum.FlowBean;

public class InverseIndexStepA {

	public static class StepOneMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context)
				throws IOException, InterruptedException {

			String line = value.toString();

			String[] fileds = StringUtils.split(line, " ");

			FileSplit inputSplit = (FileSplit) context.getInputSplit();

			Path path = inputSplit.getPath();

			String fileName = path.getName();

			for (String filed : fileds) {

				context.write(new Text(filed + "-->" + fileName), new LongWritable(1));

			}

		}

	}

	public static class StepOneReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
		@Override
		protected void reduce(Text key, Iterable<LongWritable> values,
				Reducer<Text, LongWritable, Text, LongWritable>.Context context)
				throws IOException, InterruptedException {

			long counter = 0;

			for (LongWritable value : values) {

				counter += value.get();

			}

			context.write(key, new LongWritable(counter));

		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		Job job = Job.getInstance(conf);

		job.setJarByClass(InverseIndexStepA.class);

		job.setMapperClass(StepOneMapper.class);
		job.setReducerClass(StepOneReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));

		Path output = new Path(args[1]);
		FileSystem fs = FileSystem.get(conf);

		if (fs.exists(output)) {
			fs.delete(output, true);
		}

		FileOutputFormat.setOutputPath(job, output);

		job.waitForCompletion(true);
	}

}
