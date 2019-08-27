package com.jtkt.mvvm_skyeye.base

import android.app.Activity

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/8/26 17:19
 * @company:
 * @email: lx802315@163.com
 */
object ActivityStackManager {

    private val activities = ArrayList<Activity>()

    @JvmStatic
    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    @JvmStatic
    fun removeActivity(activity: Activity) {
        if (activities.contains(activity)) {
            activities.remove(activity)
            activity.finish()
        }
    }

    @JvmStatic
    fun getTopActivity(): Activity? = if (activities.isEmpty()) null else activities[activities.size - 1]

    @JvmStatic
    fun finishAll() {
        for (a in activities)
            if (!a.isFinishing) a.finish()
    }
}