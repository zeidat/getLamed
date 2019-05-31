package com.zeidat.getlamd.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.getlamd.entity.management.Vehicle

class AddStudentViewModel:ViewModel() {
    private val selectedVehicle  = MutableLiveData<List<Vehicle>>()

    fun saveVehicle(vehicle: List<Vehicle>){
        this.selectedVehicle.value  = vehicle
    }

    fun provideVehicle():MutableLiveData<List<Vehicle>>{
        return this.selectedVehicle
    }
}