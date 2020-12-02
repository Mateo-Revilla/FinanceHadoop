package formatWMT;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * This just changes the format of date to year-month-day and sends everything else to the reducer in values as a String.
 */
public class formatWMTmap extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		// We will get all 8 comma separated values, the first of which is the date

		String line = value.toString(); // converts text to String
		String date = "20"; //years are in this millennium 
		String day="";
		String month="";

		//year-month-day  from   month/day/year
		int i = 0;
		int slashes=0;
		while (i<line.length() && line.charAt(i) != ',') {
			if (Character.isDigit(line.charAt(i)) && slashes==2) {
				date+=line.charAt(i);
			} else if (Character.isDigit(line.charAt(i)) && slashes==1) {
				day+=line.charAt(i);
			} else if (Character.isDigit(line.charAt(i)) && slashes==0) {
				month+=line.charAt(i);
			} else {
				slashes++;
			}
			i++;
		}
		
		day=addZeroIfSingleDigit(day);
		month=addZeroIfSingleDigit(month);
		
		date+= "-" + month + "-" + day;
		
		context.write(new Text(date), new Text(line.substring(i+1)));
	}
	
	public static String addZeroIfSingleDigit(String str) {
		String newStr="";
		if (str.length()==1) {
			newStr="0"+str;
			return newStr;
		}
		return str;
	}
}