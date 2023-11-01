import chisel3._
import chisel3.experimental._
import chisel3.util._

import chiseltest._
import chiseltest.formal._
import chiseltest.formal.BoundedCheck
import utest._
import org.scalatest.flatspec.AnyFlatSpec

import Adder.Param

object AdderTest extends TestSuite {
    val tests: Tests = Tests {
        test("mytest") {
            new Formal with HasTestName {
                def getTestName: String = s"adder"
            }.verify(new Adder.Adder, Seq(BoundedCheck(1)))
        }
    }
}