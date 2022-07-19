package controller

import entities.UserDraft
import repository.ExpenseSharingRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class UserController(var repository: ExpenseSharingRepository) {

    suspend fun getUserById(call: ApplicationCall):Boolean{
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Id parameter has to be a number"
                )
                return false
            }
            val user = repository.getUser(id)
            if (user == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    "user with id:$id Not Found"
                )
                return false
            } else {
                call.respond(user)
                return true
            }

    }

    suspend fun createUser(call: ApplicationCall):Boolean{
        val userDraft= call.receive<UserDraft>()
        val user = repository.createUser(userDraft)
        if(user==null){
            call.respond(
                HttpStatusCode.NotFound,
                "Group Not Found")
            return false
        }
        else call.respond(user)
        return true
    }

}