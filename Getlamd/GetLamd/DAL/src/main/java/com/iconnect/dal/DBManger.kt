package com.iconnect.dal

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel

object DBManger {


    private var realm: Realm? = null

    fun <S : RealmModel?> insert(obj: List<S>, type: Class<S>) {
        this.realm = Realm.getInstance(configureRealm())
        this.realm!!.beginTransaction()
        this.realm!!.insertOrUpdate(obj)
        this.realm!!.commitTransaction()
    }

    fun <S : RealmModel?> getAll(type: Class<S>): List<Any> {
        this.realm = Realm.getInstance(configureRealm())
        this.realm!!.beginTransaction()
        var res = this.realm!!.where(type).findAll().toArray().toList()
        this.realm!!.commitTransaction()
        return res
    }

    private fun configureRealm(): RealmConfiguration {
        return RealmConfiguration.Builder()
                  .name("get-lamd")
                  .build()
    }

}