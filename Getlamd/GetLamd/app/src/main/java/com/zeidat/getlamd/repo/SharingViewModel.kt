package com.zeidat.getlamd.repo

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.MutableLiveData
import com.getlamd.entity.buisness.Appointment


class SharingViewModel:ViewModel() {

    private val selectedAppointment = MutableLiveData<Appointment>()
    private val allAppointments = MutableLiveData<List<Appointment>>()

    fun selectAppointment(appointment: Appointment) {
        this.selectedAppointment.value = appointment
    }

    fun getSelectedAppointment(): MutableLiveData<Appointment> {
        return this.selectedAppointment
    }

    fun getAllAppointments():MutableLiveData<List<Appointment>>{
        return this.allAppointments
    }

    fun saveAppointments(appointments: List<Appointment>){
        this.allAppointments.value = appointments

    }



}