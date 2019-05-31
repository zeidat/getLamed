package com.zeidat.getlamd.models

import com.getlamd.entity.user.Student

class AppointmentType {
    var type:String? = null
    var student:Student? = null

    constructor(type: String?, student: Student?) {
        this.type = type
        this.student = student
    }
}