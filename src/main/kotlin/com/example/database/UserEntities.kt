package com.example.database

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserTable: Table<DBUserEntity>("user"){
    val userid = int("userid").primaryKey().bindTo{it.userid }
    val name= varchar("name").bindTo { it.name }
    val email=varchar("email").bindTo { it.email }
    val mobile=varchar("mobile").bindTo { it.mobile }
}

interface DBUserEntity: Entity<DBUserEntity>{
    companion object: Entity.Factory<DBUserEntity>()
    val userid:Int
    val name:String
    val email:String
    val mobile:String
}