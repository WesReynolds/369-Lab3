package csc369;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class CountryToURL {

	public static final Class OUTPUT_KEY_CLASS = Text.class;
	public static final Class OUTPUT_VALUE_CLASS = Text.class;

	// Mapper for access log file
	public static class LogMapper extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context)  throws IOException, InterruptedException {
			String record[] = value.toString().split(" ");

			Text clientIPAdd = new Text(record[0]);
			Text url = new Text("A " + record[6]);

			context.write(clientIPAdd, url);
		} 
	}

	// Mapper for hostname_country file
	public static class HostnameCountryMapper extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context)  throws IOException, InterruptedException {
			String record[] = value.toString().split(",");

			Text ipAdd = new Text(record[0]);
			Text country = new Text("B " + record[1]);

			context.write(ipAdd, country);
		}
	}


	//  Reducer: just one reducer class to perform the "join"
	public static class JoinReducer extends  Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)  throws IOException, InterruptedException {
			Text country = new Text();
			ArrayList<String> urls = new ArrayList<String>();

			for (Text value : values) {
				String tokens[] = value.toString().split(" ");
				if (tokens[0].equals("A")) {
					urls.add(tokens[1]);
				}
				else {
					country = new Text(tokens[1]);
				}
			}
			
			
			for (String url : urls) {
				context.write(country, new Text(url));	
			}
		}
	} 
}
