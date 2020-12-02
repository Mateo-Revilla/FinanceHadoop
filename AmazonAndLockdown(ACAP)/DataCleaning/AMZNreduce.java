import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.NullWritable;

public class AMZNreduce extends Reducer<Text, Text, NullWritable, Text> {
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		String line = "";
		float openPrice;
		float high;
		float low;
		float closePrice;
		int volume;
		float fluctuation;
		float changeFromOpening;
		boolean numberE = false;

		for (Text value : values) {
			
			line = key.toString() + ","+ value.toString(); // Count commas to avoid using extra space with a long array
			String[] tokens = value.toString().split(","); // There are no commas in the data set except for those
															// used to separate columns in the .csv
			try {
				openPrice = Float.parseFloat(tokens[0]);
				high = Float.parseFloat(tokens[1]);
				low = Float.parseFloat(tokens[2]);
				closePrice = Float.parseFloat(tokens[3]);
				volume = Integer.parseInt(tokens[4]);
				fluctuation = (high - low) / openPrice;
				changeFromOpening = (closePrice-openPrice)/openPrice;
				line += "," + fluctuation + "," + changeFromOpening;
			} catch (NumberFormatException e) {
				numberE = true;
				line = "BAD_NUMFORMAT";
			}
		}
		context.write(NullWritable.get(), new Text(line));
	}
}