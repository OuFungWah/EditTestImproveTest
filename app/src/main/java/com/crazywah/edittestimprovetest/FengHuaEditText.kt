package com.crazywah.edittestimprovetest

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.*
import android.text.style.BackgroundColorSpan
import android.text.style.DynamicDrawableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.text.set
import kotlin.math.abs
import kotlin.random.Random

class FengHuaEditText : AppCompatEditText {

    companion object {
        private val TAG: String = "FengHuaEditText"
        private val random = Random(System.currentTimeMillis())
        private var count = 0

        fun randomColor(): Int {
            return Color.parseColor(
                "#${abs(random.nextInt(25, 256)).toString(16)}" +
                        abs(random.nextInt(25, 256)).toString(16) +
                        abs(random.nextInt(25, 256)).toString(16)
            )
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var start = 0
    private var end = 0

    init {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                Log.d(TAG, "TextWatcher --- afterTextChanged() editable = $editable")
                var foregroundSpan = ForegroundColorSpan(randomColor())
                var backgroundSpan = BackgroundColorSpan(randomColor())
                if (start <= end) {
                    editableText.setSpan(foregroundSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    editableText.setSpan(backgroundSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                }
                var changeText = editableText.subSequence(start,end)
                Log.d(TAG, "TextWatcher --- afterTextChanged() changeText = $changeText")
                if("icon"==changeText.toString()){
                    Log.d(TAG, "TextWatcher --- afterTextChanged() 开始转换")
                    var imageSpan = ImageSpan(context,R.mipmap.ic_launcher_round,DynamicDrawableSpan.ALIGN_CENTER)
                    editableText.setSpan(imageSpan,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(
                    TAG,
                    "TextWatcher --- beforeTextChanged() charSequence = $charSequence ; start = $start ; count = $count ; after = $after"
                )
                this@FengHuaEditText.start = start
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(
                    TAG,
                    "TextWatcher --- onTextChanged() charSequence = $charSequence ; start = $start ; before = $before ; count = $count"
                )
                end = count + start
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
        if (curSpan != null)
            for (i in 0 until curSpan.size) {
                Log.d(TAG, "EditText --- onSelectionChanged ${curSpan[i].toString()}")
            }
        Log.d(TAG, "EditText --- onSelectionChanged() $text && selstart = $selStart ; selend = $selEnd")
        super.onSelectionChanged(selStart, selEnd)
    }

    // 操作选项菜单的回调方法
    // Called when a context menu option for the text view is selected.
    override fun onTextContextMenuItem(id: Int): Boolean {
        when (id) {
            android.R.id.paste -> {
                Log.d(TAG, "EditText --- paste callback")
            }
            android.R.id.cut -> {
                Log.d(TAG, "EditText --- cut callback")
            }
            android.R.id.copy -> {
                Log.d(TAG, "EditText --- copy callback")
            }
            else -> {

            }
        }
        return super.onTextContextMenuItem(id)
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        Log.d(
            TAG,
            "EditText --- onTextChanged: text = $text ; start = $start ; lengthBefore = $lengthBefore ; lengthAfter = $lengthAfter"
        )
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        val connection = FenghuaInputConnectionWrapper(super.onCreateInputConnection(outAttrs), true)
//        connection.sendKeyEvent(KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL))
//        Log.d(TAG," connection ")
        return connection
    }

    // InputConnection 主要用于接收来自 IME 的指令
    // 不同的 IME 可能使用不同的方式来传递指令，所以这些我们需要自己考虑所有情况
    class FenghuaInputConnectionWrapper(target: InputConnection?, mutable: Boolean) :
        InputConnectionWrapper(target, mutable) {

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            Log.d(
                TAG,
                "FenghuaInputConnectionWrapper--- deleteSurroundingText beforeLength = $beforeLength ; afterLength = $afterLength"
            )
            return super.deleteSurroundingText(1, 0)
        }

        override fun sendKeyEvent(event: KeyEvent?): Boolean {
            Log.d(TAG, "FenghuaInputConnectionWrapper--- sendKeyEvent keycode = ${event?.keyCode}")
            return super.sendKeyEvent(event)
        }

        // 从 IME 传来的文本内容以及添加文本内容后光标所在的位置
        override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
            Log.d(TAG, "FenghuaInputConnectionWrapper--- commitText text = $text")
            return super.commitText(text, newCursorPosition)
        }

        override fun setComposingText(text: CharSequence?, newCursorPosition: Int): Boolean {
            Log.d(TAG, "FenghuaInputConnectionWrapper--- setComposingText text = $text")
            return super.setComposingText(text, newCursorPosition)
        }

    }



}