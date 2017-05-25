package pedcbimport.study

// ===========================================================================
case class `RawStudy`(
  `CANCER_STUDY_ID`:         String /* 1 */,
  `CANCER_STUDY_IDENTIFIER`: String /* 2 */,
  `TYPE_OF_CANCER_ID`:       String /* 3 */,
  `NAME`:                    String /* 4 */,
  `SHORT_NAME`:              String /* 5 */,
  `DESCRIPTION`:             String /* 6 */,
  `PUBLIC`:                  String /* 7 */,
  `PMID`:                    String /* 8 */,
  `CITATION`:                String /* 9 */,
  `GROUPS`:                  String /* 10 */,
  `LINK_TO_HARVEST`:         String /* 11 */,
  `NORMALS_TISSUE_MAPPING`:  String /* 12 */,
  `IS_ADULT`:                String /* 13 */)

// ---------------------------------------------------------------------------
object `RawStudy` {
  
  def apply(it: Iterator[String]): `RawStudy` =
    `RawStudy`(
      `CANCER_STUDY_ID`         = it.next() /* 1 */,
      `CANCER_STUDY_IDENTIFIER` = it.next() /* 2 */,
      `TYPE_OF_CANCER_ID`       = it.next() /* 3 */,
      `NAME`                    = it.next() /* 4 */,
      `SHORT_NAME`              = it.next() /* 5 */,
      `DESCRIPTION`             = it.next() /* 6 */,
      `PUBLIC`                  = it.next() /* 7 */,
      `PMID`                    = it.next() /* 8 */,
      `CITATION`                = it.next() /* 9 */,
      `GROUPS`                  = it.next() /* 10 */,
      `LINK_TO_HARVEST`         = it.next() /* 11 */,
      `NORMALS_TISSUE_MAPPING`  = it.next() /* 12 */,
      `IS_ADULT`                = it.next() /* 13 */)
}

// ===========================================================================
