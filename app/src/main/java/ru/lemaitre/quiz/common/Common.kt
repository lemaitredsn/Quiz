package ru.lemaitre.quiz.common

import ru.lemaitre.quiz.model.Question
import ru.lemaitre.quiz.model.User

class Common {
    companion object{
        var categoryId:String = ""
        var currentUser:User? = null
        var questionList:MutableList<Question> = mutableListOf()
    }
}
