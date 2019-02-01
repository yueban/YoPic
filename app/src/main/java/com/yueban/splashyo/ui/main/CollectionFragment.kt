package com.yueban.splashyo.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yueban.splashyo.R
import com.yueban.splashyo.databinding.FragmentCollectionBinding
import com.yueban.splashyo.ui.base.BaseViewFragment
import com.yueban.splashyo.ui.main.adapter.CollectionAdapter
import com.yueban.splashyo.ui.main.vm.CollectionVM
import com.yueban.splashyo.ui.main.vm.CollectionVMFactory
import com.yueban.splashyo.util.ext.autoAnimationOnly
import com.yueban.splashyo.util.ext.finishRefreshAndLoadMore
import com.yueban.splashyo.util.ext.scrollToTop
import com.yueban.splashyo.util.vm.LoadState
import javax.inject.Inject

/**
 * @author yueban
 * @date 2019/1/6
 * @email fbzhh007@gmail.com
 */
class CollectionFragment : BaseViewFragment<FragmentCollectionBinding>() {
    private lateinit var mAdapter: CollectionAdapter
    private lateinit var mCollectionVM: CollectionVM
    @Inject
    lateinit var collectionVMFactory: CollectionVMFactory

    override fun getLayoutId(): Int = R.layout.fragment_collection

    override fun initVMAndParams(savedInstanceState: Bundle?) {
        mCollectionVM = ViewModelProviders.of(requireActivity(), collectionVMFactory).get(CollectionVM::class.java)
        setHasOptionsMenu(true)
    }

    override fun initView() {
        mAdapter = CollectionAdapter(appComponent.appExecutors())
        mBinding.rvCollections.adapter = mAdapter

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mCollectionVM.loadNextPage()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mCollectionVM.refresh()
            }
        })
        mAdapter.itemClickListener = { collection ->
            findNavController().navigate(
                CollectionFragmentDirections.actionCollectionFragmentToPhotoListFragment(
                    collection.id.toString(),
                    collection.title
                )
            )
        }
    }

    override fun observeVM() {
        mCollectionVM.collections.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
        mCollectionVM.loadStatus.observe(viewLifecycleOwner, object : Observer<LoadState> {
            //record if last time is refreshing
            private var lastTimeIsRefreshing = false

            override fun onChanged(loadState: LoadState) {
                if (loadState.isRunning) {
                    lastTimeIsRefreshing = loadState.isRefreshing
                    mBinding.refreshLayout.autoAnimationOnly(loadState.isRefreshing, loadState.isLoadingMore)
                } else {
                    if (lastTimeIsRefreshing) {
                        mBinding.rvCollections.scrollToTop()
                        lastTimeIsRefreshing = false
                    }
                    val hasMore = mCollectionVM.hasMore
                    mBinding.refreshLayout.finishRefreshAndLoadMore(hasMore)
                }

                loadState.errorMsgIfNotHandled?.let {
                    Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
                }
            }
        })
        mCollectionVM.featured.observe(viewLifecycleOwner, Observer { featured ->
            activity?.setTitle(if (featured) R.string.featured else R.string.all_collections)
            activity?.invalidateOptionsMenu()
        })
    }

    override fun initData() {
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