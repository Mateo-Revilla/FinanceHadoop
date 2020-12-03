import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * This outputs each date along with the values that accompany it in the form of a string, separated by a comma, 
 * excluding adj close, which is the only column which we will not use in the analysis. 
 */
public class AMZNmap extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString(); // converts text to String
		String date = "";
		String dateDigits = "";
		int dateInt = 0;
		String values = "";
		int commas = 0;
		for (int i = 0; i < line.length(); i++) { // There are no commas in the data set except for those
			if (line.charAt(i) == ',') { // used to separate columns in the .csv
				commas++;
			}
			if (commas == 0) { // Date
				date += line.charAt(i);
				if (Character.isDigit(line.charAt(i))) {
					dateDigits += line.charAt(i);
				}
			} else if ((commas != 5) && ((commas != 1) || (line.charAt(i) != ','))) {
				values += line.charAt(i);
			}
		}
		try {
			dateInt = Integer.parseInt(dateDigits);
		} catch (NumberFormatException e) { // skip line
		}
		if (commas >= 6 && dateInt >= 20191104 && dateInt <= 20201102) { // Eliminates valid dates not in period of
																			// study
			context.write(new Text(date), new Text(values));
		}
	}
}