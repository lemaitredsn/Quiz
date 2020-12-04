package ru.lemaitre.quiz


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import ru.lemaitre.quiz.`interface`.RankingCallback
import ru.lemaitre.quiz.common.Common
import ru.lemaitre.quiz.model.QuestionScore
import ru.lemaitre.quiz.model.Ranking


class RankingFragment private constructor() : Fragment() {
    lateinit var myFragment: View

    lateinit var database: FirebaseDatabase
    lateinit var questionScore: DatabaseReference
    lateinit var rankingTbl: DatabaseReference

    var sum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance()
        questionScore = database.getReference("Question_Score")
        rankingTbl = database.getReference("Ranking")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myFragment = inflater.inflate(R.layout.fragment_ranking, container, false)

        updateScore(Common.currentUser!!.userName, object : RankingCallback<Ranking>{
            override fun callback(ranking: Ranking) {
                rankingTbl.child(ranking.userName)
                    .setValue(ranking)
                showRanking()
            }
        })

        return myFragment

    }

    private fun showRanking(){
        9-00
    }

    private fun updateScore(userName: String, callback: RankingCallback<Ranking>) {
        questionScore.orderByChild("user").equalTo(userName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapShot: DataSnapshot?) {
                    for (data in dataSnapShot!!.children) {
                        val ques = data.getValue(QuestionScore::class.java)
                        sum += (ques.Score).toInt()
                    }
                    val ranking = Ranking(userName, sum.toLong())
                    callback.callback(ranking)
                }

                override fun onCancelled(p0: DatabaseError?) {

                }
            })
    }

    companion object {
        fun newInstance(): RankingFragment {
            return RankingFragment()
        }
    }
}