# PedCBImport

transforms pipeline output files for import in pedcbio

the best place to start is looking at [`End2EndTest.scala`](https://github.com/d3b-center/d3b-pedcbimport/blob/223b76d/src/test/scala/test/End2EndTest.scala) (which makes use of [`PedCBImport.scala`](https://github.com/d3b-center/d3b-pedcbimport/blob/223b76d/src/main/scala/pedcbimport/PedCBImport.scala)). it takes the dummy input in _end2end/input_ and checks that the output matches _end2end/references_ (which is meant to match cbioportal's file format [specification](https://cbioportal.readthedocs.io/en/latest/File-Formats.html))

to run it, issue `sbt run`

the core of the data-processing logic is in:
* mutation: [MutationData.scala](src/main/scala/pedcbimport/pipelines/mutation/MutationData.scala)
* expression: [ExpressionData.scala](src/main/scala/pedcbimport/pipelines/expression/ExpressionData.scala)
* copy number: [CopyNumberPhase1.scala](src/main/scala/pedcbimport/pipelines/copynumber/CopyNumberPhase1.scala) and [CopyNumberPhase2.scala](src/main/scala/pedcbimport/pipelines/copynumber/CopyNumberPhase2.scala)

**see the [d3b-scala repo example](https://github.com/d3b-center/d3b-scala/blob/master/examples/pedcbimport.md) for more specific instructions on how to get setup with this project (and with such projects in general)**
