package sung.gyun.alldeviceadaptiveapp

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData

class HaveIconEditText(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {

    private var parentLayout: ConstraintLayout
    private var editText: AppCompatEditText
    var imgView01: AppCompatImageView
    var imgView02: AppCompatImageView
    var editIdValue = MutableLiveData<Boolean>()
    var editPwValue = MutableLiveData<Boolean>()
    var editIdPwValue = MutableLiveData<MutableList<Boolean>>()

    var text: String = ""
        set(value) {
            field = value
            editText.setText(field)
        }
        get() {
            return editText.text.toString()
        }


    init {

        val inflaterService = Context.LAYOUT_INFLATER_SERVICE
        val inflater = context.getSystemService(inflaterService) as LayoutInflater
        val view = inflater.inflate(R.layout.edittext_have_icon, this, false)

        addView(view)

        parentLayout = view.findViewById(R.id.parentLayout)
        editText = view.findViewById(R.id.editText)
        imgView01 = view.findViewById(R.id.imgView_01)
        imgView02 = view.findViewById(R.id.imgView_02)

        editIdValue.value = false
        editPwValue.value = false
        editIdPwValue.value?.add(false)
        editIdPwValue.value?.add(false)
        val typedArray = context.obtainStyledAttributes(attrs!!, R.styleable.HaveIconEditText)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {

        // edittext 의 text:hint
        val hint = typedArray.getString(R.styleable.HaveIconEditText_android_hint)
        Log.e("hint", "$hint")
        editText.hint = hint

        // edittext 왼쪽 아이콘
        val icon01 = typedArray.getResourceId(R.styleable.HaveIconEditText_setIcon01, 0)
        imgView01.setImageResource(icon01)

        //edittext 의 textsize
        val textSize =
            typedArray.getDimensionPixelSize(R.styleable.HaveIconEditText_android_textSize, 0)

        //edittext 의 text color
        val textColor = typedArray.getColor(R.styleable.HaveIconEditText_android_textColor, 0)

        //edittext 의 gravity
        val gravity = typedArray.getInt(R.styleable.HaveIconEditText_android_gravity, 0)

        //edittext 의 fontfamily
        val fontFamily =
            typedArray.getResourceId(R.styleable.HaveIconEditText_android_fontFamily, 0)

        //edittext 의 inputType
        val inputType =
            typedArray.getInt(
                R.styleable.HaveIconEditText_android_inputType,
                InputType.TYPE_TEXT_VARIATION_NORMAL
            )

        //edittext 의 focusableMode
        val focusableMode =
            typedArray.getBoolean(R.styleable.HaveIconEditText_focusableInTouchMode, true)

        editText.apply {
            setHint(hint)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            setTextColor(textColor)
            setGravity(gravity)
            typeface = if (fontFamily == 0) {
                Typeface.DEFAULT
            } else {
                ResourcesCompat.getFont(context, fontFamily)
            }
            setInputType(inputType)
            isFocusableInTouchMode = focusableMode
        }

        // parentLayout 왼쪽 아이콘
        val focusedRes = typedArray.getResourceId(R.styleable.HaveIconEditText_setFocusedColor, 0)

        // parentLayout 왼쪽 아이콘
        val unfocusedRes =
            typedArray.getResourceId(R.styleable.HaveIconEditText_setUnfocusedColor, 0)

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                editText.setTextColor(Color.parseColor("#179393"))
                parentLayout.setBackgroundResource(focusedRes)
            } else {
                editText.setTextColor(Color.parseColor("#333333"))
                parentLayout.setBackgroundResource(unfocusedRes)
            }
        }

        typedArray.recycle()
    }

    inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
        getInt(index, -1).let {
            if (it >= 0) enumValues<T>()[it] else default
        }

    fun setIcon(resId: Int) {
        imgView01.setImageResource(resId)
    }

    private fun visibleIcon(visible: Boolean, isId: Boolean) {
        if (visible) {
            imgView01.visibility = View.VISIBLE
            if (!isId) imgView02.visibility = View.VISIBLE

        } else {
            imgView01.visibility = View.GONE
            if (!isId) imgView02.visibility = View.GONE
        }

    }

    fun changeInputType(show: Boolean) {
        if (show) {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            imgView02.setBackgroundResource(R.drawable.outline_visibility_off_24)
        } else {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imgView02.setBackgroundResource(R.drawable.outline_visibility_24)
        }
        editText.setSelection(text.length)


    }

    fun watchText(isId: Boolean) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length == 0) {
                    visibleIcon(visible = false, isId = isId)
                    if (isId) editIdValue.value = false
                    else editPwValue.value = false
                } else {
                    visibleIcon(visible = true, isId = isId)
                    if (isId) editIdValue.value = true
                    else editPwValue.value = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }


    fun setOnEditorKeyListener(onKeyListener: OnKeyListener) {
        editText.setOnKeyListener(onKeyListener)
    }

}