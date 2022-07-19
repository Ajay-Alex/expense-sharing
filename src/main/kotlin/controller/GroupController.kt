package controller

import repository.ExpenseSharingRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class GroupController(var repository: ExpenseSharingRepository) {

    suspend fun addGroup(call:ApplicationCall):Boolean{
        val grpName=call.parameters["grpName"]
        if(grpName==null){
            call.respond(
                HttpStatusCode.BadRequest,
                "Group name cannot be null"
            )
            return false
        }
        val groupId= repository.createGroupByName(grpName)
        if(groupId==0){
            call.respond(
                HttpStatusCode.BadRequest,
                "Group already exist"
            )
            return false
        }else{
            call.respond(
                HttpStatusCode.OK,
                "group created with groupId:$groupId"
            )
            return true
        }
    }
}