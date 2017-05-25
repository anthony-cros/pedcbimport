package pedcbimport.pipelines.copynumber

// ===========================================================================
case class `RawCopyNumber`(
    `chromosome_number`:  String /* 1 */,
    `start`:              String /* 2 */,
    `end`:                String /* 3 */,
    `count`:              String /* 4 */,
    `type`:               String /* 5 */) {

  require(
    count.toInt >= 0 &&
    count.toInt < 100,  // chosen 100 as an arbitrary upper bound, may need to raise that
    this)
  
}

// ---------------------------------------------------------------------------
object `RawCopyNumber` {
  def apply(it: Iterator[String]): RawCopyNumber =
    RawCopyNumber(
      `chromosome_number` = it.next() /* 1 */,
      `start`             = it.next() /* 2 */,
      `end`               = it.next() /* 3 */,
      `count`             = it.next() /* 4 */,
      `type`              = it.next() /* 5 */)}  

// ===========================================================================
