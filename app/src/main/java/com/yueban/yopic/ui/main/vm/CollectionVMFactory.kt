package com.yueban.yopic.ui.main.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yueban.yopic.data.repo.PhotoRepo
import com.yueban.yopic.util.di.scope.AppScope
import javax.inject.Inject

/**
 * @author yueban
 * @date 2019/1/5
 * @email fbzhh007@gmail.com
 */
@AppScope
class CollectionVMFactory
@Inject constructor(private val app: Application, private val photoRepo: PhotoRepo) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CollectionVM(app, photoRepo) as T
    }
}