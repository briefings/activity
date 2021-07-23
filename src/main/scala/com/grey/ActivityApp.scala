package com.grey

import com.grey.environment.{Directories, LocalSettings}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

import scala.collection.parallel.immutable.ParSeq

object ActivityApp {

    private val localSettings = new LocalSettings()

    def main(args: Array[String]): Unit = {

        // Minimising log information output
        Logger.getLogger("org").setLevel(Level.ERROR)
        Logger.getLogger("akka").setLevel(Level.ERROR)

        // Spark Session
        // Serializer: https://spark.apache.org/docs/2.4.8/tuning.html
        val spark = SparkSession.builder().appName("Activity")
            .config("spark.serializer", "org.apache.spark.serializer.KyroSerializer")
            .config("spark.sql.warehouse.dir", localSettings.warehouseDirectory)
            .getOrCreate()

        // Prepare local directories
        val directories = new Directories()
        val directoriesReset: ParSeq[Boolean] = List(localSettings.dataDirectory, localSettings.warehouseDirectory)
            .par.map( directory => directories.directoryReset(directory) )

        // Hence
        if (directoriesReset.forall(_ == true)) {
            new DataSteps(spark = spark)
        } else {
            // Superfluous because directoriesReset( ) address all directoriesReset( ) errors
            sys.exit()
        }

    }

}
