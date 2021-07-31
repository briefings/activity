package com.grey.algorithms


import com.grey.environment.LocalSettings
import org.apache.spark.sql.functions.{collect_list, struct}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

import java.nio.file.Paths


/**
 *
 * @param spark: Spark Session instance
 */
class PolarCoordinates(spark: SparkSession) {

  private val localSettings = new LocalSettings()

  /**
   *
   * @param data: The data
   */
  def polarCoordinates(data: Dataset[Row]): Unit = {


    import spark.implicits._


    // Time, frequency, & phase
    val focus: DataFrame = data.select($"setting", $"file_name".as("subject"), 
      $"time", $"frequency", $"phase")
      .groupBy($"setting", $"subject")
      .agg(
        collect_list(struct($"time", $"frequency", $"phase")).as("events")
      )


    // Then
    val focusSet: Dataset[Subject] = focus.as[Subject]


    // Export
    val settings: Array[String] = focusSet.select($"setting")
      .map(_.getAs[String]("setting")).collect().distinct
    settings.foreach(println(_))

    settings.par.foreach{segment =>
      val lines: Dataset[Subject] = focusSet.filter($"setting" === segment)
      lines.coalesce(1)
        .write.parquet(Paths.get(localSettings.warehouseDirectory, segment).toString)
    }

  }

}


/**
 *
 * @param subject: Subject
 * @param events: Events
 */
case class Subject(setting: String, subject: String, events: List[Events])


/**
 *
 * @param time: Stop watch time
 * @param frequency: Frequency
 * @param phase: Phase
 */
case class Events(time: Double, frequency: Double, phase: Double)
