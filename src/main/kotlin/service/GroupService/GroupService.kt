package service.GroupService

import database.GroupDatabaseManager
import entities.GroupObject

class GroupService {
    private val groupDatabaseManager= GroupDatabaseManager()

    fun addGroup(grpName:String): GroupObject {
        return GroupObject(groupDatabaseManager.addGroup(grpName),grpName)
    }

    fun getGroupByName(name:String): GroupObject?{
        return groupDatabaseManager.getGroupByName(name)
    }
}