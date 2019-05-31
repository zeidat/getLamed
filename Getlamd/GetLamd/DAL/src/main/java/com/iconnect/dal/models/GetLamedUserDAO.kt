package com.iconnect.dal.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class GetLamedUserDAO:RealmObject() {

    @PrimaryKey
    var getLamdUserId:Long? = null
    var arabicName:String?  = null
    var hebrewName:String? = null
    var email:String? = null
    var nationalId:String? = null
    var phone:String? = null

}