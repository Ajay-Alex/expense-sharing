package database

import entities.GroupObject
import entities.User
import entities.UserDraft
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf

class UserDatabaseManager {
    //config
    private val hostname="localhost"
    private val databaseName= "expense_sharing"
    private val username= "root"
    private val password= "password"

    private val ktormDatabase: Database

    private val groupDatabase = GroupDatabaseManager()


    init{
        val jdbcUrl = "jdbc:mysql://$hostname:3306/$databaseName?user=$username&password=$password&useSSL=false"
        ktormDatabase=Database.connect(jdbcUrl)
    }
    fun getUserById(id:Int): DBUserEntity?{
        return ktormDatabase.sequenceOf(UserTable)
            .firstOrNull{ UserTable.userid eq id}
    }
    fun getUserByName(name:String): DBUserEntity?{
        return ktormDatabase.sequenceOf(UserTable)
            .firstOrNull{ UserTable.name eq name}
    }
    fun getUsersByGroup(group:Int):Query{
        return ktormDatabase.from(UserTable).select()
    }
    fun addUser(draft: UserDraft, group: GroupObject):Int{
        return ktormDatabase.insertAndGenerateKey(UserTable){
            set(UserTable.name,draft.name)
            set(UserTable.email,draft.email)
            set(UserTable.mobile,draft.mobile)
            set(UserTable.group,group.grpId)
        } as Int
    }

}