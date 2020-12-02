# Build the jar file and run

# Remove class and jar files
rm *.class
rm *.jar

# Compile
javac -classpath `yarn classpath` -d . CountRecsMapper.java
javac -classpath `yarn classpath` -d . CountRecsReducer.java
javac -classpath `yarn classpath`:. -d . CountRecs.java

# Create jar file
jar -cvf wc.jar *.class

# Run the program
hadoop jar wc.jar CountRecs /user/"$USER"/hw/input/WMT.csv /user/"$USER"/hw/output

