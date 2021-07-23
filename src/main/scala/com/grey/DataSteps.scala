package com.grey

import org.apache.spark.sql.SparkSession

class DataSteps(spark: SparkSession) {

    def dataSteps(): Unit = {

        /**
         * Import implicits for
         *   encoding (https://jaceklaskowski.gitbooks.io/mastering-apache-spark/spark-sql-Encoder.html)
         *   implicit conversions, e.g., converting a RDD to a DataFrames.
         *   access to the "$" notation.
         */
        import spark.implicits._

    }

}
