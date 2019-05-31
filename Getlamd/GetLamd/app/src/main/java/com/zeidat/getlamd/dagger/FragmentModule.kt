package com.zeidat.getlamd.dagger

import com.zeidat.getlamd.fragments.*
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    @Provides
    fun provideAddNewAppointmentFragment():AddNewAppointmentFragment {
        return AddNewAppointmentFragment()
    }

    @Provides
    fun provideAddStudent():AddStudent {
        return AddStudent()
    }

    @Provides
    fun provideAppointmentFragment():AppointmentFragment {
        return AppointmentFragment()
    }

    @Provides
    fun provideHomeFragment():HomeFragment {
        return HomeFragment()
    }

    @Provides
    fun provideProfielFragment():ProfileFragment {
        return ProfileFragment()
    }

    @Provides
    fun provideSettingsFragment():Settings {
        return Settings()
    }

    @Provides
    fun provideStudentCard():StudentCard {
        return StudentCard()
    }

    @Provides
    fun provideStudentsFragment():StudentsFragment {
        return StudentsFragment()
    }

    @Provides
    fun provideVehicleFragment():VehicleFragment {
        return VehicleFragment()
    }

    @Provides
    fun provideAddNewVehicleFragment():AddNewVehicleFragment {
        return AddNewVehicleFragment()
    }

    @Provides
    fun provideAppointmentDetails():AppointmentDetails {
        return AppointmentDetails()
    }

    @Provides
    fun provideAppointmentComplete():AppointmentComplete {
        return AppointmentComplete()
    }
}
