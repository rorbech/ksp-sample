# KSP Sample

This is a very tiny KSP sample. It consists of a processor that collects and
generates Realm model object descriptors.

- `processor` - Holds the actual collector
- `consumer` - Holds a tiny consuming project

The `Processor` can be debugged through the IDE from `processor/src/test/kotlin/org/example/kspsample/processor/test/ProcessorTests.kt` and can be tested on the _consumer_ project with 
```
./gradlew :consumer:run
```
