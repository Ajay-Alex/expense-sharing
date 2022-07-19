package database

import database.AuditTable
import entities.Audit
import org.ktorm.database.Database
import org.ktorm.dsl.*

class AuditDatabaseManager {
    private val hostname="localhost"
    private val databaseName= "expense_sharing"
    private val username= "root"
    private val password= "password"

    private val ktormDatabase: Database

    init{
        val jdbcUrl = "jdbc:mysql://$hostname:3306/$databaseName?user=$username&password=$password&useSSL=false"
        ktormDatabase= Database.connect(jdbcUrl)
    }
    fun getAudit(to:String , from:String): Query{

        val query = ktormDatabase.from(AuditTable).select()
            .where { (AuditTable.toName eq to) and (AuditTable.fromName eq from) }
        return query
    }

    fun addAudit(audit: Audit){
        ktormDatabase.insert(AuditTable){
            set(AuditTable.toName,audit.toName)
            set(AuditTable.fromName,audit.fromName)
            set(AuditTable.amount,audit.amount)
        }
    }

    fun updateAudit(audit: Audit):Boolean{
        val updatedRows= ktormDatabase.update(AuditTable){
            set(AuditTable.toName,audit.toName)
            set(AuditTable.fromName,audit.fromName)
            set(AuditTable.amount,audit.amount)
            where{
                (AuditTable.toName eq audit.toName) and (AuditTable.fromName eq audit.fromName)
            }
        }
        return updatedRows>0
    }
}