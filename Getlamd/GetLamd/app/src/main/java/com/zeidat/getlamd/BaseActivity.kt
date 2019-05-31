package com.zeidat.getlamd

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zeidat.getlamd.dagger.AppComponent
import com.zeidat.getlamd.dagger.AppModule
import com.zeidat.getlamd.dagger.DaggerAppComponent
import com.zeidat.getlamd.dagger.FragmentModule


open class BaseActivity : AppCompatActivity() {

    val studentSharedPreferences: SharedPreferences
        get() = getSharedPreferences("student", Context.MODE_PRIVATE)

    val dateSahredPreferences: SharedPreferences
        get() = getSharedPreferences("appointmentDate", Context.MODE_PRIVATE)

    val selectedStudent: SharedPreferences
        get() = getSharedPreferences("select", Context.MODE_PRIVATE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main);

    }

    fun provideSharedPreferences(): SharedPreferences {
        return getSharedPreferences("login", Context.MODE_PRIVATE)
    }

    fun getApplicationCustomComponent(): AppComponent {
        return DaggerAppComponent.builder()
                  .fragmentModule(FragmentModule())
                  .appModule(AppModule(this))
                  .build()
    }





}
