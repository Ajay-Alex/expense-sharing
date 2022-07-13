package com.example.plugins

import com.example.entities.UserDraft
import com.example.repository.UserRepository
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        val repository:UserRepository= UserRepository()
        get("/") {
            call.respondText("Hello! Welcome to Expense Sharing App")
        }

        post("/users"){
            val userDraft= call.receive<UserDraft>()
            val todo = repository.createUser(userDraft)
            call.respond(todo)
        }

    }
}
