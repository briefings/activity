package com.grey.algorithms

import org.scalatest.funsuite.AnyFunSuite

import scala.util.Try

class IsURLSuite extends AnyFunSuite{

    test("The isURL method should fail for each false URL string"){
        assertThrows[java.lang.Exception](new IsURL().isURL(urlString = "https://erroneous.eu"))
    }

    test("The isURL method should succeed for each true URL string"){
        val verification: Try[Boolean] = new IsURL().isURL(urlString = "https://github.com/briefings")
        assert(verification.isSuccess)
    }

}
