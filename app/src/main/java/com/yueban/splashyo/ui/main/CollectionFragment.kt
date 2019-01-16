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
import androidx.navigation.fragment.findNavController
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
import com.yueban.splashyo.util.vm.LoadState

/**
 * @author yueban
 * @date 2019/1/6
 * @email fbzhh007@gmail.com
 */
class CollectionFragment : Fragment() {
    private lateinit var mBinding: FragmentCollectionBinding
    private lateinit var mAdapter: CollectionAdapter
    private lateinit var mCollectionVM: CollectionVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mCollectionVM =
            ViewModelProviders
                .of(
                    requireActivity(),
                    Injection.provideCollectionVMFactory(requireActivity())
                )
                .get(CollectionVM::class.java)
        setHasOptionsMenu(true)

        mBinding = FragmentCollectionBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = CollectionAdapter(Injection.provideAppExecutors())
        mBinding.rvCollections.adapter = mAdapter

        observeLiveData()
        setListener()
    }

    private fun observeLiveData() {
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
            findNavController().navigate(
                R.id.action_collectionFragment_to_photoListFragment,
                PhotoListFragmentArgs.Builder(collection.id.toString(), collection.title).build().toBundle()
            )
        }
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