# PedCBImport

transforms pipeline output files for import in pedcbio

the best place to start is looking at `End2EndTest.scala`. it takes the dummy input in _end2end/input_ and checks that the output matches _end2end/references_ (which is meant to match cbioportal's file format [specification](https://cbioportal.readthedocs.io/en/latest/File-Formats.html))

to run it, issue `sbt run`

