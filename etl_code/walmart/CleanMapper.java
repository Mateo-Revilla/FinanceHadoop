import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CleanMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		String line = value.toString();
		String[] fields = line.split(",");
		String date = fields[0];
		float open = Float.parseFloat(fields[1]);
		float high = Float.parseFloat(fields[2]);
		float low = Float.parseFloat(fields[3]);
		float close = Float.parseFloat(fields[4]);
		int volumn = Integer.parseInt(fields[6]);

		float pchange = (close-open)/open*100;	//printing out a percentage=, divide by 100 for actual number
		float pflut = (high-low)/open*100;
		
		//String output = String.format("%s\t%.2f\t%.2f\t%s",date, pchange, pflut, volumn);
		
		String output = String.format("%s,%.2f,%.2f,%s,",date, pchange, pflut, volumn);
		//don't really need to parse this because there is no changes made to the field, but I will put it down for now in case we need to manipulate data
		//adjusted close omitted because it is not really needed
		
		context.write(new Text(output), new IntWritable(1));
	}
}