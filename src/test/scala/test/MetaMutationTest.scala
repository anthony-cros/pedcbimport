package test

import org.junit.Test
import junit.framework.Assert
import pedcbimport.meta.MetaMutation
import pedcbimport.meta.MetaMutation.profile_description
import pedcbimport.pipelines.common.Wrapper.StudyId

// ===========================================================================
/** example of a more granular test */
class MetaMutationTest {

	@Test def test =	
	  Assert.assertEquals(

	    /* expected */
  			 """|data_filename: data_mutations.txt
            |profile_description: Mutation data from whole genome sequencing.
            |profile_name: Mutations
            |cancer_study_identifier: my_study
            |genetic_alteration_type: MUTATION_EXTENDED
            |datatype: MAF
            |show_profile_in_analysis_tab: true
            |stable_id: mutations
  				  |""".stripMargin,

	    /* actual */
    		MetaMutation.toMetaContent(
    		  StudyId("my_study"),
          profile_description.`Mutation data from whole genome sequencing.`))

}

// ===========================================================================