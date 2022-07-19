package service.AuditService

import database.AuditDatabaseManager
import database.AuditTable
import entities.Audit

class AuditService {

    private val auditDatabaseManager= AuditDatabaseManager()

    fun getAudit(to: String, from: String): Audit?{
        val query= auditDatabaseManager.getAudit(to,from)
        var audit: Audit? =null
        for(row in query){
            audit = Audit(
                row[AuditTable.toName]!!,
                row[AuditTable.fromName]!!,
                row[AuditTable.amount]!!
            )
            break
        }

        return audit
    }

    fun addAudit(audit: Audit){
        auditDatabaseManager.addAudit(audit)
    }
    fun updateAudit(audit: Audit):Boolean{
        return auditDatabaseManager.updateAudit(audit)
    }
}