package csc369;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class SortTuples {

	public static final Class OUTPUT_KEY_CLASS = CountryCountPair.class;
	public static final Class OUTPUT_VALUE_CLASS = Text.class;

	public static class MapperImpl extends Mapper<LongWritable, Text, CountryCountPair, Text> {
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String record[] = value.toString().split(" ");
			
			String country = record[0];
			String url = record[1];
			int count = Integer.parseInt(record[2]);
						
			context.write(new CountryCountPair(country, count), new Text(url));			
		}
	}
	
	// used to perform secondary sort on temperature
	public static class SortComparator extends WritableComparator {
		protected SortComparator() {
			super(CountryCountPair.class, true);
		}

		@Override
		public int compare(WritableComparable wc1, WritableComparable wc2) {
			CountryCountPair pair = (CountryCountPair) wc1;
			CountryCountPair pair2 = (CountryCountPair) wc2;
			return pair.compareTo(pair2);
		}
	}

	public static class ReducerImpl extends Reducer<CountryCountPair, Text, Text, IntWritable> {
		@Override
		protected void reduce(CountryCountPair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			String country = key.getCountry();
			int count = key.getCount();
			
			for (Text value : values) {
				context.write(new Text(country + " " + value), new IntWritable(count));
			}
		}
	}
}
