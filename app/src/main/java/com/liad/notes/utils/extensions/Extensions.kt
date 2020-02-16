package com.liad.notes.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.liad.notes.R

fun changeFragment(
    fragmentManager: FragmentManager, @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false
) {
    val transaction = fragmentManager.beginTransaction()
    if (addToBackStack) transaction.addToBackStack(null)
    transaction.setCustomAnimations(
        R.anim.abc_fade_in,
        R.anim.abc_shrink_fade_out_from_bottom,
        R.anim.abc_grow_fade_in_from_bottom,
        R.anim.abc_popup_exit
    )
    transaction.replace(containerId, fragment, fragment::class.java.simpleName).commit()
}

fun toast(context: Context , message: String, duration : Int = Toast.LENGTH_SHORT){
    Toast.makeText(context , message , duration).show()
}