hadoop jar /opt/cloudera/parcels/CDH-5.15.0-1.cdh5.15.0.p0.21/lib/hadoop-mapreduce/hadoop-streaming.jar \
-D mapreduce.job.reduces=1 \
-files hdfs://dumbo/user/mlk512/covid/analysis/mapreduce/mapper1.py,hdfs://dumbo/user/mlk512/covid/analysis/mapreduce/reducer.py \
-mapper "python mapper1.py" \
-reducer "python reducer.py" \
-input /user/mlk512/covid/all.csv \
-output /user/mlk512/job1

hadoop jar /opt/cloudera/parcels/CDH-5.15.0-1.cdh5.15.0.p0.21/lib/hadoop-mapreduce/hadoop-streaming.jar \
-D mapreduce.job.reduces=1 \
-files hdfs://dumbo/user/mlk512/covid/analysis/mapreduce/mapper2.py,hdfs://dumbo/user/mlk512/covid/analysis/mapreduce/reducer.py \
-mapper "python mapper2.py" \
-reducer "python reducer.py" \
-input /user/mlk512/covid/all.csv \
-output /user/mlk512/job2

hadoop jar /opt/cloudera/parcels/CDH-5.15.0-1.cdh5.15.0.p0.21/lib/hadoop-mapreduce/hadoop-streaming.jar \
-D mapreduce.job.reduces=1 \
-files hdfs://dumbo/user/mlk512/covid/analysis/mapreduce/mapper3.py,hdfs://dumbo/user/mlk512/covid/analysis/mapreduce/reducer.py \
-mapper "python mapper3.py" \
-reducer "python reducer.py" \
-input /user/mlk512/covid/all.csv \
-output /user/mlk512/job3

hadoop jar /opt/cloudera/parcels/CDH-5.15.0-1.cdh5.15.0.p0.21/lib/hadoop-mapreduce/hadoop-streaming.jar \
-D mapreduce.job.reduces=1 \
-files hdfs://dumbo/user/mlk512/covid/analysis/mapreduce/mapper4.py,hdfs://dumbo/user/mlk512/covid/analysis/mapreduce/reducer.py \
-mapper "python mapper4.py" \
-reducer "python reducer.py" \
-input /user/mlk512/covid/all.csv \
-output /user/mlk512/job4
