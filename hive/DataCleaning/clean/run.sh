# Build the jar file and run

# Remove class and jar files
rm *.class
rm *.jar

# Compile
javac -classpath `yarn classpath` -d . CleanMapper.java
javac -classpath `yarn classpath` -d . CleanReducer.java
javac -classpath `yarn classpath`:. -d . Clean.java

# Create jar file
jar -cvf wc.jar *.class

# Run the program
hadoop jar wc.jar Clean /user/"$USER"/final_project/input/WMT.csv /user/"$USER"/final_project/output

