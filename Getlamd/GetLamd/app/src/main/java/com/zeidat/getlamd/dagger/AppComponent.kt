package com.zeidat.getlamd.dagger

import com.zeidat.getlamd.activites.Login
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.fragments.*
import dagger.Component

@Component(modules = [FragmentModule::class, AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(login: Login)
    fun inject(addStudent: AddStudent)
    fun inject(appointmentFragment: AppointmentFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(settings: Settings)
    fun inject(studentCard: StudentCard)
    fun inject(studentsFragment: StudentsFragment)
    fun inject(vehicleFragment: VehicleFragment)
    fun inject(addNewAppointmentFragment: AddNewAppointmentFragment)
    fun inject(addNewVehicleFragment: AddNewVehicleFragment)
    fun inject(appointmentDetails: AppointmentDetails)
    fun inject(appointmentComplete: AppointmentComplete)

}