package pedcbimport.references

import utils.Implicits.StringImplicits
import utils.Implicits.SeqImplicits
import pedcbimport.common.Wrapper._

// ===========================================================================
case class CosmicLookup(private val data: Map[(Chromosome, StartPosition), CosmicVcfRow]) {
  
  def rowOpt(chromosome: Chromosome, start: StartPosition): Option[CosmicVcfRow] =
    data.get((chromosome, start))
  
}

// ---------------------------------------------------------------------------
object CosmicLookup {
  
  def apply(cosmicVcfFilePath: String): CosmicLookup =
    CosmicLookup(
      CosmicVcfRows(cosmicVcfFilePath)
        .indexBy(cosmicVcfRow =>
          (cosmicVcfRow.CHROM, cosmicVcfRow.POS)))
          
}

// ===========================================================================
case class CosmicVcfRow(
    CHROM:  Chromosome,
    POS:    StartPosition,
    ID:     String,
    REF:    String,
    ALT:    String,
    QUAL:   String,
    FILTER: String,
    INFO:   CosmicVcfRowInfo)

// ---------------------------------------------------------------------------
case class CosmicVcfRowInfo(
    GENE:   String, // eg OR4F5
    STRAND: String, // eg +
    CDS:    String, // eg c.255C>A
    AA:     String, // eg p.I85I
    CNT:    String) // eg 1     

// ---------------------------------------------------------------------------    
object CosmicVcfRows {
  
  // TODO: use a proper VCF parser instead or at least add sanity checks
  def apply(cosmicVcfFilePath: String): Seq[CosmicVcfRow] =
    cosmicVcfFilePath
      .path
      .readTsvFile()
      .dropWhile(_.head.startsWith("#"))
      .map { row =>
        val it1 = row.iterator // eg "1       69345   COSM911918      C       A       .       .       GENE=OR4F5;STRAND=+;CDS=c.255C>A;AA=p.I85I;CNT=1"
        
        CosmicVcfRow(
          CHROM  = Chromosome(it1.next()),
          POS    = StartPosition(it1.next().toInt),
          ID     = it1.next(),
          REF    = it1.next(),
          ALT    = it1.next(),
          QUAL   = it1.next(),
          FILTER = it1.next(),
          INFO   = {
            val it2 = it1.next().split(";").iterator // eg "GENE=OR4F5;STRAND=+;CDS=c.255C>A;AA=p.I85I;CNT=1"
            
            CosmicVcfRowInfo(
              GENE   = it2.next().split("=", 2).last,
              STRAND = it2.next().split("=", 2).last,
              CDS    = it2.next().split("=", 2).last,
              AA     = it2.next().split("=", 2).last,
              CNT    = it2.next().split("=", 2).last)              
          })
      }

}

// ===========================================================================  
