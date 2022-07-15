package com.example.database

import com.example.database.UserTable.bindTo
import com.example.database.UserTable.primaryKey
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface DBAuditEntity: Entity<DBAuditEntity>{
    companion object: Entity.Factory<DBUserEntity>()
    val toId:Int
    val fromId:Int
    var amount:Int
}

object AuditTable: Table<DBAuditEntity>("audit"){
    val toId = int("toid").bindTo{it.toId }
    val fromId= int("fromid").bindTo { it.fromId }
    val amount=int("amount").bindTo { it.amount }
}