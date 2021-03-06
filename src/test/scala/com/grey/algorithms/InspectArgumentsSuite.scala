package com.grey.algorithms

import org.scalatest.Outcome
import org.scalatest.funsuite.FixtureAnyFunSuite

import scala.util.control.Exception

/**
 * Ref:
 *      https://www.scalatest.org/user_guide/sharing_fixtures -> Overriding withFixture(OneArgTest)
 *      https://www.artima.com/docs-scalatest-2.0.M5/org/scalatest/fixture/FunSuite.html
 *      https://www.scalatest.org/at_a_glance/FunSuite
 */
class InspectArgumentsSuite extends FixtureAnyFunSuite{

    case class FixtureParam(faulty: String, real: String)

    /**
     *
     * @param test: In context, the withFixture(test: OneArgTest) method creates the fixture
     *                        argument and passes onto the test function
     * @return
     */
    override def withFixture(test: OneArgTest): Outcome = {

        // Variables
        val faulty: String = "https://erroneous.eu"
        val real: String = "https://github.com/briefings"
        val theFixture = FixtureParam(faulty = faulty, real = real)

        // Settings
        val settings = Exception.allCatch.withTry(
            withFixture(test.toNoArgTest(theFixture))
        )
        if (settings.isSuccess) {
            settings.get
        } else {
            sys.error(settings.failed.get.getMessage)
        }

    }

    test("The inspectArguments method should fail if element zero of the argument array is a false URL string") { T =>
        assertThrows[java.lang.Exception](InspectArguments.inspectArguments(args = Array(T.faulty)))
    }

    test("The inspectArguments method should succeed if element zero of the argument array is a true URL string") { T =>
        val (verification, _) = InspectArguments.inspectArguments(args = Array(T.real))
        assert(verification.isSuccess)
    }

    test("On success, the inspectArguments method should also return the URL string"){ T =>
        val (_, urlString) = InspectArguments.inspectArguments(args = Array(T.real))
        assert(urlString.isInstanceOf[String])
        assertResult(T.real) {urlString}
    }

}
