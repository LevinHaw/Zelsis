package com.submission.zelsis.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.submission.zelsis.R

class EmailEditText: AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // not do anything
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val email = s.toString().trim()
                val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+"
                val isValidEmail = email.matches(emailPattern.toRegex())
                if (!isValidEmail) {
                    setError(context.getString(R.string.invalid_email_format), null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //not do anything
            }
        })
    }
}