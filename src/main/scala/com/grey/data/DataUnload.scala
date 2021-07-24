package com.grey.data

import com.grey.net.IsExistURL

import java.io.File
import java.net.URL
import scala.language.postfixOps
import scala.sys.process._
import scala.util.Try
import scala.util.control.Exception

class DataUnload {

    /**
     *
     * @param urlString: The URL of a file
     * @param fileString: The unloaded file will be saved as <fileString>
     * @return
     */
    def dataUnload(urlString: String, fileString: String): Try[String] = {

        // Valid URL?
        val isExistURL: Try[Boolean] = new IsExistURL().isExistURL(urlString = urlString)

        // Unload
        val unload: Try[String] = if (isExistURL.isSuccess) {
            Exception.allCatch.withTry(
                new URL(urlString) #> new File(fileString) !!
            )
        } else {
            sys.error(isExistURL.failed.get.getMessage)
        }

        // Hence
        unload

    }



}
