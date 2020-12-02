import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * This outputs each date along with the values that accompany it in the form of a string, separated by a comma, 
 * excluding adj close, which is the only column which we will not use in the analysis. 
 */
public class ACAPmap extends Mapper<LongWritable, Text, Text, IntWritable> {

	public static final String validCountry = "USA";

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString(); // converts text to String
		String country = "";
		String logType = "";
		String dateImplemented = "";
		int logInt=0;
		// we only want ones in the US
		// and sort by log type and date, i.e. key should be country or date?
		// if key is date, that means it is easy to write per date in the reducer

		boolean inQuotes = false;
		boolean validQuote;
		char c;
		// For now, we only need ISO (commas =1), date implemented (commas = 6), and log
		// type (commas = 12)
		// Category and measure are also interesting, but not needed for analysis

		int commas = 0; // We will count commas to avoid using extra space with a long array
		for (int i = 0; i < line.length(); i++) {
			c = line.charAt(i);

			if (c == ',' && !inQuotes) {
				commas++;
				if (commas == 2 && !validCountry.equals(country)) { // ensures data is about US
					continue; // stop iterating, no point
				}
				// Determine if the next char could be a valid comma
			} else if (c == '"') {
				validQuote = true;
				if (i < line.length() - 1) {
					if (line.charAt(i + 1) != ',' && inQuotes) { // quotation mark is invalid if no comma after a second
						validQuote = false; // quotation mark
					}
				}
				if (validQuote) {
					inQuotes = !inQuotes;
				}
				// some of these CSV lines include line breaks, inc. USA ones

			} else if (commas == 1) {
				country += c;
			} else if (commas == 12) { // only execute if IF statement false
				dateImplemented += c;
			} else if (commas == 6) {
				logType += c;
			}
		}

		// if (c != ',') { // multiple lines in comment column; FIX THIS
		// FIX this
		// }

		int slashCount = 0;
		for (int i = 0; i < dateImplemented.length(); i++) {
			if (dateImplemented.charAt(i) == '/') {
				slashCount++; // ensure date is in correct format
			}
		}

		if (slashCount != 2) {
			dateImplemented = "BAD_RECORD";
		}
		
		if (logType.equals("Introduction / extension of measures")) {
			logInt = 1;
		} else if (logType.equals("Phase-out measure")) {
			logInt = -1;
		}
		
		String date = "20"; //years are in this millennium 
		String day="";
		String month="";

		//year-month-day  from   month/day/year
		int i = 0;
		int slashes=0;
		while (i<dateImplemented.length() && dateImplemented.charAt(i) != ',') {
			if (Character.isDigit(dateImplemented.charAt(i)) && slashes==2) {
				date+=dateImplemented.charAt(i);
			} else if (Character.isDigit(dateImplemented.charAt(i)) && slashes==1) {
				day+=dateImplemented.charAt(i);
			} else if (Character.isDigit(dateImplemented.charAt(i)) && slashes==0) {
				month+=dateImplemented.charAt(i);
			} else {
				slashes++;
			}
			i++;
		}
		
		day=addZeroIfSingleDigit(day);
		month=addZeroIfSingleDigit(month);
		
		date+= "-" + month + "-" + day;
		
		
		if (validCountry.equals(country) && slashCount == 2){
			context.write(new Text(date), new IntWritable(logInt));
		}
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