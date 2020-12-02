# This script cleans out the HDFS directory, creates a fresh new HDFS direcrory, 
# fills the new HDFS directory with the data file, runs the code, and
# outputs the result (assuming 1 reducer was used).

# Clean out HDFS
./clean.sh

# Create new directory structure in HDFS
./setup.sh

# Run the code
./run.sh

# Output the result
./dump.sh

