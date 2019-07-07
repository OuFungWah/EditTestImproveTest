package com.crazywah.edittestimprovetest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatEditText

class FengHuaEditText : AppCompatEditText {

    companion object{
        private val TAG : String = "FengHuaEditText"
    }

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    // 用于监控指针变化的方法，当指针指在某处则 selstart 和 selend 相等
    // 当选取了字符，则 selstart 为开始位置 selend 为结束位置
    // This method is called when the selection has changed, in case any subclasses would like to know.
    override fun onSelectionChanged(selStart: Int, selEnd: Int) {

        Log.d(TAG,"$text && selstart = $selStart ; selend = $selEnd")
        super.onSelectionChanged(selStart, selEnd)
    }

    // 操作选项菜单的回调方法
    // Called when a context menu option for the text view is selected.
    override fun onTextContextMenuItem(id: Int): Boolean {
        when (id) {
            android.R.id.paste -> {

            }
            android.R.id.cut -> {

            }
            android.R.id.copy -> {

            }
            else -> {

            }
        }
        return super.onTextContextMenuItem(id)
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        return super.onCreateInputConnection(outAttrs)
    }


}