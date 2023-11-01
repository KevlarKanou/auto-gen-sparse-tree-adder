# Automatically generated template for a radix-4 sparse tree adder

Tested bit width: 4bits, 8bits, 16bits, 32bits, 64bits, 128bits, 256bits

## Modify bit width

`src/main/scala/Adder/Param.scala`

## Generate verilog file

```
sbt "runMain Adder.Gen"
```

## Verify with z3 prover

```
sbt test
```