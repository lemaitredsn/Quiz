package ru.lemaitre.quiz.model

class User(
    var userName: String,
    var password: String,
    var email: String
) {
    constructor() : this("", "", "") {
    }
}
