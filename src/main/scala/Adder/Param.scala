package Adder

import chisel3._
import chisel3.experimental._
import chisel3.util._

abstract class Parameter {
    def Width: Int
}

object Param extends Parameter {
    val Width = 64
}