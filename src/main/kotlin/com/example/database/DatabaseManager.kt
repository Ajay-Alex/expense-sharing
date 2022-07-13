package com.example.database

import com.example.entities.User
import com.example.entities.UserDraft
import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
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

    fun getUserByMobile(mobile:String):DBUserEntity?{
        return ktormDatabase.sequenceOf(UserTable)
            .firstOrNull{it.mobile eq mobile}
    }

    fun addUser(draft: UserDraft):User{
        val id= ktormDatabase.insertAndGenerateKey(UserTable){
            set(it.name,draft.name)
            set(it.email,draft.email)
            set(it.mobile ,draft.mobile)
        } as Int

        return User(id,draft.name,draft.email,draft.mobile)
    }

    fun updateUser(id:Int,draft: UserDraft):Boolean{
        val updatedRows= ktormDatabase.update(UserTable){
            set(it.name,draft.name)
            set(it.email,draft.email)
            set(it.mobile ,draft.mobile)
            where{
                it.userid eq id
            }
        }
        return updatedRows>0
    }

    fun removeUser(id: Int): Boolean{
        val deletedRows= ktormDatabase.delete(UserTable){it.userid eq id}
        return deletedRows>0
    }

}