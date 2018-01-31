# pyterpreter

[![Build Status](https://travis-ci.com/melkamar/pyterpreter.svg?token=vMAJz6sAMcPRgk9vRaTy&branch=no-truffle)](https://travis-ci.com/melkamar/pyterpreter)

Python interpreter built on Truffle and Graal VM

## Installation
#### Requirements
- JVM (tested on 8)
    - It will work on regular HotspotVM
    - For better performance run on GraalVM (Linux/MAC)
        - [Download labsjdk on bottom of the page](http://www.oracle.com/technetwork/oracle-labs/program-languages/downloads/index.html)
        - Set `JAVA_HOME` to point to extracted LabsJDK archive and `PATH` to `JAVA_HOME/bin` 
- Gradle will be used or automatically downloaded when running `gradlew` (see below) 

#### Steps 
```
$ git clone git@github.com:melkamar/pyterpreter.git
$ cd pyterpreter
$ ./gradlew clean test jar
$ java -jar build/libs/pyterpreter*all*.jar [args]
```

## Usage

- No arguments - start REPL
- Single argument - path to file - execute that file
- `-r file/in/resources` - run file from inside pyterpreter jar. See [resources folder](src/main/resources)
    - e.g. `java -jar build/libs/pyt*all* -r benchmark/fibonacci.py` will start fibonacci benchmark.
    
