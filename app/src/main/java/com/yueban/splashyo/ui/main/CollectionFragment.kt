package com.yueban.splashyo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yueban.splashyo.R
import com.yueban.splashyo.databinding.FragmentCollectionBinding
import com.yueban.splashyo.ui.main.adapter.CollectionAdapter
import com.yueban.splashyo.ui.main.vm.CollectionVM
import com.yueban.splashyo.util.Injection
import com.yueban.splashyo.util.ext.autoAnimationOnly
import com.yueban.splashyo.util.ext.finishRefreshAndLoadMore
import com.yueban.splashyo.util.ext.scrollToTop

/**
 * @author yueban
 * @date 2019/1/6
 * @email fbzhh007@gmail.com
 */
class CollectionFragment : Fragment() {
    private lateinit var mBinding: FragmentCollectionBinding
    private lateinit var mAdapter: CollectionAdapter
    private lateinit var mCollectionVM: CollectionVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCollectionVM =
            ViewModelProviders
                .of(
                    requireActivity(),
                    Injection.provideCollectionVMFactory(requireActivity())
                )
                .get(CollectionVM::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentCollectionBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = CollectionAdapter(Injection.provideAppExecutors())
        mBinding.rvCollections.adapter = mAdapter

        observeLiveData()
        setListener()
        initData()
    }

    private fun observeLiveData() {
        mCollectionVM.collections.observe(this, Observer {
            mAdapter.submitList(it)
        })
        mCollectionVM.loadStatus.observe(this, Observer { loadState ->
            if (loadState.isRunning) {
                mBinding.refreshLayout.autoAnimationOnly(loadState.isRefreshing, loadState.isLoadingMore)
            } else {
                mBinding.refreshLayout.finishRefreshAndLoadMore()
            }

            loadState.errorMsgIfNotHandled?.let {
                Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        })
        mCollectionVM.featured.observe(this, Observer {
            activity?.invalidateOptionsMenu()
            mBinding.rvCollections.scrollToTop()
        })
    }

    private fun setListener() {
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mCollectionVM.loadNextPage()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mCollectionVM.refresh()
            }
        })
        mAdapter.itemClickListener = { collection ->
            Snackbar.make(mBinding.root, collection.title, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
        mCollectionVM.setFeatured(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        val featured = mCollectionVM.featured.value
        featured?.let {
            inflater?.inflate(R.menu.menu_collection_view, menu)
            menu?.let {
                val menuFeatured = menu.findItem(R.id.menu_collection_featured)
                val menuAll = menu.findItem(R.id.menu_collection_all)
                menuFeatured.isVisible = !featured
                menuAll.isVisible = featured
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            return when (item.itemId) {
                R.id.menu_collection_featured -> {
                    mCollectionVM.setFeatured(true)
                    true
                }
                R.id.menu_collection_all -> {
                    mCollectionVM.setFeatured(false)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}