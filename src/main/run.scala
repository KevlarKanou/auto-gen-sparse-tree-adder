import chisel3._
import _root_.circt.stage.ChiselStage

object Gen extends App {
    ChiselStage.emitSystemVerilogFile(
        new Adder.Adder,
        firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info")
    )
}
