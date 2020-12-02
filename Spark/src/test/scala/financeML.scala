import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg._
import scala.collection.mutable.ListBuffer
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.tree.model.RandomForestModel
import scala.collection.mutable.ArrayBuffer
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import java.io.PrintWriter


object financeML {
  def main(args: Array[String]): Unit = {

    // context for spark
    val spark = SparkSession.builder
      .master("local[*]")
      .appName("finance")
      .getOrCreate()


    // Load training data in LIBSVM format.
    //val data = MLUtils.loadLibSVMFile(spark.sparkContext, "sample_data.txt")

    val csvFile = spark.sparkContext.textFile("all.csv")
    val csvNoComma = csvFile.map(line=>{
      line.split(",")
    })

    val data = csvNoComma.map(line=>{

      //AMAZON CHANGE LABEL
      var label: Double = 0
      if (line(2).toDouble > 0) {
        label = 1
      }

      //AMAZON FLUCTUATION

      val flucA = line(1).toDouble


      //WALMART CHANGE
      var changePosW: Double = 0
      var changeNegW: Double = 0
      val changeW = line(4).toDouble

      if (changeW >= 0) {
        changePosW = changeW
      } else {
        changeNegW = -changeW
      }

      //WALMART FLUCTUATION
      val flucW: Double = line(3).toDouble

      //COVID CASES AND DEATHS
      val casesCovid: Double = line(7).toDouble
      val deathsCovid: Double = line(8).toDouble

      //SP500 CHANGE
      var changePosSP: Double = 0
      var changeNegSP: Double = 0
      val changeSP = line(6).toDouble

      if (changeSP >= 0) {
        changePosSP = changeSP
      } else {
        changeNegSP = -changeSP
      }

      //SP500 FLUCTUATION

      val flucSP = line(5).toDouble



      LabeledPoint(label, Vectors.dense(flucA, changePosW, changeNegW, casesCovid, deathsCovid, changeNegSP, changePosSP, flucSP))
    })

    var naiveBayesResults = ArrayBuffer[Double]()
    var logisticRegressionResults = ArrayBuffer[Double]()
    var randomForestResults = ArrayBuffer[Double]()

    var naiveBayesSum: Double = 0
    var logisticRegressionSum: Double = 0
    var randomForestSum: Double = 0



    def runModels(seed: Long) {
      // Split data into training (60%) and test (40%).
      val Array(training, test) = data.randomSplit(Array(0.75, 0.25), seed = seed)


      //NAIVE BAYES

      val modelNaiveBayes = NaiveBayes.train(training, lambda = 0.5, modelType = "multinomial")

      val predictionAndLabelmodelNaiveBayes = test.map(p => (modelNaiveBayes.predict(p.features), p.label))
      val accuracymodelNaiveBayes = 1.0 * predictionAndLabelmodelNaiveBayes.filter(x => x._1 == x._2).count() / test.count()



      //LOGISTIC REGRESSION

      val modelLR = new LogisticRegressionWithLBFGS()
        .setNumClasses(2)
        .run(training)
      val predictionAndLabelmodelLR = test.map(p => (modelLR.predict(p.features), p.label))
      val accuracymodelLR = 1.0 * predictionAndLabelmodelLR.filter(x => x._1 == x._2).count() / test.count()



      //RANDOM FOREST
      val numClasses = 2
      val categoricalFeaturesInfo = Map[Int, Int]()
      val numTrees = 3 // Use more in practice.
      val featureSubsetStrategy = "auto" // Let the algorithm choose.
      val impurity = "gini"
      val maxDepth = 2
      val maxBins = 8

      val modelRandomForest = RandomForest.trainClassifier(training, numClasses, categoricalFeaturesInfo,
        numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)
      val predictionAndLabelRandomForest = test.map(p => (modelRandomForest.predict(p.features), p.label))
      val accuracyRandomForest = 1.0 * predictionAndLabelRandomForest.filter(x => x._1 == x._2).count() / test.count()



      //OUTPUT
      /*println("accuraccy modelNaiveBayes:  " + accuracymodelNaiveBayes)
      println("accuraccy LR:  " + accuracymodelLR)
      println("accuraccy RandomForest:  " + accuracyRandomForest)*/

      //APPEND RESULT TO ARRAY
      naiveBayesResults += accuracymodelNaiveBayes
      logisticRegressionResults += accuracymodelLR
      randomForestResults += accuracyRandomForest

      //SUM RESULT
      naiveBayesSum += accuracymodelNaiveBayes
      logisticRegressionSum += accuracymodelLR
      randomForestSum += accuracyRandomForest
    }

    //RUN MODELS WITH DIFFERENT SEEDS
    var i = 0
    while (i < 5) {
      runModels(i)
      i += 1
    }


    //OUTPUT RESULTS
    val NBAverage = naiveBayesSum/naiveBayesResults.length
    val LRAverage =logisticRegressionSum/logisticRegressionResults.length
    val RFAverage = randomForestSum/randomForestResults.length

    val conf = new Configuration()
    val fs= FileSystem.get(conf)
    val output = fs.create(new Path("output/output.txt"))
    val writer = new PrintWriter(output)
    writer.write("Naive Bayes Average: " + NBAverage + "\n")
    writer.write("Logistic Regression Average: " + LRAverage + "\n")
    writer.write("Random Forest Average: " + RFAverage + "\n")
    writer.write("Naive Bayes Results: " + naiveBayesResults.toString() + "\n")
    writer.write("Logistic Regression Results: " + logisticRegressionResults.toString() + "\n")
    writer.write("Random Forest Results: " + randomForestResults.toString() + "\n")
    writer.close()


  }

}
