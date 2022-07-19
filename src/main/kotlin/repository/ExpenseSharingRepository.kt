package repository

import entities.Audit
import entities.User
import entities.UserDraft
import service.AuditService.AuditService
import service.GroupService.GroupService
import service.UserService.UserService

class ExpenseSharingRepository {

    private val userService = UserService()
    private val auditService = AuditService()
    private val groupService = GroupService()

    fun getUser(id:Int): User?{
        return userService.getUserById(id)
            ?.let{ User(it.userid,it.name,it.email,it.mobile,it.group) }
    }
    fun createUser(draft: UserDraft): User? {
        return userService.addUser(draft)
    }
    fun addAudit(audit: Audit): Audit {
        val aud=auditService.getAudit(audit.toName,audit.fromName)
        if(aud == null){
            auditService.addAudit(audit)
            return audit
        }
        else{
            aud.amount+=audit.amount
            auditService.updateAudit(aud)
        }
        return aud
    }
    fun addTxn(name:String,amount:Int):Int{
        val user=userService.getUserByName(name)?:return -1
        val userList=userService.getUsersByGroup(user.group)
        val split:Float= amount.toFloat()/(userList.size).toFloat()
        for(i in userList){
            if(i.name!=name){
                addAudit(Audit(name,i.name,split))
            }
        }
        return 1
    }
    fun createGroupByName(grpName:String):Int{
        val group=groupService.getGroupByName(grpName)
        return if(group!=null) 0
        else groupService.addGroup(grpName).grpId
    }

}