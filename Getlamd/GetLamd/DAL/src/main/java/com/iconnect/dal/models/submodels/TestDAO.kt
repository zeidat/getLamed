package com.iconnect.dal.models.submodels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TestDAO(): RealmObject() {
    @PrimaryKey
    var testId:Long? =  null

    var studentId:Long? = null
    var testDetails:String? = null

    constructor(testId: Long?, studentId: Long?, testDetails: String?) : this() {
        this.testId = testId
        this.studentId = studentId
        this.testDetails = testDetails
    }
}