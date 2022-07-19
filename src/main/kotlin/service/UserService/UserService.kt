package service.UserService

import database.DBUserEntity
import database.GroupDatabaseManager
import database.UserDatabaseManager
import database.UserTable
import entities.User
import entities.UserDraft
import org.ktorm.dsl.from
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.select

class UserService {

    private val userDatabaseManager= UserDatabaseManager()
    private val groupDatabaseManager= GroupDatabaseManager()

    fun getUserById(id:Int): DBUserEntity?{
        return userDatabaseManager.getUserById(id)
    }

    fun getUserByName(name:String): DBUserEntity?{
        return userDatabaseManager.getUserByName(name)
    }
    fun getUsersByGroup(group:Int):List<User>{
        val query=userDatabaseManager.getUsersByGroup(group)
        val userList= mutableListOf<User>()
        for(row in query){
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

    fun addUser(draft: UserDraft): User?{
        val group=groupDatabaseManager.getGroupByName(draft.group)?:return null
        val id = userDatabaseManager.addUser(draft,group)

        return User(id,draft.name,draft.email,draft.mobile,group.grpId)
    }
}