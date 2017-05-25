package pedcbimport.pipelines.expression

// ===========================================================================
case class RawExpression(  
  `gene_id`:         String /* 1 */,
  `transcript_id(s)`: String /* 2 */,
  `length`:          String /* 3 */,
  `effective_length`: String /* 4 */,
  `expected_count`:  String /* 5 */,
  `TPM`:             String /* 6 */,
  `FPKM`:            String /* 7 */) {
  
  require(FPKM.toDouble >= 0.0, this)
  
}
  
// ---------------------------------------------------------------------------
object RawExpression {
  
  def apply(it: Iterator[String]): RawExpression = 
    RawExpression(      
      `gene_id`         = it.next() /* 1 */,
      `transcript_id(s)`= it.next() /* 2 */,
      `length`          = it.next() /* 3 */,
      `effective_length`= it.next() /* 4 */,
      `expected_count`  = it.next() /* 5 */,
      `TPM`             = it.next() /* 6 */,
      `FPKM`            = it.next() /* 7 */)  

}

// ===========================================================================
