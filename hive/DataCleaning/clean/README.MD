1. MapReduce:
	To clean data and get result printed, cd into the folder called "clean" and execute "./go.sh". You will get data listed in the format:
	<date>	<percentage change>	<percentage flutuation>		<trading volumn>	0
	Note that:
	1. 0 at the end is just a filler, just ignore it
	2. percentage change means the change from open price to closing price of that day
	3. percentage flutuation means the difference between highest and lowest trade of the day proportional to the opening price

2. After you get the result from MapReduce written to HDFS, copy the file from MapReduce output location to hive input location (you can also just use the same directory for the two, I personally prefer a backup)

Below are the steps that I did on hive in commandline to put data into hiveQL:

	beeline

	!connect jdbc:hive2://babar.es.its.nyu.edu:10000/

	use <netID>;

	create external table walmart(date date, pchange float, pflut float, volumn bigint, filler tinyint) row format delimited fields terminated by '\t' location '/user/<netID>/<hiveDirectoryLocation>';

	show tables;

	describe walmart;


You should get the structure of hive table entries like this:
+-----------+------------+----------+--+
| col_name  | data_type  | comment  |
+-----------+------------+----------+--+
| date      | date       |          |
| pchange   | float      |          |
| pflut     | float      |          |
| volumn    | bigint     |          |
| filler    | tinyint    |          |
+-----------+------------+----------+--+