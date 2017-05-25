










// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! read-only borrowed from nebulis until properly externalized !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!












                                    /**
                                     * Copyright (c) 2017 (TODO)
                                     *
                                     * @author Anthony Cros <cros.anthony@gmail.com>
                                     */
                                    package utils
                                    
                                    import java.time.format.DateTimeFormatter
                                    import java.time.LocalDateTime
                                    import scala.util.matching.Regex
                                    import scala.util.{Try, Failure, Success}
                                    import org.apache.commons.lang3.StringEscapeUtils
                                    
                                    // ===========================================================================
                                    object Implicits {
                                    	
                                      implicit class StringImplicits(str: String) {
                                        
                                        val EmptyString: String = ""
                                        val UnquoteRegex = "\"(.*)\"".r      
                                      	val TimestampFormat    = new java.text.SimpleDateFormat("yyMMddHHmmss")
                                      	val TimestampIsoFormat = new java.text.SimpleDateFormat("yy-MM-dd HH:mm:ss") // TODO: missing 'T'  	
                                        
                                    		def println = { System.out.println(str); str }
                                    		def print   = { System.out.print(str); str }
                                    		
                                        def remove(potentialSubStr: String) =
                                          str.replace(potentialSubStr, "")
                                        
                                        def removePrefix(prefix: Regex) =
                                          str.replaceAll(
                                            prefix.regex.prefixWithCaret,
                                            EmptyString)
                                    
                                        def removeSuffix(suffix: Regex) =
                                          str.replaceAll(
                                            suffix.regex.suffixWithDollarSign,
                                            EmptyString)
                                            
                                        def split2(regex: String) =
                                          str.split(regex, -1).toSeq // TODO: include match clause?
                                               
                                        def splitTabSeparatedPair:   Option[(String, String)] = splitPair("\t")
                                        def splitColonSeparatedPair: Option[(String, String)] = splitPair(":")
                                        def splitEqualSeparatedPair: Option[(String, String)] = splitPair("=")
                                    
                                        // TODO: a by regex and by value
                                        def splitPair(regex: String): Option[(String, String)] = 
                                          str.split(regex, 2).toSeq match {
                                            case Seq(x)    => None
                                            case Seq(x, y) => Some(x, y)
                                          }
                                        
                                        def splitNewLines = str.split("\n", 1)
                                    
                                        // ===========================================================================
                                        // conversions
                                        
                                        def asOption(nullValue: String): Option[String] =
                                          if (str == nullValue) None else Some(str)
                                        def asOptionIfEmpty: Option[String] =
                                          asOption(nullValue = EmptyString)
                                        def asOptionIfNA: Option[String] =
                                          asOption(nullValue = "NA") // commonly used in TSVs
                                        def asOptionIfNULL: Option[String] =
                                          asOption(nullValue = "NULL") // commonly used in SQL databases
                                        
                                        // TODO: use asOptions rather?
                                        def valueUnless(nullValue: String): Option[String] = 
                                          asOption(nullValue)
                                        def valueUnlessEmptyString: Option[String] = 
                                          asOptionIfEmpty  
                                        
                                        def asBooleanFromBit:    Option[Boolean] = asBoolean("1", "0")
                                        def asBooleanFromLetter: Option[Boolean] = asBoolean("T", "F")
                                        def asBoolean(trueValue: String, falseValue: String): Option[Boolean] =
                                          if      (str == trueValue ) Some(true)
                                          else if (str == falseValue) Some(false)
                                          else                        None
                                                
                                        // ===========================================================================
                                        // quoting
                                    
                                        def unquote = str.extractOneGroup(UnquoteRegex)
                                        
                                        def quote = s""""$str""""
                                        def quoteSingle    = s"'$str'"
                                        def quoteBackticks = s"`$str`"
                                        def quotePipes     = s"|$str|" // useful for debugging
                                        
                                        def tripleQuote = s"""\"\"\"$str\"\"\"""" // useful for scala, rdf turtle, ..
                                                
                                        // ===========================================================================
                                        // formatting
                                        def breakLineBefore: String = breakLinesBefore(1)
                                        def breakLinesBefore(i: Int): String = {
                                          require(i >= 0, (i, str))
                                          
                                          ("\n" * i) + str
                                        }
                                    
                                        def breakLineAfter: String = breakLinesAfter(1)
                                        def breakLinesAfter(i: Int): String = {
                                          require(i >= 0, (i, str))
                                          
                                          str + ("\n" * i)
                                        }
                                        
                                        def indent: String = indent(1)
                                        def indent(i: Int): String = {
                                          require(i >= 0, (i, str))
                                          
                                          ("\t" * i) + str
                                        }
                                    
                                        def indentAll: String = indentAll(1)      
                                        def indentAll(i: Int): String = // TODO: inefficient
                                          str
                                            .split("\n")
                                            .map(_.indent(i))
                                            .mkString("\n")
                                    
                                        def sectionOff: String = sectionOff(1)
                                        def sectionOff(i: Int): String =
                                          indent(i).breakLineBefore
                                          
                                        def sectionAllOff: String = sectionAllOff(1)
                                        def sectionAllOff(i: Int): String =  // TODO: inefficient
                                          str
                                            .split("\n")
                                            .map(_.indent(i))
                                            .mkString("\n")
                                            .breakLineBefore
                                    
                                        // ===========================================================================
                                        // regexes
                                        def regex = str.r // more explicit
                                            
                                        def extractOneGroup(r: Regex): Option[String] =
                                          r
                                          	.findFirstMatchIn(str)
                                          	.map(_.group(1))
                                    
                                        // TODO: inefficient
                                        def extractTwoGroups(r: Regex): Option[(String, String)] =
                                          Try(r.findFirstMatchIn(str).map(m => (m.group(1), m.group(2)))).toOption.flatten
                                        def extractThreeGroups(r: Regex): Option[(String, String, String)] =
                                          Try(r.findFirstMatchIn(str).map(m => (m.group(1), m.group(2), m.group(3)))).toOption.flatten     
                                        def extractFourGroups(r: Regex): Option[(String, String, String, String)] =
                                          Try(r.findFirstMatchIn(str).map(m => (m.group(1), m.group(2), m.group(3), m.group(4)))).toOption.flatten
                                        def extractFiveGroups(r: Regex): Option[(String, String, String, String, String)] =
                                          Try(r.findFirstMatchIn(str).map(m => (m.group(1), m.group(2), m.group(3), m.group(4), m.group(5)))).toOption.flatten
                                    
                                        def prefixWithNewLine = s"\n$str"
                                        def prefixWithCaret = s"^$str" // often useful for regexes
                                        
                                        def suffixWithNewLine    = s"$str\n"
                                        def suffixWithDollarSign = s"$str$$" // often useful for regexes
                                        def suffixWithSlash      = s"$str/"  // often useful for paths
                                          
                                        def prefixWith(prefix: String) = s"$prefix$str"
                                        def suffixWith(suffix: String) = s"$str$suffix"
                                        
                                        def surroundWith(boundary: String) = s"$boundary$str$boundary"
                                        def surroundWith(prefix: String, suffix: String) = s"$prefix$str$suffix"
                                        def surroundWithCaretAndDollarSign = s"^$str$$" // often useful for regexes
                                        def surroundWithBackticks = s"`$str`" // often useful code generation
                                        def surroundWithBrackets       = s"($str)"
                                        def surroundWithCurlyBrackets  = s"{$str}"
                                        def surroundWithSquareBrackets = s"[$str]"
                                    
                                        def prefixWithIfNonEmpty(prefix: String) = if (str.nonEmpty) s"$prefix$str" else str
                                        def suffixWithIfNonEmpty(suffix: String) = if (str.nonEmpty) s"$str$suffix" else str
                                        def surroundWithIfNonEmpty(prefix: String, suffix: String) = if (str.nonEmpty) s"$prefix$str$suffix" else str
                                        
                                        def prefixWithIf(b: Boolean, prefix: String) = // TODO: one with pred
                                          if (b) s"$prefix$str" else str
                                        def suffixWithIf(b: Boolean, suffix: String) = // TODO: one with pred
                                          if (b) s"$str$suffix" else str    
                                        def surroundWithIf(b: Boolean, prefix: String, suffix: String) = // TODO: one with pred
                                          if (b) s"$prefix$str$suffix" else str
                                        
                                        // TODO: more canonical name?
                                        def lowerFirstLetterCase = str.head.toLower + str.tail // TODO: check non-empty first
                                          
                                    
                                        // ===========================================================================
                                        // paths
                                        
                                        	    def path = UnixFileSystemPath(str)
                                               // TODO: for transition only
                                               def ls: List[String] = path.ls
                                               //def lsPaths: List[String] = path.lsPaths
                                               def mkdirs: String       = path.mkdirs
                                               def emptyDir: String = path.emptyDir
                                               def fullFilePathFromResources: String = path.fullFilePathFromMainResources
                                               def fullFilePathFromTestResources: String = path.fullFilePathFromTestResources
                                               def readSmallFileFromResources(): String = path.readSmallFileFromResources()
                                               def readSmallFileFromTestResources(): String = path.readSmallFileFromTestResources()
                                               def readSmallFile(): String = path.readFile()
                                               def streamFile(): Iterable[String] = path.streamFile()
                                               def writeSmallFile(content: String): String = path.writeFile(content)
                                               def writeSmallFileLines(lines: Iterator[String]): String = path.writeSmallFileLines(lines)
                                               def writeStreamingLines(lines: Iterable[String]): String = path.writeFileLines(lines)
                                               def writeStreamingTsv(rows: Iterable[Vector[String]]): String = path.writeTsvFile(rows)
                                               def readSmallFileLines(): List[String] = path.readFileLines()
                                               def readSmallFileTsv(): List[Vector[String]] = path.readTsvFile()
                                               def streamFileTsv(): Iterable[Vector[String]] = path.streamTsvFile()
                                               def readSmallFileSep(sep: Regex): List[Vector[String]] = path.readSmallFileSep(sep)
                                               def readSmallFileKvps(): List[(String, String)] = path.readSmallFileKvps()
                                               def readHeader: String  = path.readHeader
                                               def basename: String = path.basename                              
                                    
                                        // ===========================================================================
                                        // time
                                    
                                        // useful for logs
                                        def withTimestamp      = (str, TimestampFormat   .format(new java.util.Date()))
                                        def withTimestampIso   = (str, TimestampIsoFormat.format(new java.util.Date()))
                                        def withTimestampEpoch = (str, System.currentTimeMillis())  
                                         
                                        def parseIsoLocalDateTime: LocalDateTime = // eg 2014-09-02T08:05:23
                                          LocalDateTime.parse(
                                            str,
                                            DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                         
                                        // ===========================================================================
                                        // system
                                    
                                        def systemCall: String =
                                          sys.process.Process(str) !! // TODO: way to return both code and output    
                                        
                                        def isAvailableUnixBinary: Boolean =
                                          scala.util.Try(
                                              sys.process.Process(s"which ${str}").!!)
                                            .getOrElse(EmptyString)
                                            .nonEmpty
                                    
                                        // ===========================================================================
                                        // urls
                                    
                                        def url =
                                          Url(str)                        
                                          
                                        def urlEncodeUTF8: String =
                                          java.net.URLEncoder.encode(str, "UTF-8")        
                                    
                                    
                                        // ===========================================================================
                                        // misc
                                        def base64: String =
                                          java.util.Base64
                                            .getEncoder
                                            .encodeToString(
                                              str.getBytes(
                                                  java.nio.charset.StandardCharsets.UTF_8))
                                    
                                        def isDigits: Boolean =
                                          str.nonEmpty &&
                                          str.forall(_.isDigit)
                                    
                                    
                                      }
                                    
                                      // ===========================================================================  
                                      implicit class OptionImplicits[A](opt: Option[A]) {
                                        def forceGet: A = opt.get
                                        
                                        def force = opt.get
                                        
                                        def getDebug(args: Any*): A = debugGet(args)
                                        def debugGet(args: Any*): A =
                                          if (opt.isDefined) opt.get
                                          else
                                            throw new IllegalStateException(          
                                              args.toVector.toString)
                                        
                                        def asOptionalPair[B](b: B): Option[(B, A)] =
                                          opt.map(a => (b, a)) 
                                          
                                        def sideEffectPassAlong(f: A => Unit) =
                                          opt.map { a => f(a); a }
                                        
                                        def indexBy[B](f: A => B): Option[(B, A)] =
                                          opt.map(a => f(a) -> a)
                                        def index[B](b: B): Option[(B, A)] =
                                          opt.map(a => b -> a)
                                    
                                        def peek: Option[A] = peek("")
                                        def peek(around: String): Option[A] = peek(around, around)
                                        def peek(before: String, after: String): Option[A] = { println(s"${before}${opt}${after}"); opt } // 170523173020; TODO: one with logger
                                    
                                      }
                                      
                                      // ===========================================================================  
                                      implicit class IterableImplicits[A](it: Iterable[A]) {
                                    
                                        def println = { it.foreach(x => System.out.println(x)); it; }
                                          
                                        def print(sep: String) { it.foreach(x => System.out.print(s"${x}${sep}")) }
                                        
                                        def indexByIdentity: Iterable[(A, A)] =
                                          it.map { x => (x, x) }
                                        
                                        def sideEffectPassAlong(f: A => Unit) =
                                          it.map { a => f(a); a }
                                        
                                        // TODO: see seq's for improvements
                                        def countBySelf: Map[A, Int] =
                                          it.groupBy(identity).mapValues(_.size)
                                                              
                                                              
                                                              
                                        def peek: Iterable[A] = peek("")
                                        def peek(around: String): Iterable[A] = peek(around, around)
                                        def peek(before: String, after: String): Iterable[A] = { it.map { x => System.out.println(s"${before}${x}${after}"); x } } // 170523173020; TODO: one with logger                          
                                    
                                        
                                                              
                                                              
                                                              
                                                              
                                    
                                        // TODO: write
                                                     
                                        // TODO; indexBy...
                                        
                                        // TODO: as plural?
                                        def mkStringNewLine: String = 
                                          it.mkString("\n")
                                        def mkStringTab: String = 
                                          it.mkString("\t")
                                        
                                        def dropHeader: Iterable[A] =
                                          it.drop(1)
                                    
                                        def dropExpected(a: A): Option[Iterable[A]] =
                                          dropExpected(_ == a)
                                        
                                        def dropExpected(f: A => Boolean): Option[Iterable[A]] = {    
                                          val (head, tail) = it.splitAt(1)
                                      
                                          if (head.size == 1 && head.forall(f)) Some(tail)
                                          else None
                                        }
                                        
                                        
                                          
                                          
                                    
                                        def asOptionIfEmpty: Option[Iterable[A]] =
                                          if (it.isEmpty) None else Some(it)
                                          
                                          
                                          
                                          
                                          
                                      }
                                                
                                      // ---------------------------------------------------------------------------  
                                      implicit class SeqImplicits[A](seq: Seq[A]) {
                                    
                                        // mkStrings    
                                        def mkStringNewLine: String = 
                                          seq.mkString("\n")
                                        def mkStringTab: String = 
                                          seq.mkString("\t")
                                        def mkStringComma: String = 
                                          seq.mkString(",")
                                        def mkStringCommaSpace: String = 
                                          seq.mkString(", ")
                                        def mkStringColon: String = 
                                          seq.mkString(":")
                                    
                                        // ===========================================================================
                                        // size
                                    
                                        def isSizeOne: Boolean =
                                          seq.size == 1      
                                        def isSizeTwo: Boolean =
                                          seq.size == 2
                                        def isSizeThree: Boolean =
                                          seq.size == 3
                                    
                                        def sizeOne: Option[A] =
                                          if (isSizeOne) Some(seq.head) else None      
                                        def sizeTwo: Option[(A, A)] =
                                          if (isSizeTwo) Some(seq.head, seq.last) else None
                                        def sizeThree: Option[(A, A, A)] =
                                          if (isSizeThree) Some(seq.head, seq.tail.head, seq.last) else None
                                        
                                    
                                        // ===========================================================================
                                        def splitAtOne: Option[(A, Seq[A])] = {
                                          seq match {
                                            case Seq() => None
                                            case _ =>
                                          		val (head, tail) = seq.splitAt(1)
                                          		
                                          		Some((head.head, tail))        
                                          }
                                        }
                                        
                                        def splitStringPair(sep: String = "\t")(implicit evidence: A <:< String): Seq[(String, String)] =
                                          seq
                                            .map(_.split("\t", 2))
                                            .map { x => x.head -> x.last }    
                                                        
                                        // ---------------------------------------------------------------------------
                                        def indexByIdentity: Map[A, A] = indexBy(identity)
                                        
                                        // TODO: careful collisions
                                        def indexBy[B](f: A => B): Map[B, A] =
                                          seq.map(x => f(x) -> x).toMap
                                          
                                        def indexWithIdentity: Map[A, A] = indexWith(identity)
                                        def indexWith[B](f: A => B): Map[A, B] = // TODO: trait Map is invariant in type A. You may wish to investigate a wildcard type such as `_ <: Denormalizer2.this.CurrentFieldName`. (SLS 3.2.10)
                                          seq.map(x => x -> f(x)).toMap
                                        
                                        // ---------------------------------------------------------------------------    
                                        // FIXME
                                        def groupByKey[B, C](implicit evidence: A =:= (B, C)): Map[B, Seq[C]] = // for <:< see https://twitter.github.io/scala_school/advanced-types.html#otherbounds
                                          seq
                                              .asInstanceOf[Seq[(B, C)]] // TODO: can we get rid of this?
                                              .groupBy  { case (key, _) => key }
                                              .mapValues{ _.map { case (_, value) => value }}
                                        
                                        //    def mapKeys[K, V, K2](f: K => K2)(implicit evidence: A =:= (K, V)): Seq[(K2, V)] =
                                        //      seq.map { case (k, v) => f(k) -> v }
                                            
                                        //def countBySelf: Seq[(A, Long)] = countBy[A](identity)
                                        def countBy[B](f: A => B): Seq[(B, Long)] =
                                          seq
                                            .map(f)
                                            .groupBy(identity)
                                            .mapValues(_.size.toLong)
                                            .toSeq
                                          
                                        def swapContainers[B](implicit evidence: A <:< Try[B]): Try[Seq[B]] = // for <:< see https://twitter.github.io/scala_school/advanced-types.html#otherbounds
                                          if (!seq.exists(_.isFailure)) Success(seq.map(_.get))
                                          else Failure[Seq[B]](seq.find(_.isFailure).get.failed.get) 
                                    
                                      }
                                      
                                      // ===========================================================================  
                                      implicit class PairsIterableImplicits[K, V](seq: Iterable[(K, V)]) {
                                        def values: Iterable[V] = seq.map(_._2)
                                        
                                        def groupByKey: Map[K, Iterable[V]] =
                                          seq
                                            .groupBy  { case (key, _) => key }
                                            .mapValues{ _.map { case (_, value) => value }}
                                        
                                        def mapKeys[K2](f: K => K2): Iterable[(K2, V)] =
                                          seq.map { case (k, v) => f(k) -> v }
                                    
                                        def mapValues[V2](f: V => V2): Iterable[(K, V2)] =
                                          seq.map { case (k, v) => k -> f(v) }
                                    
                                        def flatMapValues[V2](f: V => Option[V2]): Iterable[(K, V2)] =
                                          seq.flatMap { case (k, v) => f(v).map(v2 => k -> v2) }
                                        
                                        def filterByValues(pred: V => Boolean): Iterable[(K, V)] =
                                          seq.filter { case (_, v) => pred(v)}
                                        def filterByValuesNot(pred: V => Boolean): Iterable[(K, V)] =
                                          seq.filter { case (_, v) => !pred(v)}
                                        
                                        def filterByKeys(pred: K => Boolean): Iterable[(K, V)] =
                                          seq.filter { case (k, _) => pred(k)}
                                        def filterByKeysNot(pred: K => Boolean): Iterable[(K, V)] =
                                          seq.filter { case (k, _) => !pred(k)}    
                                      }
                                      
                                          // ---------------------------------------------------------------------------
                                          // TODO
                                          implicit class PairsSeqImplicits[K, V](seq: Seq[(K, V)]) {
                                        
                                            def values: Seq[V] = seq.map(_._2)
                                            
                                            def groupByKey: Map[K, Seq[V]] =
                                              seq
                                                .groupBy  { case (key, _) => key }
                                                .mapValues{ _.map { case (_, value) => value }}
                                            
                                            def mapKeys[K2](f: K => K2): Seq[(K2, V)] =
                                              seq.map { case (k, v) => f(k) -> v }
                                        
                                            def mapValues[V2](f: V => V2): Seq[(K, V2)] =
                                              seq.map { case (k, v) => k -> f(v) }
                                        
                                            def flatMapValues[V2](f: V => Option[V2]): Seq[(K, V2)] =
                                              seq.flatMap { case (k, v) => f(v).map(v2 => k -> v2) }
                                            
                                            def filterByValues(pred: V => Boolean): Seq[(K, V)] =
                                              seq.filter { case (_, v) => pred(v)}
                                            def filterByValuesNot(pred: V => Boolean): Seq[(K, V)] =
                                              seq.filter { case (_, v) => !pred(v)}
                                            
                                            def filterByKeys(pred: K => Boolean): Seq[(K, V)] =
                                              seq.filter { case (k, _) => pred(k)}
                                            def filterByKeysNot(pred: K => Boolean): Seq[(K, V)] =
                                              seq.filter { case (k, _) => !pred(k)}
                                            
                                          }
                                    
                                      // ===========================================================================  
                                      implicit class MapImplicits[K, V](map: Map[K, V]) {
                                    
                                        /** careful, assumes the modified keys don't collide. */
                                        def flatMapKeys[K2](f: K => Option[K2]): Map[K2, V] =
                                          map.flatMap { case (k, v) => f(k).map(k2 => k2 -> v) }
                                        
                                        /** careful, assumes the modified keys don't collide. */
                                        def mapKeys[K2](f: K => K2): Map[K2, V] =
                                          map.map { case (k, v) => f(k) -> v }
                                    
                                        def assertValues(p: V => Boolean) =
                                          map.mapValues { x => assert(p(x)); x }
                                    
                                        def flatMapValues[V2](f: V => Option[V2]): Map[K, V2] =
                                          map.flatMap { case (k, v) => f(v).map(v2 => k -> v2) }
                                    
                                        // TODO
                                        def flatMapValues2[V2](f: V => Seq[V2]): Map[K, V2] =
                                          map.flatMap { case (k, v) => f(v).map(v2 => k -> v2) }
                                    
                                        /** careful, assumes values don't collide. */
                                        def swapEntry: Map[V, K] =
                                          map.map { case (k, v) => v -> k }
                                      
                                        def filterByValues(pred: V => Boolean): Map[K, V] =
                                          map.filter { case (_, v) => pred(v)}
                                        def filterByValuesNot(pred: V => Boolean): Map[K, V] =
                                          map.filter { case (_, v) => !pred(v)}
                                        
                                        def filterByKeys(pred: K => Boolean): Map[K, V] =
                                          map.filter { case (k, _) => pred(k)}
                                        def filterByKeysNot(pred: K => Boolean): Map[K, V] =
                                          map.filter { case (k, _) => !pred(k)}       
                                        
                                        def debug(k: K): V =
                                          if (map.contains(k)) map(k)
                                          else throw new IllegalStateException(s"$k, ${map.keys.take(10)}")
                                                
                                      }
                                    
                                      
                                      // ===========================================================================  
                                      implicit class IteratorImplicits[A](it: Iterator[A]) {
                                    
                                      	def asClosingIterable(src: io.Source): Iterable[A] = {
                                          new Iterable[A] {                            
                                            def iterator(): Iterator[A] =                              
                                                new Iterator[A] {
                                      
                                            		  def next(): A = 
                                            		    it.next()
                                            		  
                                            		  def hasNext: Boolean = {
                                            		    val more = it.hasNext
                                            		    
                                            		    // can close source when iterable has been consumed entirely; TODO: consider deleting the files too?
                                            		    if (!more) src.close()
                                            		    
                                            		    more
                                            		  }
                                      
                                                }                               
                                          }
                                      	} 
                                      	
                                      }
                                        
                                      // ===========================================================================
                                      implicit class RegexMatchImplicits(m: scala.util.matching.Regex.Match) {
                                        def triplet: (String, String, String) =
                                          (m.group(1), m.group(2), m.group(3))
                                      }
                                    
                                      // ===========================================================================
                                      implicit class Tuple2Implicits[A, B](tup: Tuple2[A, B]) {    
                                        def firstElement:  A = tup._1      
                                        def secondElement: B = tup._2      
                                      }
                                    
                                      // ---------------------------------------------------------------------------
                                      implicit class Tuple3Implicits[A, B, C](tup: Tuple3[A, B, C]) {    
                                        def firstElement:  A = tup._1      
                                        def secondElement: B = tup._2
                                        def thirdElement:  C = tup._3
                                      }
                                    
                                      // ---------------------------------------------------------------------------
                                      implicit class Tuple4Implicits[A, B, C, D](tup: Tuple4[A, B, C, D]) {    
                                        def firstElement:  A = tup._1      
                                        def secondElement: B = tup._2
                                        def thirdElement:  C = tup._3
                                        def fourthElement: D = tup._4
                                      }
                                    
                                      // ---------------------------------------------------------------------------
                                      implicit class Tuple5Implicits[A, B, C, D, E](tup: Tuple5[A, B, C, D, E]) {    
                                        def firstElement:  A = tup._1      
                                        def secondElement: B = tup._2
                                        def thirdElement:  C = tup._3
                                        def fourthElement: D = tup._4
                                        def fifthElement:  E = tup._5
                                      }
                                    
                                      // ===========================================================================  
                                      implicit class MutableIntImplicits(var int: Int) {
                                        
                                        // @nonthreadsafe    
                                        def counterDebug: Int = counterDebug(1)
                                        def counterDebug(batchSize: Int = 1): Int = { // TODO: specifying logger
                                          if (int % batchSize == 0)
                                            println(int)
                                          int + 1
                                        }
                                        
                                      }
                                          
                                      // ===========================================================================
                                      implicit class ThrowableImplicits(throwable: Throwable) {
                                    
                                        def stackTraceString: String = { // 161103091210
                                          val sw = new java.io.StringWriter
                                          throwable.printStackTrace(
                                            new java.io.PrintWriter(sw))
                                          val str = sw.toString
                                          sw.close()
                                          
                                          str
                                        }  	
                                            
                                      }
                                      
                                      // ===========================================================================
                                      implicit class ClassImplicits[A](clazz: Class[A]) {
                                        
                                        def fieldNames: Vector[String] =
                                          clazz
                                            .getDeclaredFields
                                            .map(_.getName)
                                            .map(_.replace("$u00", "\\u00")) // TODO 
                                            .map(org.apache.commons.lang3.StringEscapeUtils.unescapeJava)
                                            .toVector
                                      }
                                    
                                      // ---------------------------------------------------------------------------
                                      implicit class ProductImplicits(product: Product) {
                                    
                                        /* see http://stackoverflow.com/questions/1226555/case-class-to-map-in-scala */  
                                        def asMap: Map[String, Any] = // 161212132250
                                          product
                                            .getClass
                                            .getDeclaredFields
                                            .map(_.getName)
                                            .zip(
                                                product
                                                  .productIterator
                                                  .to)
                                            .toMap
                                            .map { case (k, v) =>
                                              k.replace("$u0020" /* TODO */, " ") -> v }
                                        
                                        import scala.collection.immutable.SortedMap
                                        def asSortedMap: SortedMap[String, Any] =
                                          SortedMap(
                                              product
                                                .getClass
                                                .getDeclaredFields
                                                .map(_.getName)
                                                .zip(
                                                    product
                                                      .productIterator
                                                      .to)
                                              : _*)
                                    
                                        /* see http://stackoverflow.com/questions/1226555/case-class-to-map-in-scala */  
                                        @deprecated("see class implicits instead") def orderedDeclaredFields: Vector[String] = // 161212132250
                                          product
                                            .getClass
                                            .getDeclaredFields // ordered
                                            .map(_.getName)
                                            .toVector
                                      }
                                      
                                      // ===========================================================================
                                      implicit class DoubleImplicits(d: Double) {
                                        
                                        def println { System.out.println(d) }
                                        
                                        def formatExplicit: String = f"${d}%f"
                                        def formatUsLocale: String =
                                          java.text.NumberFormat
                                            .getNumberInstance(java.util.Locale.US)
                                            .format(d)  
                                      }
                                      
                                      // ---------------------------------------------------------------------------
                                      implicit class IntImplicits(i: Int) {
                                        
                                        def println { System.out.println(i) }
                                        
                                        def formatExplicit: String = f"${i}%0f" // TODO: try
                                        def formatUsLocale: String =
                                          java.text.NumberFormat
                                            .getNumberInstance(java.util.Locale.US)
                                            .format(i)
                                      }
                                      
                                      // ===========================================================================
                                      implicit class AnythingImplicits[A](a: A /* TODO: or use Any? */) {
                                        def println = { System.out.println(a); a }
                                    
                                        // thrush combinator (see https://users.scala-lang.org/t/implicit-class-for-any-and-or-generic-type/501)
                                        def then[B](f: A => B) = f(a)   
                                      }
                                    
                                    }
                                    
                                    // =========================================================================== 
                                    
                                    import scala.util.matching.Regex
                                    import scala.collection.JavaConverters._
                                    import java.io.File
                                    import java.io.FileWriter
                                    import java.nio.file.Paths
                                    import java.nio.file.Files
                                    import Implicits.IteratorImplicits
                                    
                                    // ===========================================================================
                                    /** not really meant to be used directly, instead use "/my/path".fs */
                                    case class UnixFileSystemPath(command: String) { // TODO: rename
                                      require(command.nonEmpty)
                                      
                                      private val str = command // TODO: for transition only
                                      private val path = Paths.get(str)
                                      
                                      private val MavenMainResourcesDir: String = java.nio.file.Paths.get("").toAbsolutePath().toString + "/src/main/resources"
                                      private val MavenMainScalaDir: String     = java.nio.file.Paths.get("").toAbsolutePath().toString + "/src/main/scala"
                                      private val MavenTestResourcesDir: String = java.nio.file.Paths.get("").toAbsolutePath().toString + "/src/test/resources"
                                      private val MavenTestScalaDir: String     = java.nio.file.Paths.get("").toAbsolutePath().toString + "/src/test/scala"
                                        
                                      def parent: String =
                                        str
                                          .split("/", -1)
                                          .init
                                          .mkString("/")
                                      
                                      def filename: String =
                                        str
                                          .split("/", -1)
                                          .last
                                    
                                      def basename: String =
                                        filename
                                          .split("\\.")
                                          .head
                                    
                                      // ---------------------------------------------------------------------------  
                                      def ls: List[String] = listNames
                                      def listNames: List[String] =
                                        list
                                          .map(_.getFileName.toString) // TODO
                                          .toList
                                          
                                      def listPaths: List[String] =
                                        list
                                          .map(p => s"${p.getParent}/${p.getFileName}")
                                          .toList
                                    
                                      def listPathsRecursively: List[String] =
                                        walk
                                          .map(p => s"${p.getParent}/${p.getFileName}")
                                          .toList
                                    
                                      def listFilesRecursively: List[String] =    
                                        walk
                                          .filter(p => Files.isRegularFile(p))
                                          .map(p => s"${p.getParent}/${p.getFileName}")
                                          .toList
                                    
                                      def listDirsRecursively: List[String] =    
                                        walk
                                          .filter(p => Files.isDirectory(p))
                                          .map(p => s"${p.getParent}/${p.getFileName}")
                                          .toList
                                    
                                      private def list =
                                        Files
                                          .list(path)
                                          .iterator() // https://stackoverflow.com/questions/38339440/how-to-convert-a-java-stream-to-a-scala-stream
                                          .asScala
                                          
                                      private def walk =
                                        Files
                                          .walk(path)
                                          .iterator() // https://stackoverflow.com/questions/38339440/how-to-convert-a-java-stream-to-a-scala-stream
                                          .asScala
                                          
                                      // ---------------------------------------------------------------------------
                                      def mkdirs: String = {      
                                        new File(str).mkdirs()
                                        
                                        str
                                      }
                                      
                                      /** use carefully... */
                                      def emptyDir: String = { // non-recursive
                                        val dir = new File(str)
                                        require(dir.isDirectory, str)
                                        require(dir.listFiles().forall(_.isFile), str) // ok for softlinks too (TBC)
                                        
                                        dir.listFiles().foreach(_.delete())
                                        
                                        str
                                      }
                                    
                                      def fullPathFromMainScala: String =
                                        s"${MavenMainScalaDir}/$str"
                                      def fullPathFromTestScala: String =
                                        s"${MavenTestScalaDir}/$str"
                                      def fullPathFromMainResources: String =
                                        s"${MavenMainResourcesDir}/$str"    
                                      def fullPathFromTestResources: String =
                                        s"${MavenTestResourcesDir}/$str"
                                    
                                        // to deprecate:
                                        def fullFilePathFromMainScala: String =
                                          s"${MavenMainScalaDir}/$str"
                                        def fullFilePathFromTestScala: String =
                                          s"${MavenTestScalaDir}/$str"
                                        def fullFilePathFromMainResources: String =
                                          s"${MavenMainResourcesDir}/$str"    
                                        def fullFilePathFromTestResources: String =
                                          s"${MavenTestResourcesDir}/$str"
                                    
                                      // TODO: handle from eclipse vs not
                                      /** str acts as relative path */    
                                      def readSmallFileFromResources(): String =
                                        UnixFileSystemPath(fullFilePathFromMainResources).readFile()
                                    
                                      def readSmallFileFromTestResources(): String =
                                        UnixFileSystemPath(fullFilePathFromTestResources).readFile
                                    
                                      /** str acts as path */
                                      def readFile(): String = {
                                        val src = io.Source.fromFile(str)
                                        val content = src.mkString
                                        src.close
                                        
                                        content
                                      }
                                    
                                      /** str acts as path */      
                                      def streamFile(): Iterable[String] = {
                                        val src = io.Source.fromFile(str)
                                        
                                        src
                                          .getLines()
                                          .asClosingIterable(src)
                                      }
                                    
                                      /** str acts as path */
                                      def writeFile(content: String): String = {
                                        val fw = new FileWriter(str)
                                          fw.write(content)
                                          fw.close()
                                        
                                        str
                                      }
                                    
                                      /** str acts as path */
                                    	def writeFileLines(lines: Iterator[String]): String =
                                    	  writeSmallFileLines(lines)
                                      def writeSmallFileLines(lines: Iterator[String]): String = {
                                        val fw = new FileWriter(str)
                                          lines
                                            .map(x => s"${x}\n")
                                            .foreach(fw.write) // TODO: flush often?
                                          fw.close()
                                        
                                        str
                                      }
                                      
                                      /** str acts as path */
                                      def writeFileLines(lines: Iterable[String]): String = {
                                        val fw = new FileWriter(str)
                                          lines
                                            .map(x => s"${x}\n")
                                            .foreach(fw.write) // TODO: flush often?
                                          fw.close()
                                        
                                        str
                                      }
                                      
                                      def writeTsvFile(rows: Iterable[Vector[String]]): String =
                                        writeFileLines(rows.map(_.mkString("\t")))
                                    
                                      def readFileLines(): List[String] =
                                        readFile().split("\n").toList    
                                      
                                      def readTsvFile(): List[Vector[String]] =
                                        readFileLines().map(_.split("\t", -1).toVector)    
                                    
                                      def streamTsvFile(): Iterable[Vector[String]] =
                                        streamFile().map(_.split("\t", -1).toVector)    
                                    
                                      def readSmallFileSep(sep: Regex): List[Vector[String]] =
                                        readFileLines().map(_.split(sep.regex, -1).toVector)
                                    
                                      def readSmallFileKvps(): List[(String, String)] =
                                        readFileLines()
                                          .filterNot(_.trim.isEmpty) // typically not wanted for KVPs
                                          .map { line =>
                                            val spl = line.split("\t", 2)
                                            (spl.head, spl.last)
                                          }    
                                      
                                      // TODO: offset
                                      def readHeader: String =  {
                                        val src = io.Source.fromFile(str)                          
                                        val header = src.getLines().next() // TODO: if emtpy?
                                        
                                        src.close()
                                        header
                                      }
                                    
                                      def readHeaderFields: Vector[String] =
                                        readHeaderFields("\t".r)
                                    
                                      def readHeaderFields(sep: Regex): Vector[String] =
                                        readHeader
                                          .split(sep.regex, -1)
                                          .toVector  
                                    
                                      def readFirstLines(count: Int): Vector[String] =  {
                                        require(count > 0, count)
                                        
                                        val src = io.Source.fromFile(str)
                                        val it = src.getLines()
                                        val lines =
                                          Range(0, count)
                                            .map { _ => it.next() }
                                            .toVector // TODO: if not enough element
                                        
                                        src.close()
                                        lines
                                      }
                                          
                                    }
                                    
                                    // ===========================================================================
                                    
                                    import java.net.URL
                                    import Implicits.IterableImplicits
                                    import Implicits.IteratorImplicits
                                    
                                    // ===========================================================================
                                    case class Url(underlying: String) {
                                      
                                      private val url = new URL(underlying)
                                        
                                      // TODO: codec
                                      def read(): String = {
                                        val src = io.Source.fromURL(underlying)
                                        
                                        val content = src.mkString
                                        src.close()
                                          
                                        content
                                      }
                                    
                                      // TODO: codec  
                                      def readLines(): Iterable[String] = {
                                        val src = io.Source.fromURL(underlying)
                                        
                                        val it = src.getLines()
                                        src.close()
                                            
                                        it.asClosingIterable(src)
                                      }
                                      
                                    }
                                    
                                    // ===========================================================================

                                    case class Tsv(rows: Iterable[Vector[String]]) {
                                      
                                      def formatRows: Iterable[String] =
                                        rows.map(_.mkString("\t"))
                                      
                                      def format: String =
                                        formatRows.mkString("\n") + "\n"
                                    }
                                    
                                    // ===========================================================================

                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! read-only borrowed from nebulis until properly externalized !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
