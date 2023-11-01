package Adder

import scala.math._

import chisel3._
import chisel3.experimental._
import chisel3.util._

class Adder extends Module {
    val io = IO(new Bundle{
        val A       = Input(UInt(Param.Width.W))
        val B       = Input(UInt(Param.Width.W))
        val Cin     = Input(UInt(1.W))
        val S       = Output(UInt(Param.Width.W))
        val Cout    = Output(UInt(1.W))
    })

    assert(Param.Width % 4 == 0)

    val G = io.A & io.B
    val P = io.A ^ io.B

    // Number of Layer
    val Layer = (log(Param.Width) / log(4.0)).ceil.toInt
    val LayerWidth = Param.Width / 4

    val GLayer = Wire(Vec(Layer, Vec(LayerWidth, Bool())))
    val PLayer = Wire(Vec(Layer, Vec(LayerWidth, Bool())))

    // Layer 0
    for (j <- 0 until LayerWidth) {
        val DotOpOut = DotOp(
            4,
            G(4*j+3, 4*j),
            P(4*j+3, 4*j),
        )
        GLayer(0)(j) := DotOpOut.Gn
        PLayer(0)(j) := DotOpOut.Pn
    }

    // Layer 1 ...
    for (i <- 1 until Layer; j <- 0 until LayerWidth) {
        val base = math.pow(4.0, i-1).toInt
        if (j < base) {
            GLayer(i)(j) := GLayer(i-1)(j)
            PLayer(i)(j) := PLayer(i-1)(j)
        }
        else {
            val n = if ((j / base) >= 3) 4 else ((j / base) + 1)
            val Gin = Wire(Vec(n, Bool()))
            val Pin = Wire(Vec(n, Bool()))
            for (k <- 0 until n) {
                Gin(k) := GLayer(i-1)(j-(n-k-1)*base)
                Pin(k) := PLayer(i-1)(j-(n-k-1)*base)
            }
            val DotOpOut = DotOp(
                n,
                Gin.asUInt,
                Pin.asUInt,
            )
            GLayer(i)(j) := DotOpOut.Gn
            PLayer(i)(j) := DotOpOut.Pn
        }
    }

    // Calculate Co
    val Co = Wire(Vec(LayerWidth, Bool()))
    for (j <- 0 until LayerWidth) {
        val CalcCoOut = DotOp(
            2,
            Cat(GLayer(Layer-1)(j), io.Cin),
            Cat(PLayer(Layer-1)(j), "b0".U),
        )
        Co(j) := CalcCoOut.Gn
    }
    io.Cout := Co(LayerWidth-1)

    val S = Wire(Vec(LayerWidth, UInt(4.W)))
    S(0) := MuxRCA(io.Cin, io.A(3,0), io.B(3,0))
    for (j <- 1 until LayerWidth) {
        S(j) := MuxRCA(Co(j-1).asUInt, io.A(4*j+3, 4*j), io.B(4*j+3, 4*j))
    }

    io.S := S.asUInt

    val ref = (io.A +& io.B) + io.Cin
    chisel3.assert(Cat(io.Cout, io.S) === ref)
} // Adder

