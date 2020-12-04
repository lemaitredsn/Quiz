package ru.lemaitre.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_done.*
import ru.lemaitre.quiz.common.Common
import ru.lemaitre.quiz.model.QuestionScore

class Done : AppCompatActivity() {
    lateinit var database: FirebaseDatabase
    lateinit var question_score: DatabaseReference

    lateinit var txtResultScore: TextView
    lateinit var getTxtResultQuestion: TextView
    lateinit var mProgressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done)

        database = FirebaseDatabase.getInstance()
        question_score = database.getReference("Question_score")

        txtResultScore = txtTotalScore
        getTxtResultQuestion = txtTotalQuestion
        mProgressBar = doneProgressBar

        btnTryAgain.setOnClickListener {
            val intent = Intent(Done@ this, Home::class.java)
            startActivity(intent)
            finish()
        }

        val extra = intent.extras
        if (extra != null) {
            val score = extra.getInt("SCORE")
            val totalQuestion = extra.getInt("TOTAL")
            val correctAnswer = extra.getInt("CORRECT")

            txtTotalScore.text = String.format("SCORE : %d", score)
            txtTotalQuestion.text = String.format("PASSED : %d / %d", correctAnswer, totalQuestion)

            doneProgressBar.max = totalQuestion
            doneProgressBar.progress = correctAnswer

            //upload point to DB
            question_score.child(
                String.format(
                    "%s_%s", Common.currentUser?.userName,
                    Common.categoryId
                )
            )
                .setValue(
                    QuestionScore(
                        String.format(
                            "%s_%s",
                            Common.currentUser?.userName,
                            Common.categoryId,
                            Common.currentUser?.userName,
                            score.toString()
                        )
                    )
                )

        }

    }
}