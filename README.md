# pyterpreter
Python interpreter built on Truffle and Graal VM

## Installation
#### Requirements
- Java (tested on 8)
- Gradle (tested on 3.0)

#### Steps 
```
$ git clone git@github.com:melkamar/pyterpreter.git
$ cd pyterpreter
$ gradle clean test jar
$ java -jar build/libs/pyterpreter*all.jar
```

## Usage

- No arguments - start REPL
- Single argument - path to file - execute that file