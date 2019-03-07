package com.yueban.yopic.ui.main

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yueban.yopic.R
import com.yueban.yopic.databinding.FragmentPhotoListBinding
import com.yueban.yopic.ui.base.BaseViewFragment
import com.yueban.yopic.ui.main.adapter.PhotoListAdapter
import com.yueban.yopic.ui.main.vm.PhotoListVM
import com.yueban.yopic.ui.main.vm.PhotoListVMFactory
import com.yueban.yopic.util.ext.autoAnimationOnly
import com.yueban.yopic.util.ext.finishRefreshAndLoadMore
import com.yueban.yopic.util.ext.toPx
import javax.inject.Inject

/**
 * @author yueban
 * @date 2019/1/15
 * @email fbzhh007@gmail.com
 */
class PhotoListFragment : BaseViewFragment<FragmentPhotoListBinding>() {
    private lateinit var mPhotoListVM: PhotoListVM
    private lateinit var mAdapter: PhotoListAdapter
    /**
     * when mCollectionId is null, it means that it was in MainActivity. in this case:
     * - use activity to get VM to enable cache
     */
    private var mCollectionId: String? = null
    private var mCollectionTitle: String? = null
    @Inject
    lateinit var photoListVMFactory: PhotoListVMFactory

    override fun getLayoutId(): Int = R.layout.fragment_photo_list

    override fun initVMAndParams(savedInstanceState: Bundle?) {
        try {
            val args = navArgs<PhotoListFragmentArgs>().value
            mCollectionId = args.collectionId
            mCollectionTitle = args.collectionTitle
            mPhotoListVM = ViewModelProviders.of(this, photoListVMFactory).get(PhotoListVM::class.java)
        } catch (e: IllegalStateException) {
            mCollectionId = null
            mCollectionTitle = getString(R.string.all_photos)
            mPhotoListVM = ViewModelProviders.of(requireActivity(), photoListVMFactory).get(PhotoListVM::class.java)
        }
    }

    override fun initView() {
        requireActivity().title = mCollectionTitle

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
        mAdapter = PhotoListAdapter(spanCount, spacing)
        mBinding.rvPhotos.adapter = mAdapter

        mBinding.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPhotoListVM.loadNextPage()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPhotoListVM.refresh()
            }
        })
        mAdapter.itemClickListener = { imageView, photo ->
            findNavController().navigate(
                PhotoListFragmentDirections.actionPhotoListFragmentToPhotoDetailActivity(photo),
                ActivityNavigatorExtras(
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        imageView,
                        imageView.transitionName
                    )
                )
            )
        }
    }

    override fun observeVM() {
        mPhotoListVM.photos.observe(viewLifecycleOwner, Observer(mAdapter::submitList))
        mPhotoListVM.loadState.observe(viewLifecycleOwner, Observer { loadState ->
            if (loadState.isRunning) {
                mBinding.refreshLayout.autoAnimationOnly(loadState.isRefreshing, loadState.isLoadingMore)
            } else {
                mBinding.refreshLayout.finishRefreshAndLoadMore(loadState.isSuccess, mPhotoListVM.hasMore)
            }

            loadState.errorMsgIfNotHandled?.let {
                Snackbar.make(mBinding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    override fun initData() {
        val cacheLabel = mCollectionId ?: PhotoListVM.CACHE_LABEL_ALL
        mPhotoListVM.setCacheLabel(cacheLabel)
    }
}