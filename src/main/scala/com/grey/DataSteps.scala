package com.grey

import com.grey.data.{DataUnload, DataUnzip}
import com.grey.environment.LocalSettings
import org.apache.spark.sql.SparkSession

import scala.util.Try
import scala.util.control.Exception

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
        val archiveString = new DataUnload(directory = localSettings.archiveDirectory).dataUnload(urlString = urlString)
        val deArchive: Try[Unit] = Exception.allCatch.withTry(
            new DataUnzip(directory = localSettings.dataDirectory).dataUnzip (archiveString = archiveString)
        )

        println(deArchive.isSuccess)




    }

}
