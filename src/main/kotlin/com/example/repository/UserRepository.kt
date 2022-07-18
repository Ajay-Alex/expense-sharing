package com.example.repository

import com.example.database.DatabaseManager
import com.example.entities.Audit
import com.example.entities.User
import com.example.entities.UserDraft

class UserRepository {

    private val database = DatabaseManager()

    fun getUser(id:Int):User?{
        return database.getUserById(id)
            ?.let{ User(it.userid,it.name,it.email,it.mobile,it.group) }
    }

    fun createUser(draft: UserDraft): User? {
        return database.addUser(draft)
    }


    fun addAudit(audit: Audit):Audit{
        val aud=database.getAudit(audit.toName,audit.fromName)
        if(aud == null){
            database.addAudit(audit)
            return audit
        }
        else{
            aud.amount+=audit.amount
            database.updateAudit(aud)
        }
        return aud
    }

    fun addTxn(user:String,amount:Int):Int{
        val user1=database.getUserByName(user)?:return -1
        val userList=database.getUsersByGroup(user1.group)
        val split:Float= amount.toFloat()/(userList.size).toFloat()
        for(i in userList){
            if(i.name!=user){
                addAudit(Audit(user,i.name,split))
            }
        }
        return 1
    }

    /*
    fun createGroupById(createGroup: CreateGroup):Int{

        val userList= mutableListOf<User>()
        for(i in createGroup.userIdList){
            val user= database.getUserById(i)
                ?.let{ User(it.userid,it.name,it.email,it.mobile,it.group)} ?: return -1
            if(user.group!=0) return -2
            userList.add(user)
        }
        val group=database.addGroup(createGroup.grpName)
        for(i in userList){
            database.updateUser(User(i.userid,i.name,i.email,i.mobile,group.grpId))
        }
        return group.grpId
    }
    */

    fun createGroupByName(grpName:String):Int{
        val group=database.getGroupByName(grpName)
        return if(group!=null) 0
        else database.addGroup(grpName).grpId
    }

}