package com.xayah.design.adapter

import android.widget.CompoundButton
import androidx.databinding.BindingAdapter
import com.xayah.design.preference.Clickable
import com.xayah.design.preference.EditableText
import com.xayah.design.preference.SelectableList
import com.xayah.design.preference.Switch

class ViewAdapter {
    companion object {
        @BindingAdapter("items")
        @JvmStatic
        fun setItems(v: SelectableList, array: Array<String>?) {
            if (array != null) {
                v.setItems(array)
            }
        }

        @BindingAdapter("onConfirm")
        @JvmStatic
        fun setSelectableListOnConfirmListener(
            v: SelectableList,
            listener: ((v: SelectableList, choice: Int) -> Unit)
        ) {
            v.setOnConfirmListener(listener)
        }

        @BindingAdapter("onConfirm")
        @JvmStatic
        fun setEditableTextOnConfirmListener(
            v: EditableText,
            listener: ((v: EditableText, content: CharSequence?) -> Unit)
        ) {
            v.setOnConfirmListener(listener)
        }

        @BindingAdapter("onCheckedChange")
        @JvmStatic
        fun setOnCheckedChangeListener(
            v: Switch,
            listener: ((buttonView: CompoundButton, isChecked: Boolean) -> Unit)
        ) {
            v.setOnCheckedChangeListener(listener)
        }

        @BindingAdapter("isRound")
        @JvmStatic
        fun setRound(view: Clickable, isRound: Boolean) {
            view.setRound(isRound)
        }
    }
}