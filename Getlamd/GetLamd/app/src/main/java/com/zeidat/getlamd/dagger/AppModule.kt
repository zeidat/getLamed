package com.zeidat.getlamd.dagger

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context:Context) {

    @Provides
    fun provideSharedPreferences():SharedPreferences {
        return context.getSharedPreferences("login",Context.MODE_PRIVATE)
    }

    @Provides
    fun provideContext():Context {
        return context
    }
}