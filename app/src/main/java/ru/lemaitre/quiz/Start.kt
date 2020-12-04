package ru.lemaitre.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_start.*
import ru.lemaitre.quiz.common.Common
import ru.lemaitre.quiz.model.Question
import java.util.*

class Start : AppCompatActivity() {
    lateinit var database: FirebaseDatabase
    lateinit var questions: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        database = FirebaseDatabase.getInstance()
        questions = database.getReference("Questions")

        loadQuestions(Common.categoryId)

        btnPlay.setOnClickListener{
            val intent = Intent(Start@this, Playing::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadQuestions(categoryId: String) {
        if(Common.questionList.size > 0)
            Common.questionList.clear()

        questions.orderByChild("CategoryId").equalTo(categoryId)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    for (dataSnap: DataSnapshot in dataSnapshot?.children!!){
                        val ques = dataSnap.getValue(Question::class.java)
                        Common.questionList.add(ques)
                    }

                }

                override fun onCancelled(p0: DatabaseError?) {

                }

            })

        Common.questionList.shuffle()
    }
}