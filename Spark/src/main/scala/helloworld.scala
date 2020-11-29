import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.classification.LinearSVC

object helloworld {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().config("spark.master", "local").getOrCreate()

    // Load training data
    val training = spark.read.format("csv").load("sample_data.txt")

    val lsvc = new LinearSVC()
      .setMaxIter(10)
      .setRegParam(0.1)

    // Fit the model
    val lsvcModel = lsvc.fit(training)

    // Print the coefficients and intercept for linear svc
    println(s"Coefficients: ${lsvcModel.coefficients} Intercept: ${lsvcModel.intercept}")
  }

  }
