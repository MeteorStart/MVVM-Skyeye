package com.jtkt.mvvm_skyeye.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import com.jtkt.mvvm_skyeye.MyApplication
import com.jtkt.mvvm_skyeye.R
import com.jtkt.mvvm_skyeye.base.BaseActivity
import com.jtkt.mvvm_skyeye.databinding.ActivitySplashBinding
import com.jtkt.mvvm_skyeye.utils.AppUtils
import com.jtkt.mvvm_skyeye.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    /**
     * 权限申请列表
     */
    var permissions =
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

    private lateinit var onPermissionListener: OnPermission

    private var textTypeface: Typeface? = null

    private var descTypeFace: Typeface? = null

    private var alphaAnimation: AlphaAnimation? = null

    init {
        textTypeface =
            Typeface.createFromAsset(MyApplication.instance.assets, "fonts/Lobster-1.4.otf")
        descTypeFace = Typeface.createFromAsset(
            MyApplication.instance.assets,
            "fonts/FZLanTingHeiS-L-GB-Regular.TTF"
        )
    }

    override fun getLayoutId(): Int = R.layout.activity_splash

    @SuppressLint("SetTextI18n")
    override fun initView() {

        tvAppName.typeface = textTypeface
        tvSplashDesc.typeface = descTypeFace
        tvVersionName.text = "v${AppUtils.getVerName(this)}"

        //渐变启动
        alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        alphaAnimation?.duration = 2000
        alphaAnimation?.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                redirectToMain()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })

    }

    override fun initData() {
        //获取读写权限
        if (!isHasPermission(this, permissions)) {
            initPermissions(this, permissions, onPermissionListener)
        } else {
            if (alphaAnimation != null) {
                ivWebIcon.startAnimation(alphaAnimation)
            }
        }
    }

    override fun initListener() {
        onPermissionListener = object : OnPermission {

            override fun hasPermission(granted: List<String>, isAll: Boolean) {
                if (isAll) {
                    if (alphaAnimation != null) {
                        ivWebIcon.startAnimation(alphaAnimation)
                    }
                    ToastUtils.showToast(this@SplashActivity, "获取权限成功！")
                } else {
                    ToastUtils.showToast(this@SplashActivity, "获取权限成功，部分权限未正常授予！")
                }
            }

            override fun noPermission(denied: List<String>, quick: Boolean) {
                if (quick) {
                    ToastUtils.showToast(this@SplashActivity, "被永久拒绝授权，请手动授予权限！")
                    //如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.gotoPermissionSettings(this@SplashActivity)
                } else {
                    ToastUtils.showToast(this@SplashActivity, "获取权限失败！")
                }
            }
        }
    }

    override fun needTransparentStatus(): Boolean = true

    /**
     * @description: 跳转主页
     * @date: 2019/8/26 17:46
     * @author: Meteor
     * @email: lx802315@163.com
     */
    fun redirectToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
