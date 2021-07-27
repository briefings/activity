package com.grey.types

import org.apache.spark.sql.types._

class HiveDataType {

    def hiveDataType(dataTypeOfVariable: DataType): String = {

        dataTypeOfVariable match {

            case _:ByteType => "tinyint"
            case _:ShortType => "smallint" // NOT "shortint"
            case _:IntegerType => "integer"
            case _:LongType => "bigint"
            case _:FloatType => "float"
            case _:DoubleType => "double"
            case _:DecimalType => "decimal"
            case _:StringType => "string"
            case _:BinaryType => "binary"
            case _:BooleanType => "boolean"
            case _:TimestampType => "timestamp" // Beware
            case _:DateType => "date"
            case _:ArrayType => "array"
            case _:StructType => "struct"
            case _:MapType => "map"
            case _ => "string"

        }

    }


}
