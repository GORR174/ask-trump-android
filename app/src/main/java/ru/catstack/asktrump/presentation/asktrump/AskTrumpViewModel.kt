package ru.catstack.asktrump.presentation.asktrump

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdRequest
import kotlinx.coroutines.*
import ru.catstack.asktrump.enums.Answers
import ru.catstack.asktrump.data.IAnswersRepo
import ru.catstack.asktrump.data.RandomAnswersRepo

enum class AnswerState {
    NO, YES, DUNNO, NO_QUESTION
}

class AskTrumpViewModel : ViewModel() {
    private val answersRepo: IAnswersRepo = RandomAnswersRepo()

    private val mutableQuestionAnswer = MutableLiveData<AnswerState>()
    val questionAnswer: LiveData<AnswerState> = mutableQuestionAnswer

    private val mutableAdRequest = MutableLiveData<AdRequest>()
    val adRequest: LiveData<AdRequest> = mutableAdRequest

    init {
        mutableQuestionAnswer.value = AnswerState.NO_QUESTION
    }

    fun getAnswer(question: String) {
        if (question.isNotBlank()) {
            CoroutineScope(Dispatchers.Main).launch {
                val answer = answersRepo.getRandomAnswer()
                mutableQuestionAnswer.value = when (answer) {
                    Answers.YES -> AnswerState.YES
                    Answers.NO -> AnswerState.NO
                    Answers.IDK -> AnswerState.DUNNO
                }
            }
        }
    }

    fun textFieldChanged() {
        if (questionAnswer.value != AnswerState.NO_QUESTION)
            mutableQuestionAnswer.value = AnswerState.NO_QUESTION
    }

    fun loadAd() {
        mutableAdRequest.value = AdRequest.Builder().build()
    }
}