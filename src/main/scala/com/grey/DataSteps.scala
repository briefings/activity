package com.grey

import com.grey.data.{DataUnload, DataUnzip}
import com.grey.environment.LocalSettings
import org.apache.spark.sql.SparkSession

import scala.util.Try

class DataSteps(spark: SparkSession) {

    private val localSettings = new LocalSettings()

    def dataSteps(urlString: String): Unit = {

        /**
         * Import implicits for
         *   encoding (https://jaceklaskowski.gitbooks.io/mastering-apache-spark/spark-sql-Encoder.html)
         *   implicit conversions, e.g., converting a RDD to a DataFrames.
         *   access to the "$" notation.
         */
        import spark.implicits._

        // Unload & Unzip
        val unload = (arg: String) => new DataUnload(directory = localSettings.archiveDirectory).dataUnload(urlString = arg)
        val unzip = (arg: String) => new DataUnzip(directory = localSettings.dataDirectory).dataUnzip(archiveString = arg)
        
        val next: String => Boolean = unload andThen unzip
        println(next)

    }

}
