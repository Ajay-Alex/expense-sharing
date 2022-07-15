package com.example.repository

import com.example.database.DatabaseManager
import com.example.entities.Audit
import com.example.entities.CreateGroup
import com.example.entities.User
import com.example.entities.UserDraft

class UserRepository {

    private val database = DatabaseManager()

    fun getUser(id:Int):User?{
        return database.getUserById(id)
            ?.let{ User(it.userid,it.name,it.email,it.mobile,it.group) }
    }

    fun createUser(draft: UserDraft): User {
        return database.addUser(draft)
    }


    fun addAudit(audit: Audit):Audit{
        //println("Inside addTxn:$audit")
        val aud=database.getAudit(audit.toId,audit.fromId)
        if(aud == null){
            //println("******AUDIT NULL******")
            database.addAudit(audit)
            return audit
        }
        else{
            aud.amount+=audit.amount
            //println("aud:$aud,audit:$audit")
            database.updateAudit(aud)
        }
        return aud
    }

    fun addTxn(userId:Int,amount:Int):Int{
        val user1=database.getUserById(userId)?:return -1
        if(user1.group==0)return -2
        val userList=database.getUsersByGroup(user1.group)
        val split=amount/userList.size
        for(i in userList){
            if(i.userid!=userId){
                addAudit(Audit(userId,i.userid,split))
            }
        }
        return 1
    }

    fun createGroupById(createGroup: CreateGroup):Int{

        val userList= mutableListOf<User>()
        for(i in createGroup.userIdList){
            val user= database.getUserById(i)
                ?.let{ User(it.userid,it.name,it.email,it.mobile,it.group)} ?: return -1
            if(user.group!=0) return -2
            userList.add(user)
        }
        val groupId=createGroup.userIdList[0]
        for(i in userList){
            database.updateUser(User(i.userid,i.name,i.email,i.mobile,groupId))
        }
        return groupId
    }

}