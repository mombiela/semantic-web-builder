cd %~dp0
java -cp bin;lib/*; -Dstxt.path=defs org.swb.Executor processor.properties main

