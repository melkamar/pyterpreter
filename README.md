# pyterpreter

[![Build Status](https://travis-ci.com/melkamar/pyterpreter.svg?token=vMAJz6sAMcPRgk9vRaTy&branch=no-truffle)](https://travis-ci.com/melkamar/pyterpreter)

Python interpreter built on Truffle and Graal VM


## Disclaimer
This is a pure Java interpreter of Python - it is less capable than what is in the `master` branch 
(which uses Truffle). This branch is discontinued.


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
