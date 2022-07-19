package database

import database.UserTable.bindTo
import database.UserTable.primaryKey
import org.ktorm.entity.Entity
import org.ktorm.schema.*

interface DBAuditEntity: Entity<DBAuditEntity>{
    companion object: Entity.Factory<DBUserEntity>()
    val toName:String
    val fromName:String
    var amount:Float
}

object AuditTable: Table<DBAuditEntity>("audit"){
    val toName = varchar("to").bindTo{it.toName }
    val fromName= varchar("from").bindTo { it.fromName }
    val amount=float("amount").bindTo { it.amount }
}