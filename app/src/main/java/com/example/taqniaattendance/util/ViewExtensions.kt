package com.example.taqniaattendance.util

import android.content.Context
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.view.*
import java.util.*

fun View.onClick(body : (() -> Unit)?){
    this.setOnClickListener { body?.invoke()}
}

fun TextInputEditText.addClearTextListener(body :(() -> Unit)?) {
    this.setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (Locale.getDefault().language == Constants.GeneralKeys.LANGUAGE_EN) {
                if(event?.getAction() == MotionEvent.ACTION_UP && event?.x >= (this@addClearTextListener.right - this@addClearTextListener.getTotalPaddingRight())) {
                        this@addClearTextListener.text?.clear()
                        body?.invoke()
                        return true;
                }
            } else {
                event?.x?.let {
                    if (it <= this@addClearTextListener.getTotalPaddingLeft()){
                        this@addClearTextListener.text?.clear()
                        body?.invoke()
                        return true;
                    }
                }
            }
            return false
        }
    })
}

fun View.show() : View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.showIf(body: () -> Boolean) : View {
    if (visibility != View.VISIBLE && body()) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.hide() : View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

 fun View.hideIf(body: () -> Boolean) : View {
    if (visibility != View.INVISIBLE && body()) {
        visibility = View.INVISIBLE
    }
    return this
}

fun View.remove() : View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

inline fun View.removeIf(body: () -> Boolean) : View {
    if (visibility != View.GONE && body()) {
        visibility = View.GONE
    }
    return this
}

fun AppCompatTextView.showText(msg: String) {
    this.visibility = View.VISIBLE
    this.text = msg
}

fun AppCompatTextView.setVisibilityGone() {
    this.visibility = View.GONE
    this.text = ""
}

fun AppCompatTextView.setVisibilityInvisible() {
    this.visibility = View.INVISIBLE
    this.text = ""
}

fun View.toggleVisibility() : View {
    visibility = if (visibility == View.VISIBLE) View.INVISIBLE else View.INVISIBLE
    return this
}

fun TextView.setBold() {
    setTypeface(typeface, Typeface.BOLD)
}

fun BottomSheetBehavior<out View>.collapse() {
    this.state = BottomSheetBehavior.STATE_COLLAPSED
}

fun BottomSheetBehavior<out View>.expand() {
    this.state = BottomSheetBehavior.STATE_EXPANDED
}

fun BottomSheetBehavior<out View>.hide() {
    this.state = BottomSheetBehavior.STATE_HIDDEN
}

fun View.enable(): View {
    isEnabled = true
    return this
}

fun View.disable(): View {
    isEnabled = false
    return this
}

fun View.disableWithChildes(): View {
    isEnabled = false
    if (this is ViewGroup) {
        for (i: Int in 0..childCount) {
            getChildAt(i)?.disableWithChildes()
        }
    }
    return this
}

fun View.enableWithChildes(): View {
    isEnabled = true
    if (this is ViewGroup) {
        for (i: Int in 0..childCount) {
            getChildAt(i)?.enableWithChildes()
        }
    }
    return this
}

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        show()
    }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(

    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<String?>>,
    timeLength: Int
) {


    snackbarEvent.observe(lifecycleOwner, androidx.lifecycle.Observer { event ->
        event.getContentIfNotHandled()?.let {
            if (it.isNullOrBlank())
                return@let
                hideKeyboard()
                showSnackbar(it, timeLength)
        }
    })

}