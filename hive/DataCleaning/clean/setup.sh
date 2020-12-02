# Setup the HDFS directory stucture and populate it. 

# Create new directory structure in HDFS
hdfs dfs -mkdir final_project
hdfs dfs -mkdir final_project/input

# Populate the input directory in HDFS with the input file
hdfs dfs -put WMT.csv final_project/input

# Verify what is in the input data directory
hdfs dfs -ls final_project/input
