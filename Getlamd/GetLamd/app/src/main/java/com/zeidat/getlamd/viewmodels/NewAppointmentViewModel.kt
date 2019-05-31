package com.zeidat.getlamd.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.getlamd.entity.user.Student

class NewAppointmentViewModel:ViewModel() {
    private var listOfStudents  = MutableLiveData<List<Student>>()

    fun saveStudents(students:List<Student>){
        this.listOfStudents.value  =students
    }

    fun provideStudents():MutableLiveData<List<Student>>{
        return this.listOfStudents
    }
}