package com.example.database

import com.example.database.AuditTable.bindTo
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface DBGroupEntity: Entity<DBGroupEntity> {
    companion object: Entity.Factory<DBUserEntity>()
    val grpId:Int
    val grpName:String
}

object GroupTable: Table<DBGroupEntity>("group"){
    val grpId = int("grpid").primaryKey().bindTo{it.grpId }
    val grpName= varchar("grpname").bindTo { it.grpName }
}