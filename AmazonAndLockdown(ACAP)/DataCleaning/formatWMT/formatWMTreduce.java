package formatWMT;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.NullWritable;

public class formatWMTreduce extends Reducer<Text, Text, NullWritable, Text> {
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		String line = "";
		String volume = "";
		String fluctuation = "";
		String changeFromOpening = "";

		for (Text value : values) {

			line = key.toString() + ",";
			String[] tokens = value.toString().split(","); // There are no commas in the data set except for those
															// used to separate columns in the .csv
			try {
				for (int i = 0; i < 4; i++) {
					line += tokens[i] + ",";
				}
				changeFromOpening = tokens[4];
				fluctuation = tokens[5];
				volume = tokens[6];
				line += volume + "," + fluctuation + "," + changeFromOpening;
			} catch (ArrayIndexOutOfBoundsException e) {
				line = "BAD_NUMFORMAT";
			}
		}
		context.write(NullWritable.get(), new Text(line));
	}
}