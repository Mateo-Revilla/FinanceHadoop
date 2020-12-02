import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CleanMapper extends Mapper<LongWritable, Text, Text, Text> {

 

  @Override public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {



    String line = value.toString();

    //DROP FIRST ROW
    if (line.contains("Date")) {
      return;
    }
    
    //SPLIT
    String[] lineArray = line.split(",");

    if (lineArray.length == 7 ) {
      String date = lineArray[0];
      String high = lineArray[2];
      String low = lineArray[3];
      String open = lineArray[1];
      String close = lineArray[4];
      String volume = lineArray[6];

      float openFloat = Float.parseFloat(open);
      float closeFloat = Float.parseFloat(close);
      float highFloat = Float.parseFloat(high);
      float lowFloat= Float.parseFloat(low);
    
      String change = String.valueOf((closeFloat-openFloat)/openFloat);
      String fluctuation = String.valueOf((highFloat-lowFloat)/openFloat);



      context.write(new Text(""), new Text(date + "," + close + "," + high + "," + open + "," + volume + "," + low + "," + change + "," + fluctuation));
	}
    }
  }
