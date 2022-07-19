package controller

import repository.ExpenseSharingRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class AuditController(var repository: ExpenseSharingRepository) {

    suspend fun addTxn(call: ApplicationCall): Boolean {
        val name=call.parameters["userName"]
        if(name==null){
            call.respond(
                HttpStatusCode.BadRequest,
                "Name parameter cannot be null"
            )
            return false
        }
        val amount= call.parameters["amount"]?.toIntOrNull()
        if(amount==null){
            call.respond(
                HttpStatusCode.BadRequest,
                "Amount parameter has to be a number"
            )
            return false
        }
        val res = repository.addTxn(name,amount)

        if(res==-1){
            call.respond(
                HttpStatusCode.NotFound,
                "user with name :$name Not Found"
            )
            return false
        }else{
            call.respond(HttpStatusCode.OK)
            return true
        }
    }
}