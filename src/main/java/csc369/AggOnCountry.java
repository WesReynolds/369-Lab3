package csc369;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class AggOnCountry {

	public static final Class OUTPUT_KEY_CLASS = Text.class;
	public static final Class OUTPUT_VALUE_CLASS = IntWritable.class;

	public static class MapperImpl extends Mapper<LongWritable, Text, Text, IntWritable> {
		private final IntWritable one = new IntWritable(1);
		private Text word = new Text();

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			String[] record = value.toString().split("\\s");
			
			if (record.length > 1) {
				Text country = new Text(record[0]);
				IntWritable count = new IntWritable(Integer.parseInt(record[1]));

				context.write(country, count);
			}
			
			//context.write(value, one);
		}
	}

	public static class ReducerImpl extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			
			for (IntWritable value : values) {
				sum += value.get();
			}

			if (sum > 0) {
				result.set(sum);
				context.write(key, result);
			}
		}
	}
}
