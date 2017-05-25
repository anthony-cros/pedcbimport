package utils

import enumeratum.{Enum, EnumEntry, PlayJsonEnum}

// ===========================================================================
/* generic, not necessarily just for copy number mutations */
sealed trait Overlap
    extends EnumEntry
  object Overlap
      extends Enum[Overlap]
         with PlayJsonEnum[Overlap] {
    val values = findValues
        
    // TODO; better terminology? see https://english.stackexchange.com/questions/31986/whats-the-opposite-of-precede/31988#31988
    // TODO: break it down into more cases? create mini-ontology?
    case object `StrictlyPrecedes`     extends Overlap // contiguous would be a special case
    case object `PartiallyPrecedes`    extends Overlap
    case object `StrictlyContains`     extends Overlap
    case object `StrictlyMatches`      extends Overlap
    case object `StrictlyContainedBy`  extends Overlap
    case object `PartiallySucceeds`    extends Overlap
    case object `StrictlySucceeds`     extends Overlap // contiguous would be a special case

    // ---------------------------------------------------------------------------
    def apply(segment1: (Long, Long), segment2: (Long, Long)): Overlap =
      apply(segment1._1, segment1._2, segment2._1, segment2._2)

    // TODO: overlap size computation too
    def apply(start1: Long, end1: Long, start2: Long, end2: Long): Overlap =
       /* careful, order matters greatly */
       if                          (end1 <  start2) StrictlyPrecedes
       else if (start1 >  end2  )                   StrictlySucceeds
       else if (start1 == start2 && end1 == end2  ) StrictlyMatches
       else if (start1 <= start2 && end1 >= end2  ) StrictlyContains
       else if (start1 >= start2 && end1 <= end2  ) StrictlyContainedBy
       else if (start1 <  start2 && end1 >= start2) PartiallyPrecedes
       else if (start1 <= end2   && end1 >  end2  ) PartiallySucceeds
       else throw new IllegalStateException((start1, end1, start2, end2).toString) // can't happen               
    
  }

//===========================================================================