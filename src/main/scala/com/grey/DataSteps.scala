package com.grey

import com.grey.environment.LocalSettings
import org.apache.commons.io.FileUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DataType, StructType}

import java.io.File
import java.nio.file.Paths
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
        import  spark.implicits._

        // Read-in the schema
        val fieldProperties: Try[RDD[String]] = Exception.allCatch.withTry(
            spark.sparkContext.textFile(Paths.get(localSettings.resourcesDirectory, "schemaOf.json").toString)
        )

        // Convert schema to StructType
        val schema: Try[StructType] = if (fieldProperties.isSuccess){
            Exception.allCatch.withTry(
                DataType.fromJson(fieldProperties.get.collect.mkString("")).asInstanceOf[StructType]
            )
        } else {
            sys.error(fieldProperties.failed.get.getMessage)
        }

        // List of files
        val dataObject: File = new File(localSettings.dataDirectory)
        val dataFiles: List[File] = FileUtils.listFiles(dataObject, Array("csv"),true).asScala.toList

        // Read
        dataFiles.foreach{file =>
            val readings = spark.read.format("csv")
                .schema(schema.get)
                .option("header", value = false)
                .load(file.toString)
            readings.show(5)
        }

    }

}
