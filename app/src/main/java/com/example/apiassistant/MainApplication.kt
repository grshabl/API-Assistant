package com.example.apiassistant

import android.app.Application
import com.example.data.source.realm.RealmConfig
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val  config: RealmConfiguration = RealmConfiguration.Builder()
            .name(RealmConfig.NAME_DATABASE)
            .schemaVersion(1)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}