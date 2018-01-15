# pyterpreter
Python interpreter built on Truffle and Graal VM

## PARSING

### def func(a)
With a single parameter, structure is:
```
|- stmt
   |  |- TOKEN[type: 1, text: def]
   |  |- TOKEN[type: 35, text: g]
   |  |- parameters
   |  |  |- TOKEN[type: 47, text: (]
   |  |  |- typedargslist
   |  |  |  '- TOKEN[type: 35, text: b]
   |  |  '- TOKEN[type: 48, text: )]
   |  |- TOKEN[type: 50, text: :]
```

With multiple parameters, structure is:
```
|- stmt
   |  |- TOKEN[type: 1, text: def]
   |  |- TOKEN[type: 35, text: f]
   |  |- parameters
   |  |  |- TOKEN[type: 47, text: (]
   |  |  |- typedargslist
   |  |  |  |- tfpdef
   |  |  |  |  '- TOKEN[type: 35, text: a]
   |  |  |  |- TOKEN[type: 49, text: ,]
   |  |  |  |- tfpdef
   |  |  |  |  '- TOKEN[type: 35, text: x]
   |  |  |  |- TOKEN[type: 49, text: ,]
   |  |  |  '- tfpdef
   |  |  |     '- TOKEN[type: 35, text: y]
   |  |  '- TOKEN[type: 48, text: )]
   |  |- TOKEN[type: 50, text: :]
```
    - Actual parameter names are hidden under one more node - tfpdef

