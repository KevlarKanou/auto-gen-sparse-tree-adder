package Adder

import chisel3.stage.ChiselStage

object Gen extends App {
    (new ChiselStage).emitVerilog(new Adder, args)
}