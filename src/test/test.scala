package Adder

import chisel3._
import chisel3.util._
import chiseltest._
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec

class FormalTest extends AnyFlatSpec with ChiselScalatestTester with Formal {
  "Test" should "pass" in {
    verify(new Adder, Seq(BoundedCheck(1)))
  }
}
