package csc369;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Partitioner;

public class SortTuples {

	public static final Class OUTPUT_KEY_CLASS = CountryCountPair.class;
	public static final Class OUTPUT_VALUE_CLASS = Text.class;

	public static class MapperImpl extends Mapper<LongWritable, Text, CountryCountPair, Text> {
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String record[] = value.toString().split("\\s");
			
			if (record.length > 1) {
				String country = record[0];
				String url = record[1];
				int count = Integer.parseInt(record[2]);
						
				context.write(new CountryCountPair(country, count), new Text(url));
			}
			
			//context.write(new CountryCountPair("Brazil", 7), value);

		}
	}
	
	// controls the reducer to which a particular (key, value) is sent
	public static class PartitionerImpl extends Partitioner<CountryCountPair, Text> {
		@Override
		public int getPartition(CountryCountPair pair, Text temperature, int numberOfPartitions) {
			return Math.abs(pair.getCountry().hashCode() % numberOfPartitions);
		}
	}
	
	// used to group data by (country, count)
	public static class GroupingComparator extends WritableComparator {
		public GroupingComparator() {
			super(CountryCountPair.class, true);
		}

		@Override
		public int compare(WritableComparable wc1, WritableComparable wc2) {
			CountryCountPair pair = (CountryCountPair) wc1;
			CountryCountPair pair2 = (CountryCountPair) wc2;
			return pair.getCountry().compareTo(pair2.getCountry());
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
			Text country = key.getCountry();
			IntWritable count = key.getCount();
			
			for (Text value : values) {
				context.write(new Text(country.toString() + " " + value), count);
			}
			
			context.write(new Text("Reduced"), new IntWritable(1));
		}
	}
}
