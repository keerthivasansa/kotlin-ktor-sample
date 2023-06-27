package com.example.services

import com.example.models.DatabaseFactory.dbQuery
import com.example.models.Todo
import com.example.models.TodoCreateInput
import com.example.models.Todos
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object TodosService {

    private fun fromResultRow(row: ResultRow): Todo {
        return Todo(row[Todos.content], row[Todos.completed], row[Todos.id])
    }

    suspend fun getAll(): List<Todo> {
        val result = dbQuery {
            Todos.selectAll().map(::fromResultRow)
        }
        return result
    }

    suspend fun create(data: TodoCreateInput): Todo? {
        val result = dbQuery {
            Todos.insert {
                it[content] = data.content
                it[completed] = data.completed
            }
        }
        return result.resultedValues?.map(::fromResultRow)?.first()
    }

    suspend fun get(id: Int): Todo? {
        val todo = dbQuery {
            Todos.select(Todos.id eq id)
                .map(::fromResultRow)
                .singleOrNull()
        }
        return todo
    }

    suspend fun delete(id: Int): Boolean {
        val result = dbQuery {
            Todos.deleteWhere { Todos.id eq id }
        }
        return result == 1
    }

    suspend fun toggle(todo: Todo): Boolean {
        return dbQuery {
            Todos.update(where = {
                Todos.id eq todo.id
            }, 1) {
                it[completed] = !todo.completed
            }
        } == 1
    }
}