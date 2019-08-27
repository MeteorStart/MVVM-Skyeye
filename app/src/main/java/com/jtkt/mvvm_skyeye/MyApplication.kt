package com.jtkt.mvvm_skyeye

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/8/26 17:31
 * @company:
 * @email: lx802315@163.com
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Context
    }
}