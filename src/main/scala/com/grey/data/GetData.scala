package com.grey.data

import com.grey.types.CaseClassOf
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.types.StructType

import java.io.File
import scala.collection.parallel.immutable.ParSeq

class GetData(spark: SparkSession) {

    def getData(dataFiles: List[File], schema: StructType): Dataset[Row] = {

        // Read
        val partitionsOfInstances: ParSeq[DataFrame] =  dataFiles.par.map{ file =>

            // Markers
            val fileName = file.getName.dropRight(".csv".length)
            val setting = file.getParentFile.getName.take(2).toUpperCase

            val readings = spark.read.format("csv")
                .schema(schema)
                .option("header", value = false)
                .load(file.toString)

            val extended = readings.withColumn("sex", lit(fileName.takeRight(1)))
                .withColumn("setting", lit(setting))
                .withColumn("file_name", lit(fileName))

            extended
        }

        // Reduce
        val instances: DataFrame = partitionsOfInstances.reduce(_ union _)

        // Transform to DataSet
        val caseClassOf = CaseClassOf.caseClassOf(schema = instances.schema)
        instances.as(caseClassOf)

    }

}
