package database

import entities.GroupObject
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf

class GroupDatabaseManager {
    //config
    private val hostname="localhost"
    private val databaseName= "expense_sharing"
    private val username= "root"
    private val password= "password"

    private val ktormDatabase: Database

    init{
        val jdbcUrl = "jdbc:mysql://$hostname:3306/$databaseName?user=$username&password=$password&useSSL=false"
        ktormDatabase= Database.connect(jdbcUrl)
    }

    fun addGroup(grpName:String): Int{

        val id= ktormDatabase.insertAndGenerateKey(GroupTable){
            set(GroupTable.grpName,grpName)
        } as Int

        return id
    }

    fun getGroupByName(name:String): GroupObject?{
        return ktormDatabase.sequenceOf(GroupTable)
            .firstOrNull{ GroupTable.grpName eq name}
            ?.let{ GroupObject(it.grpId,it.grpName) }?: null
    }
}