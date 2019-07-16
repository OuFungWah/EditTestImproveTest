package com.crazywah.edittestimprovetest

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import androidx.appcompat.widget.AppCompatEditText

class FengHuaEditText : AppCompatEditText {

    companion object{
        private val TAG : String = "FengHuaEditText"
    }

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                Log.d(TAG,"TextWatcher --- afterTextChanged() p0 = $p0")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG,"TextWatcher --- beforeTextChanged() p0 = $p0 ; p1 = $p1 ; p2 = $p2 ; p3 = $p3")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG,"TextWatcher --- onTextChanged() p0 = $p0 ; p1 = $p1 ; p2 = $p2 ; p3 = $p3")
            }

        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    // 用于监控指针变化的方法，当指针指在某处则 selstart 和 selend 相等
    // 当选取了字符，则 selstart 为开始位置 selend 为结束位置
    // This method is called when the selection has changed, in case any subclasses would like to know.
    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        val curSpan = text?.getSpans(selStart, selEnd, ForegroundColorSpan::class.java)
        if (curSpan!=null)
        for(i in 0 until curSpan.size){
            Log.d(TAG,"EditText --- onSelectionChanged ${curSpan[i].toString()}")
        }
        Log.d(TAG,"$text && selstart = $selStart ; selend = $selEnd")
        super.onSelectionChanged(selStart, selEnd)
    }

    // 操作选项菜单的回调方法
    // Called when a context menu option for the text view is selected.
    override fun onTextContextMenuItem(id: Int): Boolean {
        when (id) {
            android.R.id.paste -> {
                Log.d(TAG,"EditText --- paste callback")
            }
            android.R.id.cut -> {
                Log.d(TAG,"EditText --- cut callback")
            }
            android.R.id.copy -> {
                Log.d(TAG,"EditText --- copy callback")
            }
            else -> {

            }
        }
        return super.onTextContextMenuItem(id)
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        Log.d(TAG,"EditText --- onTextChanged: text = $text ; start = $start ; lengthBefore = $lengthBefore ; lengthAfter = $lengthAfter")
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        val connection = FenghuaInputConnectionWrapper(super.onCreateInputConnection(outAttrs),true)
//        connection.sendKeyEvent(KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL))
//        Log.d(TAG," connection ")
        return connection
    }

    // InputConnection 主要用于接收来自 IME 的指令
    // 不同的 IME 可能使用不同的方式来传递指令，所以这些我们需要自己考虑所有情况
    class FenghuaInputConnectionWrapper(target: InputConnection?, mutable: Boolean) :
        InputConnectionWrapper(target, mutable) {

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            Log.d(TAG,"FenghuaInputConnectionWrapper--- deleteSurroundingText beforeLength = $beforeLength ; afterLength = $afterLength")
            return super.deleteSurroundingText(1, 0)
        }

        override fun sendKeyEvent(event: KeyEvent?): Boolean {
            Log.d(TAG,"FenghuaInputConnectionWrapper--- sendKeyEvent keycode = ${event?.keyCode}")
            return super.sendKeyEvent(event)
        }

        // 从 IME 传来的文本内容以及添加文本内容后光标所在的位置
        override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
            Log.d(TAG,"FenghuaInputConnectionWrapper--- commitText text = $text")
            return super.commitText(text, newCursorPosition)
        }

        override fun setComposingText(text: CharSequence?, newCursorPosition: Int): Boolean {
            Log.d(TAG,"FenghuaInputConnectionWrapper--- setComposingText text = $text")
            return super.setComposingText(text, newCursorPosition)
        }

    }

}