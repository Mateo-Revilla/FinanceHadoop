package formatWMT;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.OutputFormat;

/*
 * Kate Holland
 * This simple code reformats the Walmart data that Holly cleaned by just altering the format of the date
 * (changes slashes to hyphens) as well as the order of the columns in the table so that it matches the format
 * of my table and thus can run in Hive seamlessly. 
 */

public class formatWMT {
	public static void main(String[] args) throws Exception {

		Job job = new Job();
		job.setNumReduceTasks(1);
		job.setJarByClass(formatWMT.class);
		job.setJobName("formatWMT");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(formatWMTmap.class);
		job.setReducerClass(formatWMTreduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
