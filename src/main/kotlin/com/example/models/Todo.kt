package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Todo(val content: String, val completed: Boolean, val id: Int)

@Serializable
data class TodoCreateInput(val content: String, val completed: Boolean = false)

object Todos : Table() {
    val id = integer("id").autoIncrement()
    val content = varchar("content", 128)
    val completed = bool("completed")
}
