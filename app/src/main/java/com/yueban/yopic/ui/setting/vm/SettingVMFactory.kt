package com.yueban.yopic.ui.setting.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yueban.yopic.util.PrefManager
import com.yueban.yopic.util.di.scope.AppScope
import com.yueban.yopic.worker.WorkerUtil
import javax.inject.Inject

@AppScope
class SettingVMFactory
@Inject constructor(
    private val prefManager: PrefManager,
    private val workerUtil: WorkerUtil
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        SettingVM(prefManager, workerUtil) as T
}