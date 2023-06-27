package com.example.routers

import com.example.models.TodoCreateInput
import com.example.services.TodosService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant

fun Routing.todosRouting() {

    get {
        val result = TodosService.getAll()
        call.respond(result)
    }

    get("{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText(
            "A malformed ID was provided",
            status = HttpStatusCode.BadRequest
        )
        val todo =
            TodosService.get(id) ?: return@get call.respondText("Todo not found", status = HttpStatusCode.NotFound)
        call.respond(todo)
    }

    post {
        val todoCreateInput = call.receive<TodoCreateInput>()
        val result = TodosService.create(todoCreateInput)
        if (result != null)
            call.respond(result)
        else
            call.respondText("Failed to create", status = HttpStatusCode.InternalServerError)
    }

    patch("{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@patch call.respondText(
            "A malformed ID was provided",
            status = HttpStatusCode.BadRequest
        )
    }

    delete("{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText(
            "A malformed ID was provided",
            status = HttpStatusCode.BadRequest
        )
        val result = TodosService.delete(id)
        if (result)
            call.respondText("Deleted", status = HttpStatusCode.OK)
        else
            call.respondText("Failed", status = HttpStatusCode.InternalServerError)
    }

    get("status") {
        val todos = TodosService.getAll()
        val completedNo = todos.count { todo -> todo.completed }
        val total = todos.size
        val incomplete = total - completedNo
        val uptime = Instant.now().toEpochMilli()
        val data =
            hashMapOf("completed" to completedNo, "incomplete" to incomplete, "total" to total, "uptime" to uptime)
        call.respond(data)
    }

    patch("{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@patch call.respondText(
            "A malformed ID was provided",
            status = HttpStatusCode.BadRequest
        )
        val todo =
            TodosService.get(id) ?: return@patch call.respondText("Todo not found", status = HttpStatusCode.NotFound)
        val result = TodosService.toggle(todo)

        if (result)
            call.respondText("Updated", status = HttpStatusCode.OK)
        else
            call.respondText("Failed", status = HttpStatusCode.InternalServerError)
    }
}