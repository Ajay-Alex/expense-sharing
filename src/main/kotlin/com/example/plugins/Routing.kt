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
                    "user with id:$id Not Found")
            }else{
                call.respond(user)
            }
        }

        post("/users"){
            val userDraft= call.receive<UserDraft>()
            val user = repository.createUser(userDraft)
            if(user==null){
                call.respond(
                    HttpStatusCode.NotFound,
                    "Group Not Found")
            }
            else call.respond(user)
        }

        post("/addGroup/{grpName}"){
            val grpName=call.parameters["grpName"]
            if(grpName==null){
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Group name cannot be null"
                )
                return@post
            }
            val groupId= repository.createGroupByName(grpName)
            if(groupId==0){
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Group already exist"
                )
            }else{
                call.respond(HttpStatusCode.OK, "group created with groupId:$groupId")
            }
        }

        post("/addTxn/{userName}/{amount}"){
            val name=call.parameters["userName"]
            if(name==null){
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Name parameter has to be a provided"
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
            val res = repository.addTxn(name,amount)

            if(res==-1){
                call.respond(
                    HttpStatusCode.NotFound,
                    "user with name :$name Not Found"
                )
            }else{
                call.respond(HttpStatusCode.OK)
            }
        }

    }
}
