package org.example.kspsample.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getKotlinClassByName
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSClassifierReference
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.visitor.KSTopDownVisitor
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

    fun emit(s: String, indent: String = "") {
        output.appendText("$indent$s\n")
    }

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) {
            return emptyList()
        }
        output = codeGenerator.createNewFile(Dependencies(false), "", "SampleOutput", "txt")
        emit("SampleProcessor: init($options)", "")

        val files = resolver.getAllFiles()
        val fileInfo = files.map { "${it.packageName.asString()}, ${it.fileName}, ${it.filePath}" }.joinToString("\n")
        emit("files: $fileInfo", "")

        val realmObject: KSClassDeclaration? = resolver.getKotlinClassByName("io.realm.RealmObject")
        emit("RealmObject: ${realmObject?.packageName?.asString()} ${realmObject?.qualifiedName?.asString()}", "")

        val primaryKeysElements = resolver.getSymbolsWithAnnotation("io.realm.annotations.PrimaryKey")

        val visitor = Visitor()
        val generator = ModelDescriptorGenerator()
        for (file in files) {
            emit("Processing: ${file.filePath}", "")
            file.accept(visitor, "")
            file.accept(generator, Schema(mutableMapOf()))
        }

        invoked = true
        return emptyList()
    }

    inner class Visitor : KSTopDownVisitor<String, Unit>() {
        override fun defaultHandler(node: KSNode, data: String) {
            emit("$node", "")
        }

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: String) {
            super.visitClassDeclaration(classDeclaration, data)
            val isRealmObject = classDeclaration.superTypes.any {
                when (val ref = it.element) {
                    is KSClassifierReference ->
                        ref.referencedName() == "RealmObject"
                            && it.resolve().declaration.qualifiedName?.asString() == "io.realm.RealmObject"
                    else -> false
                }
            }
            emit("RealmObject: ${classDeclaration.qualifiedName?.asString()}")
        }
    }

    data class Schema(val schema: MutableMap<String, MutableMap<String, String>>)

    inner class ModelDescriptorGenerator : KSTopDownVisitor<Schema, Unit>() {
        override fun defaultHandler(node: KSNode, data: Schema) {
        }

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Schema) {
        super.visitClassDeclaration(classDeclaration, data)
            val isRealmObject = classDeclaration.superTypes.any {
                when (val ref = it.element) {
                    is KSClassifierReference ->
                        ref.referencedName() == "RealmObject"
                            && it.resolve().declaration.qualifiedName?.asString() == "io.realm.RealmObject"
                    else -> false
                }
            }
            if (isRealmObject) {
                val packageName = "org.example.kspsample.schema"
                val className = classDeclaration.simpleName.asString() + "Schema"
                val file: OutputStream = codeGenerator.createNewFile(
                     Dependencies(true, classDeclaration.containingFile!!),
                    packageName, className
                )
                file.appendText("package $packageName\n\n")
                file.appendText("class $className {\n")
                file.appendText("\tcompanion object {\n")
                file.appendText("\t\tval schema : Map<String, Map<String, String>> = mapOf(\n")
                data.schema.map { (className, properties) ->
                    file.appendText("\t\t\t\"$className\" to mapOf (\n")
                    properties.map { (k, v) ->
                        file.appendText("\t\t\t\t\"$k\" to \"$v\",\n")
                    }
                    file.appendText("\t\t\t),\n")
                }
                file.appendText("\t\t)\n")
                file.appendText("\t}\n")
                file.appendText("}\n")
                file.close()
            }
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Schema) {
            when (val parent = property.parent) {
                is KSClassDeclaration -> {
                    data.schema.getOrPut(
                        parent.simpleName.asString(),
                        ::mutableMapOf
                    )[property.simpleName.asString()] = property.type.toString()
                }
                else -> Unit
            }
        }
    }
}
