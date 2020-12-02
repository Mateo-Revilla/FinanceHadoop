import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
 * This takes "neighborhood_group neighborhood" as a key input and outputs it
 * along with the number of times it appears.
 */
public class ACAPreduce extends Reducer<Text, IntWritable, Text, IntWritable> {
	public void reduce(Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {

		int score=0;

		for (IntWritable value : values) {
			score += value.get();
		}
		context.write(key, new IntWritable(score));
	}
}