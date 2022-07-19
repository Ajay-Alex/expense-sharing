package com.example.plugins

import controller.AuditController
import controller.GroupController
import controller.UserController
import entities.UserDraft
import repository.ExpenseSharingRepository
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {

        val repository: ExpenseSharingRepository = ExpenseSharingRepository()
        val userController= UserController(repository)
        val groupController= GroupController(repository)
        val auditController= AuditController(repository)

        get("/") {
            call.respondText("Hello! Welcome to Expense Sharing App")
        }

        get("/user/{id}"){
            userController.getUserById(call)
        }

        post("/users"){
            userController.createUser(call)
        }

        post("/addGroup/{grpName}"){
            groupController.addGroup(call)
        }

        post("/addTxn/{userName}/{amount}"){
            auditController.addTxn(call)
        }

    }
}
