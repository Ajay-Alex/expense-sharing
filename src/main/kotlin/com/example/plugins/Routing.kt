package com.example.plugins

import com.example.entities.Audit
import com.example.entities.CreateGroup
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
        get("/user/{id}"){
            val id=call.parameters["id"]?.toIntOrNull()
            if(id==null){
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Id parameter has to be a number"
                )
                return@get
            }
            val user=repository.getUser(id)
            if(user==null){
                call.respond(
                    HttpStatusCode.NotFound,
                    "ToDo with id:$id Not Found")
            }else{
                call.respond(user)
            }
        }

        post("/users"){
            val userDraft= call.receive<UserDraft>()
            val user = repository.createUser(userDraft)
            call.respond(user)
        }
        post("/addGroup"){
            val createGroup= call.receive<CreateGroup>()
            val groupId= repository.createGroupById(createGroup)
            if(groupId==-2){
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Multiple groups per user not allowed"
                )
                return@post
            }
            if(groupId==-1){
                call.respond(
                    HttpStatusCode.NotFound,
                    "user Not Found"
                )
            }else{
                call.respond(HttpStatusCode.OK, "group created with groupId:$groupId")
            }
        }

        post("/addTxn/{userId}/{amount}"){
            val id=call.parameters["userid"]?.toIntOrNull()
            if(id==null){
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Id parameter has to be a number"
                )
                return@post
            }
            val amount= call.parameters["amount"]?.toIntOrNull()
            if(amount==null){
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Amount parameter has to be a number"
                )
                return@post
            }
            val res = repository.addTxn(id,amount)
            if(res==-2){
                call.respond(
                    HttpStatusCode.BadRequest,
                    "user not in any group"
                )
                return@post
            }
            if(res==-1){
                call.respond(
                    HttpStatusCode.NotFound,
                    "user with id:$id Not Found"
                )
            }else{
                call.respond(HttpStatusCode.OK)
            }
        }

    }
}
