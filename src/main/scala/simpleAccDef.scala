import chisel3._
import chisel3.util.Enum

class Instruction() extends Bundle {

  val add :: mul :: st :: ld :: stop :: nop :: Nil = Enum(6)

  val addr = RegInit(0.U(8.W));
  val op = RegInit(nop);
}