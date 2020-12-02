# Setup the HDFS directory stucture and populate it. 

# Create new directory structure in HDFS
hdfs dfs -mkdir hw
hdfs dfs -mkdir hw/input

# Populate the input directory in HDFS with the input file
hdfs dfs -put WMT.csv hw/input

# Verify what is in the input data directory
hdfs dfs -ls hw/input
