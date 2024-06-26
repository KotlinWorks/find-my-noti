package com.mrwhoknows.findmynoti

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.mrwhoknows.findmynoti.di.appModule
import com.mrwhoknows.findmynoti.di.notificationListModule
import com.mrwhoknows.findmynoti.di.settingsModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(appModule, notificationListModule, settingsModule)
        }
        if (isDebugBuild()) {
            Napier.base(DebugAntilog())
        }
    }

    private fun isDebugBuild(): Boolean {
        return try {
            val pm = applicationContext.packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    companion object {
        private var instance: MainApplication? = null

        val applicationContext: Context
            get() = instance!!.applicationContext
    }

}