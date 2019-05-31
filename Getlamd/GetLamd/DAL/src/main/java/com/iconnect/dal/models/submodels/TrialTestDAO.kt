package com.iconnect.dal.models.submodels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TrialTestDAO() : RealmObject() {
    @PrimaryKey
    var trialTestId:Long? = null

    private var studentId:Long? = null
    private var details:String? = null

    constructor(trialTestId: Long?, studentId: Long?, details: String?) : this() {
        this.trialTestId = trialTestId
        this.studentId = studentId
        this.details = details
    }
}