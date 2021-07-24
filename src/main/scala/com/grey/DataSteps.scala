package com.grey

import com.grey.environment.LocalSettings
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.SparkSession

import java.io.File
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

class DataSteps(spark: SparkSession) {

    private val localSettings = new LocalSettings()

    def dataSteps(): Unit = {

        /**
         * Import implicits for
         *   encoding (https://jaceklaskowski.gitbooks.io/mastering-apache-spark/spark-sql-Encoder.html)
         *   implicit conversions, e.g., converting a RDD to a DataFrames.
         *   access to the "$" notation.
         */
        import  spark.implicits._

        // List of files
        val dataObject: File = new File(localSettings.dataDirectory)
        val dataFiles: List[File] = FileUtils.listFiles(dataObject, Array("csv"),true).asScala.toList
        dataFiles.par.foreach(file => println(file.toString))





    }

}
