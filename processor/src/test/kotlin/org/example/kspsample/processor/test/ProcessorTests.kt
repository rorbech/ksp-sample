package org.example.kspsample.processor.test

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.example.kspsample.processor.Provider
import org.junit.Test
import java.io.File


class ProcessorTests {

    @Test
    fun test() {
        val input = SourceFile.kotlin(
            "sample.kt",
            """ 
                import io.realm.RealmObject
                import io.realm.annotation.PrimaryKey
                import io.realm.RealmConfiguration
                    
                class A : RealmObject {                 
                    @PrimaryKey
                    var id: String = "Realm"
                }
            """.trimIndent()
        )
        val compilation = KotlinCompilation().apply {
            workingDir = File("./build/test")
            sources = listOf(input)
            symbolProcessorProviders = listOf(Provider())
            inheritClassPath = true
        }
        val result = compilation.compile()
    }
}
