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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yueban.splashyo.R
import com.yueban.splashyo.databinding.FragmentCollectionBinding
import com.yueban.splashyo.ui.main.adapter.CollectionAdapter
import com.yueban.splashyo.ui.main.vm.CollectionVM
import com.yueban.splashyo.util.Injection
import timber.log.Timber

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

        // live data
        mCollectionVM.collections.observe(this, Observer {
            mAdapter.submitList(it)
        })
        mCollectionVM.loadMoreStatus.observe(this, Observer { loadMoreStatus ->
            Timber.d("loadMoreStatus.isRunning: ${loadMoreStatus.isRunning}")
            Timber.d("loadMoreStatus.errorMsgIfNotHandled: ${loadMoreStatus.errorMsgIfNotHandled}")
        })
        mCollectionVM.featured.observe(this, Observer {
            activity?.invalidateOptionsMenu()
        })

        //set listener
        mBinding.rvCollections.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePos = linearLayoutManager.findLastVisibleItemPosition()
                if (lastVisiblePos == linearLayoutManager.itemCount - 1) {
                    mCollectionVM.loadNextPage()
                }
            }
        })
        mAdapter.itemClickListener = { collection ->
            Snackbar.make(mBinding.root, collection.title, Snackbar.LENGTH_SHORT).show()
        }

        initData()
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