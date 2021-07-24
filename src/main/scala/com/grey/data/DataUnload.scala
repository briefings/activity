package com.grey.data

import com.grey.net.IsURL

import java.io.File
import java.net.URL
import java.nio.file.Paths
import scala.language.postfixOps
import scala.sys.process._
import scala.util.Try
import scala.util.control.Exception

/**
 *
 * @param directory: The file will be unloaded into <directory>
 */
class DataUnload(directory: String) {

    /**
     *
     * @param urlString: The URL of a file
     * @return The URI of the unloaded file
     */
    def dataUnload(urlString: String): String = {

        // Valid URL?
        val isExistURL: Try[Boolean] = new IsURL().isURL(urlString = urlString)

        // Name strings
        val fileName: String = urlString.split("/").reverse.head
        val fileString: String = Paths.get(directory, fileName).toString

        // Unload
        val unload: Try[String] = if (isExistURL.isSuccess) {
            Exception.allCatch.withTry(
                new URL(urlString) #> new File(fileString) !!
            )
        } else {
            sys.error(isExistURL.failed.get.getMessage)
        }

        // Hence
        if (unload.isSuccess){
            fileString
        } else {
           sys.error(unload.failed.get.getMessage)
        }

    }



}
