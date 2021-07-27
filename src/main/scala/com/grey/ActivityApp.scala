package com.grey

import com.grey.environment.{Directories, LocalSettings}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

import scala.collection.parallel.immutable.ParSeq

object ActivityApp {

    private val localSettings: LocalSettings = new LocalSettings()

    def main(args: Array[String]): Unit = {

        // Arguments
        /*
        if (args.length == 0) {
            sys.error("The data URL is required.")
        }
        */

        // Inspect Arguments
        /*
        val inspectArguments = InspectArguments
        val (_, urlString: String) = inspectArguments.inspectArguments(args = args)
        */

        // Minimising log information output
        Logger.getLogger("org").setLevel(Level.ERROR)
        Logger.getLogger("akka").setLevel(Level.ERROR)

        // Spark Session
        // Serializer: https://spark.apache.org/docs/2.4.8/tuning.html
        val spark = SparkSession.builder().appName("Activity")
            .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .config("spark.sql.warehouse.dir", localSettings.warehouseDirectory)
            .config("spark.worker.cleanup.enabled", value = true)
            .getOrCreate()

        // Prepare local directories
        val directories = new Directories()
        val directoriesReset: ParSeq[Boolean] = List(localSettings.warehouseDirectory).
            par.map(directory => directories.directoryReset(directory))

        // Hence
        if (directoriesReset.forall(_ == true)) {
            new DataSteps(spark = spark).dataSteps()
        } else {
            // Superfluous because directoriesReset( ) address all directoriesReset( ) errors
            sys.exit()
        }

    }

}
