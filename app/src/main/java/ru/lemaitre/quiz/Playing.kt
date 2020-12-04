package ru.lemaitre.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_playing.*
import ru.lemaitre.quiz.common.Common

class Playing : AppCompatActivity(), View.OnClickListener {
    var progressValue:Int = 0
    lateinit var mCountDownTimer:CountDownTimer

    var index = 0
    var score = 0
    var thisQuestion = 0
    var totalQuestion = 0
    var correctAnswer = 0


    lateinit var mProgressBar:ProgressBar
    lateinit var mQuestion_image:ImageView
    lateinit var btnA:Button
    lateinit var btnB:Button
    lateinit var btnC:Button
    lateinit var btnD:Button
    lateinit var txtScore:TextView
    lateinit var txtQuestionNum:TextView
    lateinit var questions_text:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing)

        txtScore = textScore
        txtQuestionNum = txtTotalQuestion
        questions_text = question_text

        mProgressBar = progressBar
        mQuestion_image = question_image


        btnA = btnAnswerA
        btnB = btnAnswerB
        btnC = btnAnswerC
        btnD = btnAnswerD

        btnA.setOnClickListener(this)
        btnB.setOnClickListener(this)
        btnC.setOnClickListener(this)
        btnD.setOnClickListener(this)

    }

    companion object{
        const val INTERVAL:Long = 1000 //1 sec
        const val TIMEOUT:Long = 7000 //7 sec

    }

    override fun onClick(v: View?) {
        mCountDownTimer.cancel()
        if(index < totalQuestion){
            val clickedButton = v as Button
            if(clickedButton.text.equals(Common.questionList.get(index).CorrectAnswer)){
                //Choose correct answer
                score+=10
                correctAnswer++
                showQuestion(++index)
            }else{
                //Choose wrong answer
                val intent = Intent(this, Done::class.java)
                val dataSend = Bundle()
                dataSend.putInt("SCORE", score)
                dataSend.putInt("TOTAL", totalQuestion)
                dataSend.putInt("CORRECT", correctAnswer)
                intent.putExtras(dataSend)
                startActivity(intent)
                finish()
            }

            txtScore.setText(String.format("%d", score))
        }
    }

    private fun showQuestion(i: Int) {
        if(index < totalQuestion){
            thisQuestion++
            txtQuestionNum.setText(String.format("%d / %d", thisQuestion, totalQuestion))
            mProgressBar.setProgress(0)
            progressValue = 0
            if(Common.questionList.get(index).IsImageQuestion.equals("true")){
                //if is image
                Picasso.with(baseContext)
                    .load(Common.questionList.get(index).Question)
                    .into(mQuestion_image)
                mQuestion_image.visibility = View.VISIBLE
                questions_text.visibility = View.INVISIBLE
            }else{
                //if is text
                questions_text.setText(Common.questionList.get(index).Question)

                questions_text.visibility = View.VISIBLE
                mQuestion_image.visibility = View.INVISIBLE


            }
            btnA.setText(Common.questionList[index].AnswerA)
            btnB.setText(Common.questionList[index].AnswerB)
            btnC.setText(Common.questionList[index].AnswerC)
            btnD.setText(Common.questionList[index].AnswerD)

            mCountDownTimer.start()
        }else{
            val intent = Intent(this, Done::class.java)
            val dataSend = Bundle()
            dataSend.putInt("SCORE", score)
            dataSend.putInt("TOTAL", totalQuestion)
            dataSend.putInt("CORRECT", correctAnswer)
            intent.putExtras(dataSend)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        totalQuestion = Common.questionList.size

        mCountDownTimer = object: CountDownTimer(TIMEOUT, INTERVAL){
            override fun onTick(millisUntilFinished: Long) {
                mProgressBar.setProgress(progressValue)
                progressValue++
            }

            override fun onFinish() {
                mCountDownTimer.cancel()
                showQuestion(++index)
            }
        }
        showQuestion(index)

    }
}
