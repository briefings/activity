package com.grey

import com.grey.data.{GetData, GetSchema}
import com.grey.environment.LocalSettings
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.types.StructType
import org.apache.spark.storage.StorageLevel

import java.io.File
import scala.collection.JavaConverters.collectionAsScalaIterableConverter
import scala.util.Try
import scala.util.control.Exception

class DataSteps(spark: SparkSession) {

    private val localSettings = new LocalSettings()

    def dataSteps(): Unit = {

        /**
         * Import implicits for
         *   encoding (https://jaceklaskowski.gitbooks.io/mastering-apache-spark/spark-sql-Encoder.html)
         *   implicit conversions, e.g., converting a RDD to a DataFrames.
         *   access to the "$" notation.
         */
        // import  spark.implicits._

        // Get the schema of the data set
        val schema: Try[StructType] = new GetSchema(spark = spark).getSchema

        // The list of files
        val dataObject: File = new File(localSettings.dataDirectory)
        val dataFiles: Try[List[File]] = Exception.allCatch.withTry(
            FileUtils.listFiles(dataObject, Array("csv"),true).asScala.toList
        )

        // The data
        val data: Dataset[Row] = new GetData(spark = spark).getData(dataFiles = dataFiles.get, schema = schema.get)
        data.persist(StorageLevel.MEMORY_ONLY)

        // Temporary view
        data.createOrReplaceTempView("activity")

        // Hence
        data.show(5)

    }

}
