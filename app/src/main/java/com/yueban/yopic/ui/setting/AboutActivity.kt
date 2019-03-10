package com.yueban.yopic.ui.setting

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import com.yueban.yopic.BuildConfig
import com.yueban.yopic.R
import me.drakeet.support.about.AbsAboutActivity
import me.drakeet.support.about.Category
import me.drakeet.support.about.Contributor
import me.drakeet.support.about.License

@Suppress("SpellCheckingInspection")
class AboutActivity : AbsAboutActivity() {
    override fun onCreateHeader(icon: ImageView, slogan: TextView, version: TextView) {
        icon.setImageResource(R.mipmap.ic_launcher)
        slogan.text = getString(R.string.about_app_desc)
        @SuppressLint("SetTextI18n")
        version.text = "v${BuildConfig.VERSION_NAME}"
    }

    override fun onItemsCreated(items: MutableList<Any>) {
        items.add(Category("Developers"))
        items.add(
            Contributor(
                R.drawable.ic_avatar_yueban,
                "yueban",
                "Developer & designer",
                "https://github.com/yueban"
            )
        )

        items.add(Category("Open Source Licenses"))
        items.add(
            License(
                "kotlin",
                "jetbrains",
                License.APACHE_2,
                "https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib-jdk8"
            )
        )
        items.add(License("androidx", "google", License.APACHE_2, "https://mvnrepository.com/artifact/androidx"))
        items.add(
            License(
                "android.arch",
                "google",
                License.APACHE_2,
                "https://mvnrepository.com/artifact/android.arch"
            )
        )
        items.add(
            License(
                "material",
                "google",
                License.APACHE_2,
                "https://mvnrepository.com/artifact/com.google.android.material/material"
            )
        )
        items.add(License("glide", "bumptech", License.APACHE_2, "https://github.com/bumptech/glide"))
        items.add(
            License(
                "glide-transformations",
                "wasabeef",
                License.APACHE_2,
                "https://github.com/wasabeef/glide-transformations"
            )
        )
        items.add(License("okhttp", "square", License.APACHE_2, "https://github.com/square/okhttp"))
        items.add(License("retrofit", "square", License.APACHE_2, "https://github.com/square/retrofit"))
        items.add(
            License(
                "SmartRefreshLayout",
                "scwang90",
                License.APACHE_2,
                "https://github.com/scwang90/SmartRefreshLayout"
            )
        )
        items.add(License("moshi", "square", License.APACHE_2, "https://github.com/square/moshi"))
        items.add(License("kotshi", "ansman", License.APACHE_2, "https://github.com/ansman/kotshi"))
        items.add(License("RxPermissions", "tbruyelle", License.APACHE_2, "https://github.com/tbruyelle/RxPermissions"))
        items.add(License("dagger", "google", License.APACHE_2, "https://github.com/google/dagger"))
        items.add(License("RxJava", "ReactiveX", License.APACHE_2, "https://github.com/ReactiveX/RxJava"))
        items.add(
            License(
                "BottomSheetMenu",
                "Kennyc1012",
                License.APACHE_2,
                "https://github.com/Kennyc1012/BottomSheetMenu"
            )
        )
        items.add(License("tray", "grandcentrix", License.APACHE_2, "https://github.com/grandcentrix/tray"))
        items.add(License("RxBinding", "JakeWharton", License.APACHE_2, "https://github.com/JakeWharton/RxBinding"))
        items.add(License("RxLifecycle", "trello", License.APACHE_2, "https://github.com/trello/RxLifecycle"))
        items.add(License("Android-SpinKit", "ybq", License.MIT, "https://github.com/ybq/Android-SpinKit"))
        items.add(License("MultiType", "drakeet", License.APACHE_2, "https://github.com/drakeet/MultiType"))
        items.add(License("about-page", "drakeet", License.APACHE_2, "https://github.com/drakeet/about-page"))
        items.add(License("xlog", "elvishew", License.APACHE_2, "https://github.com/elvishew/xLog"))
        items.add(License("ImmersionBar", "gyf-dev", License.APACHE_2, "https://github.com/gyf-dev/ImmersionBar"))
        items.add(License("bouquet", "quanturium", License.APACHE_2, "https://github.com/quanturium/bouquet"))
        items.add(
            License(
                "ForegroundViews",
                "Commit451",
                License.APACHE_2,
                "https://github.com/Commit451/ForegroundViews"
            )
        )
    }
}