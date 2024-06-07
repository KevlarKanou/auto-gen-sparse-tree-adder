package Adder

import chisel3._
import chisel3.experimental._
import chisel3.util._

class DotOpOut extends Bundle {
    val Gn  = Output(UInt(1.W))
    val Pn  = Output(UInt(1.W))
}

class DotOp(n: Int) extends Module {
    val io = IO(new Bundle{
        val G   = Input(UInt(n.W))
        val P   = Input(UInt(n.W))
    })
    val DotOpOut = IO(new DotOpOut)

    DotOpOut.Pn   := io.P.andR

    var x = io.G(n-1)
    for (i <- n - 2 to 0 by -1) {
        x = x | (io.P(n-1, i+1).andR & io.G(i))
    }
    DotOpOut.Gn := x
} // DotOp

object DotOp {
    def apply(n: Int, G: UInt, P:UInt) = {
        val u = Module(new DotOp(n))
        u.io.G := G
        u.io.P := P
        u.DotOpOut
    }
}
