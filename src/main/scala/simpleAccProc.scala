import chisel3._
import chisel3.stage._
import chisel3.util._


class simpleAccProc() extends Module {
  val io = IO(new Bundle {
  })

  val fetch :: decode :: execute :: Nil = Enum(3)
  val add :: mul :: st :: ld :: stop :: nop :: Nil = Enum(6)

  val PC = RegInit(0.U(16.W))
  val ACC = RegInit(0.U(16.W))
  val IR = new Instruction
  val State = RegInit(fetch)
  val Memory = Mem(256, UInt(16.W))

  switch(State){
    is(fetch){
      State := decode
      IR.addr := Memory.read(PC)(7, 0)
      IR.op := Memory.read(PC)(15, 8)
    }
    is(decode){

      State := execute

      switch(IR.op) {
        is(IR.add) {
          ACC := ACC + Memory.read(IR.addr)
        }
        is(IR.mul){
          ACC := ACC * Memory.read(IR.addr)
        }
        is(IR.ld){
          ACC := Memory.read(IR.addr)
        }
        is(IR.st){
          Memory.write(IR.addr, ACC)
        }
      }

    }
    is(execute){
      if(IR.op != IR.stop){
        State := fetch
        PC := PC + 1.U(1.W)
      }
    }
  }
}

object simpleAccProc extends App{
  val chiselStage = new chisel3.stage.ChiselStage();
  val TARGET_DIR = "./verilog/simpleAccPro";

  chiselStage.emitVerilog(
    new simpleAccProc(),
    Array("--target-dir", TARGET_DIR));
}
