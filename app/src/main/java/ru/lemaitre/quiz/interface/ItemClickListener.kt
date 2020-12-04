package ru.lemaitre.quiz.`interface`

import android.view.View

interface ItemClickListener {
    fun onClick(view: View, position:Int, isLongClick:Boolean)
}