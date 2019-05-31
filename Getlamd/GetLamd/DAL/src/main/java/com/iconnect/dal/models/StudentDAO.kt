package com.iconnect.dal.models

import io.realm.annotations.PrimaryKey

open class StudentDAO {

    @PrimaryKey
    var studentId:Long? = null
    var getLamedUserId:Long? =  null


}