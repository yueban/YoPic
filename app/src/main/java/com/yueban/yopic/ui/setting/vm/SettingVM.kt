package com.yueban.yopic.ui.setting.vm

import androidx.lifecycle.ViewModel
import com.yueban.yopic.data.model.util.WallpaperSwitchOption
import com.yueban.yopic.data.model.util.WallpaperSwitchOptionObservable
import com.yueban.yopic.util.PrefKey
import com.yueban.yopic.util.PrefManager
import com.yueban.yopic.worker.WorkerUtil

class SettingVM(
    private val prefManager: PrefManager,
    private val workerUtil: WorkerUtil
) : ViewModel() {
    private val option = prefManager.getObject(PrefKey.WALLPAPER_SWITCH_OPTION, WallpaperSwitchOption::class.java)
        ?: WallpaperSwitchOption()
    val optionObservable: WallpaperSwitchOptionObservable = WallpaperSwitchOptionObservable(option)

    fun refreshWorkIfNeeded() {
        val newOption = optionObservable.toOption()
        if (newOption != option) {
            prefManager.put(PrefKey.WALLPAPER_SWITCH_OPTION, newOption, WallpaperSwitchOption::class.java)
            workerUtil.refreshWallpaperChangeTask(true)
        }
    }
}