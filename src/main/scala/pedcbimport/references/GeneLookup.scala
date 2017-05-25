package pedcbimport.references

import utils.Implicits.StringImplicits
import pedcbimport.pipelines.common.Wrapper._

// ===========================================================================
object GeneLookup {

  def apply(genesKvpFilePath: String): Map[HugoGeneSymbol, EntrezGeneId] =
    genesKvpFilePath
      .path
      .readSmallFileKvps()
      .filterNot { case (hugo, entrez) =>
          hugo.trim.isEmpty } // TODO: seem to have some empty HUGO symbol associated?
      .map { case (hugo, entrez) =>        
        (HugoGeneSymbol(hugo), EntrezGeneId(entrez)) }
      .toMap
    
}

// ===========================================================================