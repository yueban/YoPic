package com.yueban.splashyo.ui.main

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yueban.splashyo.databinding.FragmentPhotoListBinding
import com.yueban.splashyo.ui.main.adapter.PhotoListAdapter
import com.yueban.splashyo.ui.main.vm.PhotoListVM
import com.yueban.splashyo.util.Injection
import com.yueban.splashyo.util.ext.autoAnimationOnly
import com.yueban.splashyo.util.ext.finishRefreshAndLoadMore
import com.yueban.splashyo.util.ext.toPx

/**
 * @author yueban
 * @date 2019/1/15
 * @email fbzhh007@gmail.com
 */
class PhotoListFragment : Fragment() {
    private lateinit var mBinding: FragmentPhotoListBinding
    private lateinit var mPhotoListVM: PhotoListVM
    private lateinit var mAdapter: PhotoListAdapter
    private var mCollectionId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentPhotoListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            mCollectionId = PhotoListFragmentArgs.fromBundle(it).collectionId
        }

        mPhotoListVM =
            ViewModelProviders.of(this, Injection.providePhotoListVMFactory(requireActivity()))
                .get(PhotoListVM::class.java)

        val spanCount = 3
        val spacing = 6.toPx()
        mBinding.rvPhotos.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        mBinding.rvPhotos.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val position = parent.getChildAdapterPosition(view)
                if (position >= 0) {
                    val column = position % spanCount
                    outRect.left = spacing - column * spacing / spanCount
                    outRect.right = (column + 1) * spacing / spanCount
                    if (position < spanCount) { // top edge
                        outRect.top = spacing
                    }
                    outRect.bottom = spacing // item bottom
                } else {
                    outRect.left = 0
                    outRect.right = 0
                    outRect.top = 0
                    outRect.bottom = 0
                }
            }
        })

        mAdapter = PhotoListAdapter(Injection.provideAppExecutors(), spanCount, spacing)
        mBinding.rvPhotos.adapter = mAdapter

        observeLiveData()
        setListener()
        initData()
    }

    private fun observeLiveData() {
        mPhotoListVM.photos.observe(this, Observer(mAdapter::submitList))
        mPhotoListVM.loadStatus.observe(this, Observer { loadState ->
            if (loadState.isRunning) {
                mBinding.refreshLayout.autoAnimationOnly(loadState.isRefreshing, loadState.isLoadingMore)
            } else {
                val hasMore = mPhotoListVM.hasMore
                mBinding.refreshLayout.finishRefreshAndLoadMore(hasMore)
            }

            loadState.errorMsgIfNotHandled?.let {
                Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun setListener() {
        mBinding.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPhotoListVM.loadNextPage()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPhotoListVM.refresh()
            }
        })
        mAdapter.itemClickListener = { photo ->
            Snackbar.make(
                mBinding.root,
                "userName:${photo.userName}\nlikes:${photo.likes}\nsponsor:${photo.sponsorName}",
                Snackbar.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun initData() {
        val cacheLabel = mCollectionId ?: PhotoListVM.CACHE_LABEL_ALL
        mPhotoListVM.setCacheLabel(cacheLabel)
    }
}