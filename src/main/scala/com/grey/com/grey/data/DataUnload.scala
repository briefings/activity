package com.grey.com.grey.data

import com.grey.net.IsExistURL

import java.io.File
import java.net.URL
import scala.language.postfixOps
import scala.sys.process._
import scala.util.Try
import scala.util.control.Exception

class DataUnload {


    def dataUnload(urlString: String, unloadString: String): Try[String] = {

        // Valid URL?
        val isExistURL: Try[Boolean] = new IsExistURL().isExistURL(urlString = urlString)

        // Unload
        val unload: Try[String] = if (isExistURL.isSuccess) {
            Exception.allCatch.withTry(
                new URL(urlString) #> new File(unloadString) !!
            )
        } else {
            sys.error(isExistURL.failed.get.getMessage)
        }

        // Hence
        unload

    }


}
