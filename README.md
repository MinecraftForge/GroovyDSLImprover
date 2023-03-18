# GroovyDSLImprover 
GroovyDSLImprover is a Groovy ASTT API used to generate Gradle DSLs,
compatible with IDEs and all 3 languages you can write Gradle buildscripts / plugins in (Java, Groovy and Kotlin).

## Available modules:
- `"main"` - The main artifact, containing the GroovyDSLImprover API, in the form of the AST Annotations and transformers.
- `runtime` - The runtime artifact, needs to be added to the runtime classpath so that the properties and methods can be remapped at runtime.
- `base` - The base DSL types. This is a utility artifact that can be used to generate DSLs faster and easier.