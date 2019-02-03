package com.yueban.splashyo.util.bottomsheet

import com.kennyc.bottomsheet.BottomSheetListener
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment

/**
 * @author yueban
 * @date 2019-02-02
 * @email fbzhh007@gmail.com
 */
abstract class SimpleBottomSheetListener : BottomSheetListener {
    override fun onSheetDismissed(bottomSheet: BottomSheetMenuDialogFragment, `object`: Any?, dismissEvent: Int) {
    }

    override fun onSheetShown(bottomSheet: BottomSheetMenuDialogFragment, `object`: Any?) {
    }
}