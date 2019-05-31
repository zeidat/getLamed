package com.iconnect.dal.models.submodels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class LessonDAO(): RealmObject() {
    @PrimaryKey
    var lessonId:Long? = null

    var studentId:Long? = null
    var lessonDetails:String? = null

    constructor(lessonId: Long?, studentId: Long?, lessonDetails: String?) : this() {
        this.lessonId = lessonId
        this.studentId = studentId
        this.lessonDetails = lessonDetails
    }
}