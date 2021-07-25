package com.grey.algorithms

import scala.util.Try

object InspectArguments {

    def inspectArguments(args: Array[String]): (Try[Boolean], String) = {

        // Is the string args(0) a URL?
        val isURL: Try[Boolean] = new IsURL().isURL(args(0))

        // If args(0) is a valid URL string, return ...
        if (isURL.isSuccess) {
            (isURL, args(0))
        } else {
            sys.error(isURL.failed.get.getMessage)
        }

    }

}
