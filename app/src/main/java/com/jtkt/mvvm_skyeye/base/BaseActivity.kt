package com.jtkt.mvvm_skyeye.base

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * @author: X_Meteor
 * @description: Activity基类
 * @version: V_1.0.0
 * @date: 2019/8/26 16:53
 * @company:
 * @email: lx802315@163.com
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(),
    CoroutineScope by MainScope() {

    protected val mBinding: VB by lazy {

        DataBindingUtil.setContentView(this, getLayoutId()) as VB

    }

    /**
     * 初始化根布局文件
     */
    abstract fun getLayoutId(): Int

    /**
     * 初始化控件
     */
    abstract fun initView()

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化接口
     */
    abstract fun initListener()

    /**
     * 是否需要透明状态栏
     */
    protected open fun needTransparentStatus(): Boolean = false

    /**
     * 透明状态栏
     */
    open fun transparentStatusBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
        supportActionBar?.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStackManager.addActivity(this)
        if (needTransparentStatus()) transparentStatusBar()
        mBinding.lifecycleOwner = this

        initListener()
        initView()
        initData()

    }

    /**
     * 获取 ViewModel
     */
    fun <T : ViewModel> getViewModel(clazz: Class<T>): T = ViewModelProviders.of(this).get(clazz)

    /**
     * @description: 初始化权限管理
     * @date: 2018/9/29 16:41
     * @author: Meteor
     * @email: lx802315@163.com
     */
    protected fun initPermissions(activity: Activity, permissions: Array<String>, onPermissionListener: OnPermission) {
        XXPermissions.with(activity)
            //.constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
            .permission(permissions)
            //                .permission(Permission.Group.STORAGE, Permission.Group.CALENDAR) //不指定权限则自动获取清单中的危险权限
            .request(onPermissionListener)
    }

    /**
     * @name: 判断是否具有此权限
     * @description: 方法描述
     * @date: 2018/9/17 16:49
     * @company:
     * @author: Meteor
     */
    protected fun isHasPermission(context: Context, permissions: Array<String>): Boolean {
        return XXPermissions.isHasPermission(context, permissions)
    }

    /**
     * @description: 跳转到权限设置页面
     * @date: 2018/9/29 16:42
     * @author: Meteor
     * @email: lx802315@163.com
     */
    protected fun gotoPermissionSettings(context: Context) {
        XXPermissions.gotoPermissionSettings(context)
    }
}