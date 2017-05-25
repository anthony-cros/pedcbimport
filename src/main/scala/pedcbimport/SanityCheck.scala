package pedcbimport

import pedcbimport.pipelines.common.CommonUtils
import pedcbimport.pipelines.mutation.RawSampleMafRow
import utils.Implicits.StringImplicits
import utils.Implicits.ClassImplicits
import enumeratum.{Enum, EnumEntry, PlayJsonEnum}
import pedcbimport.pipelines.expression.RawExpression
import pedcbimport.pipelines.copynumber.RawCopyNumber
import pedcbimport.pipelines.mutation.MutationConstants
import utils.Implicits.IterableImplicits

// ===========================================================================
/** quick sanity check, this is *not* meant to act as a proper validation system (hence the coarseness of the error messages) */
object SanityCheck {

  val ExpectedSchema: Map[DataType, Vector[String]] =
    Map(
      DataType.Mutation   -> classOf[RawSampleMafRow].fieldNames,
      DataType.CopyNumber -> classOf[RawCopyNumber]  .fieldNames,
      DataType.Expression -> classOf[RawExpression]  .fieldNames)

  val ExpectedFirstRows: Map[DataType, Vector[Vector[String]]] =
    Map(
      DataType.Mutation   ->
                              Vector(
                                Vector(MutationConstants.FirstLine),
                                ExpectedSchema(DataType.Mutation)),
      DataType.CopyNumber -> Vector(),
      DataType.Expression -> Vector(ExpectedSchema(DataType.Expression)))

  val AllowedCharacters = 
    ('a'.to('z') ++
     'A'.to('Z') ++
     '0'.to('9') ++
      Seq( // hand-picked list of extra allowed characters (loosening must be justified)
        '#',
        ' ',
        '\'',
        '(',
        ')',
        '*',
        '+',
        ',',
        '-',
        '.',
        '/',
        ':',
        ';',
        '=',
        '>',
        '?',
        '_' ))
     .toSet
     
   def validFilePaths(samplesDirPath: String): List[String] =
        (CommonUtils.mutationSampleFilePaths(samplesDirPath) ++  
         CommonUtils.copyNumberSampleFilePaths(samplesDirPath) ++
         CommonUtils.expressionSampleFilePaths(samplesDirPath))
          .sorted
   
  // ===========================================================================
  def apply(samplesDirPath: String): Option[SanityCheckValidationError] = {

    // check dir content
    val actualFilePaths: List[String] =
      samplesDirPath
        .path
        .listPaths
        .sorted

    if (actualFilePaths != validFilePaths(samplesDirPath))
      Some(SanityCheckValidationError.UnexpectedFilesFound(
        offending = actualFilePaths.diff(validFilePaths(samplesDirPath))))

    else
                mutationData(samplesDirPath).flatMap(checkFiles(DataType.Mutation))
      .orElse(copyNumberData(samplesDirPath).flatMap(checkFiles(DataType.CopyNumber)))
      .orElse(expressionData(samplesDirPath).flatMap(checkFiles(DataType.Expression)))      
  }

  // ---------------------------------------------------------------------------
  def mutationData(samplesDirPath: String): Option[Iterable[String]] =
    CommonUtils.mutationSampleFilePaths(samplesDirPath).asOptionIfEmpty

  def copyNumberData(samplesDirPath: String): Option[Iterable[String]] =
    CommonUtils.copyNumberSampleFilePaths(samplesDirPath).asOptionIfEmpty
  
  def expressionData(samplesDirPath: String): Option[Iterable[String]] =
    CommonUtils.expressionSampleFilePaths(samplesDirPath).asOptionIfEmpty
   

  // ===========================================================================
  def checkFiles(dataType: DataType)(sampleFilePaths: Iterable[String]): Option[SanityCheckValidationError] =
    sampleFilePaths
      .map(checkFile(dataType))
      .find(_.nonEmpty)
      .flatten  
  
  // ---------------------------------------------------------------------------
  def checkFile(dataType: DataType)(sampleFilePath: String): Option[SanityCheckValidationError] = {

    import SanityCheckValidationError._
    
    val firstRows: Vector[Vector[String]] =
      if (dataType == DataType.CopyNumber)
        Vector()
      else
        this.firstRows(
          sampleFilePath,
          count = if (dataType == DataType.Mutation) 2 /* because of the extra "#version 2.4" */ else 1)
    
    if (firstRows != ExpectedFirstRows(dataType))
      Some(UnexpectedHeaderEncountered(
        sampleFilePath,
        expected = ExpectedFirstRows(dataType),
        actual   = firstRows ))

    else if (rowLengths(dataType, sampleFilePath) != Seq(ExpectedSchema(dataType).size))
      Some(InconsistentColumnCountDetected(
        sampleFilePath,
        expected = ExpectedSchema(DataType.Mutation).size,
        actual   = rowLengths(dataType, sampleFilePath)))

    else if (!actualCharacters(dataType, sampleFilePath).forall(AllowedCharacters.contains))
      Some(InvalidCharactersFound(
        sampleFilePath,
        offending = actualCharacters(dataType, sampleFilePath).filterNot(AllowedCharacters.contains) ))

    else
      None
  }
 
  // ===========================================================================
  def firstRows(sampleFilePath: String, count: Int = 1): Vector[Vector[String]] =
    sampleFilePath
      .path
      .readFirstLines(count)
      .map(_.split("\t", -1).toVector)
      
  // ---------------------------------------------------------------------------
  def rowLengths(dataType: DataType, sampleFilePath: String): Seq[Int] =
    sampleFilePath
      .path
      .readTsvFile()
      .drop(ExpectedFirstRows(dataType).size) // the first lines have already been checked for more thoroughly
      .map(_.size)
      .distinct
      .toSeq
      
  // ---------------------------------------------------------------------------
  def actualCharacters(dataType: DataType, sampleFilePath: String): List[Char] =
    sampleFilePath
      .path
      .readTsvFile()
      .drop(ExpectedFirstRows(dataType).size) // the first lines have already been checked for more thoroughly
      .flatMap(
        _.flatMap(
           _.toCharArray())
         .distinct)
      .distinct
      .sorted

}

// ===========================================================================
sealed trait SanityCheckValidationError
  object SanityCheckValidationError {
  
    case class UnexpectedFilesFound(
        offending: List[String])
      extends SanityCheckValidationError

    case class InconsistentColumnCountDetected(
        sampleFilePath: String,
        expected:       Int,
        actual:         Seq[Int])
      extends SanityCheckValidationError

    case class UnexpectedHeaderEncountered(
        sampleFilePath: String,
        expected:       Vector[Vector[String]],
        actual:         Vector[Vector[String]])
      extends SanityCheckValidationError

    case class InvalidCharactersFound(
        sampleFilePath: String,
        offending:      List[Char])
      extends SanityCheckValidationError
    
  }

// ===========================================================================
