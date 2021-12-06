package org.example.kspsample.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import java.io.OutputStream

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

class Processor(
    val codeGenerator: CodeGenerator,
    val options: Map<String, String>
) : SymbolProcessor {
    private lateinit var output: OutputStream
    private var invoked: Boolean = false

    fun emit(s: String, indent: String) {
        output.appendText("$indent$s\n")
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) {
            return emptyList()
        }
        output = codeGenerator.createNewFile(Dependencies(false), "", "SampleOutput", "txt")
        emit("SampleProcessor: init($options)", "")
        invoked = true
        return emptyList()
    }
}
