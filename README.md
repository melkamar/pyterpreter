# pyterpreter

[![Build Status](https://travis-ci.com/melkamar/pyterpreter.svg?token=vMAJz6sAMcPRgk9vRaTy&branch=no-truffle)](https://travis-ci.com/melkamar/pyterpreter)

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
$ java -jar build/libs/pyterpreter*all*.jar [args]
```

## Usage

- No arguments - start REPL
- Single argument - path to file - execute that file
- `-r file/in/resources` - run file from inside pyterpreter jar. See [resources folder](src/main/resources)
    - e.g. `java -jar build/libs/pyt*all* -r benchmark/fibonacci.py` will start fibonacci benchmark.
## Troubleshooting

- Annotated classes not being generated? Go to settings-annotation processor and uncheck->apply->check->okay and rerun gradle build step