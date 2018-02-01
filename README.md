# pyterpreter

[![Build Status](https://travis-ci.com/melkamar/pyterpreter.svg?token=vMAJz6sAMcPRgk9vRaTy&branch=no-truffle)](https://travis-ci.com/melkamar/pyterpreter)

Python interpreter built on Truffle and Graal VM

## Features

#### Types
- int (arbitrary precision)
- str
- bool

#### Operators
- arithmetic (+, -, *, /, %)
- assignment (=, +=, -=, *=, /=, %=)
- comparison (==, !=, >, >=, <, <=)
- logical (not, and, or)

#### Flow control
- if/else (no elif)
- while loop (no for)

#### Functions
- function definition, arbitrary argument number (only positional, no named)
- function nesting, recursion
- no `pass` blank statement
- builtin functions:
    - `x = str(arg)` - convert `arg` to string type
    - `x = int(arg)` - convert `arg` to int type
    - `print(arg)` - print `arg` to console (with newline)
    - `x = input()` - read user input from console, assign to variable
    - `sleep(seconds)` - sleep for given number of seconds
    - `exit()` - exit the program
    - `x = time()` - get millisecond-precision time 
      - Not actual clock time, but good for measuring elapsed time
      - Similar to Python's `time.time()` but Pyterpreter cannot do imports so there we go
      - For compatibility in running pyterpreter scripts in Python, user a wrapper script where you import `time()`, e.g. `from time import time; import pyterpreter_script.py`

#### Examples
- Some scripts that Pyterpreter can handle are in the [benchmark folder](src/main/resources/benchmark)

## Installation
#### Requirements
- JVM (tested on 8)
    - It will work on regular HotspotVM
    - For better performance run on GraalVM (Linux/MAC)
        - [Download labsjdk on bottom of the page](http://www.oracle.com/technetwork/oracle-labs/program-languages/downloads/index.html)
        - Set `JAVA_HOME` to point to extracted LabsJDK archive and `PATH` to `JAVA_HOME/bin` 
- Gradle will be used or automatically downloaded when running `gradlew` (see below) 
- In order to run Pyterpreter on Graal, copy or symlink Graal root directory to `pyterpreter/graalvm` (download on the same URL as above, but use the links on the top of the page)

#### Steps 
```
$ git clone git@github.com:melkamar/pyterpreter.git
$ cd pyterpreter
$ ./gradlew clean test jar
```

## Usage
```
$ ./pyterpreter [args]  # Non-graal
$ ./pyterpreter-graal [args]  # Graal-enabled
```
- No arguments - start REPL
- Single argument - path to file - execute that file
- `-r file/in/resources` - run file from inside pyterpreter jar. See [resources folder](src/main/resources)
    - e.g. `java -jar build/libs/pyt*all* -r benchmark/fibonacci.py` will start fibonacci benchmark.
    
## Numbers

Using benchmark `countdown.py`

#### Before variable lookup improvement
- Hotspot 679/348
- Graal   1986/503