package com.grey.data

import com.grey.environment.LocalSettings
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DataType, StructType}

import java.nio.file.Paths
import scala.util.Try
import scala.util.control.Exception

class GetSchema(spark: SparkSession) {

    private val localSettings = new LocalSettings()

    def getSchema: Try[StructType] = {

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

        schema

    }

}
