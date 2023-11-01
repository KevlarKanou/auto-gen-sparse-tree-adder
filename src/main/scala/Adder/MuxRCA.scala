package Adder

import chisel3._
import chisel3.experimental._
import chisel3.util._

class RCA extends Module {
    val io = IO(new Bundle{
        val a   = Input(UInt(4.W))
        val b   = Input(UInt(4.W))
        val cin = Input(UInt(1.W))
        val s   = Output(UInt(4.W))
    })

    // TODO: real RCA
    io.s := io.a + io.b + io.cin

} // RCA

class MuxRCA extends Module {
    val io = IO(new Bundle{
        val a   = Input(UInt(4.W))
        val b   = Input(UInt(4.W))
        val cin = Input(UInt(1.W))
        val s   = Output(UInt(4.W))
    })

    val RCA_Cin         = Module(new RCA)
    RCA_Cin.io.a        := io.a
    RCA_Cin.io.b        := io.b
    RCA_Cin.io.cin      := 1.U

    val RCA_noCin       = Module(new RCA)
    RCA_noCin.io.a      := io.a
    RCA_noCin.io.b      := io.b
    RCA_noCin.io.cin    := 0.U

    io.s                := (Fill(4, io.cin) & RCA_Cin.io.s) | (Fill(4, ~io.cin) & RCA_noCin.io.s)
} // MuxRCA

object MuxRCA {
    def apply(cin:UInt, a: UInt, b: UInt) = {
        val u = Module(new MuxRCA)
        u.io.a      := a
        u.io.b      := b
        u.io.cin    := cin
        u.io.s
    }
}