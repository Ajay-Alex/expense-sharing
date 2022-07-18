package com.example.database

import com.example.entities.Audit
import com.example.entities.GroupEntity
import com.example.entities.User
import com.example.entities.UserDraft
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

class DatabaseManager {
    //config
    private val hostname="localhost"
    private val databaseName= "expense_sharing"
    private val username= "root"
    private val password= "password"

    private val ktormDatabase: Database

    init{
        val jdbcUrl = "jdbc:mysql://$hostname:3306/$databaseName?user=$username&password=$password&useSSL=false"
        ktormDatabase=Database.connect(jdbcUrl)
    }

    fun getAllUsers(): List<DBUserEntity> {
        return ktormDatabase.sequenceOf(UserTable).toList()
    }
    fun getUserById(id:Int):DBUserEntity?{
        return ktormDatabase.sequenceOf(UserTable)
            .firstOrNull{it.userid eq id}
    }

    fun getUserByName(name:String):DBUserEntity?{
        return ktormDatabase.sequenceOf(UserTable)
            .firstOrNull{it.name eq name}
    }

    fun getUsersByGroup(group:Int):List<User>{
        val userList= mutableListOf<User>()
        for(row in ktormDatabase.from(UserTable).select()){
            if(row[UserTable.group]==group) {
                userList.add(
                    User(
                        row[UserTable.userid]!!,
                        row[UserTable.name]!!,
                        row[UserTable.email]!!,
                        row[UserTable.mobile]!!,
                        group
                    )
                )
            }
        }
        return userList
    }
    fun getAudit(to:String , from:String): Audit?{
        //println("inside getaudit")
         val query = ktormDatabase.from(AuditTable).select()
             .where { (AuditTable.toName eq to) and (AuditTable.fromName eq from) }
        var audit:Audit? =null
        for(row in query){
            audit = Audit(
                row[AuditTable.toName]!!,
                row[AuditTable.fromName]!!,
                row[AuditTable.amount]!!
            )
            break
        }
        //println("inside getaudit after query: $audit")

        return audit
    }
    fun getGroupByName(name:String):GroupEntity?{
        return ktormDatabase.sequenceOf(GroupTable)
            .firstOrNull{it.grpName eq name}
            ?.let{ GroupEntity(it.grpId,it.grpName)}?: null
    }

    fun addUser(draft: UserDraft):User?{
        val group=getGroupByName(draft.group)?:return null
        val id= ktormDatabase.insertAndGenerateKey(UserTable){
            set(it.name,draft.name)
            set(it.email,draft.email)
            set(it.mobile ,draft.mobile)
            set(it.group,group.grpId)
        } as Int

        return User(id,draft.name,draft.email,draft.mobile,group.grpId)
    }
     fun addAudit(audit:Audit){
         println("inside add audit: $audit")
         ktormDatabase.insert(AuditTable){
             set(it.toName,audit.toName)
             set(it.fromName,audit.fromName)
             set(it.amount,audit.amount)
         }
     }

    fun addGroup(grpName:String): GroupEntity{

        val id= ktormDatabase.insertAndGenerateKey(GroupTable){
            set(it.grpName,grpName)
        } as Int

        return GroupEntity(id,grpName)
    }

    /*
    fun updateUser(user: User):Boolean{
        val updatedRows= ktormDatabase.update(UserTable){
            set(it.name,user.name)
            set(it.email,user.email)
            set(it.mobile ,user.mobile)
            set(it.group,user.group)
            where{
                it.userid eq user.userid
            }
        }
        return updatedRows>0
    }
     */
    fun updateAudit(audit: Audit):Boolean{
        println("inside update audit:$audit")
        val updatedRows= ktormDatabase.update(AuditTable){
            set(it.toName,audit.toName)
            set(it.fromName,audit.fromName)
            set(it.amount,audit.amount)
            where{
                (AuditTable.toName eq audit.toName) and (AuditTable.fromName eq audit.fromName)
            }
        }
        return updatedRows>0
    }



    fun removeUser(id: Int): Boolean{
        val deletedRows= ktormDatabase.delete(UserTable){it.userid eq id}
        return deletedRows>0
    }

}