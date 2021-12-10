package org.example.kspsample.consumer

import org.example.kspsample.schema.ModelObjectSchema

fun main() {
    println("Model objects:")
    ModelObjectSchema.schema.map { (name, properties) ->
        println("\t$name = {")
        properties.map { (propertyName, type) ->
            println("\t\t$propertyName: $type ")
        }
        println("\t}")
    }
}
