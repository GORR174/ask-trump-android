package ru.catstack.asktrump.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdRequest
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*
import ru.catstack.asktrump.enums.Answers
import ru.catstack.asktrump.repositories.IAnswersRepo
import ru.catstack.asktrump.repositories.RandomAnswersRepo

enum class AskTrumpFragmentState {
    NO, YES, DUNNO, LOADING, NO_QUESTION
}

class AskTrumpViewModel : ViewModel() {
    private val answersRepo: IAnswersRepo = RandomAnswersRepo()
    val state = BehaviorSubject.createDefault(AskTrumpFragmentState.NO_QUESTION)!!

    fun getAnswer() {
        CoroutineScope(Dispatchers.Main).launch {
            state.onNext(AskTrumpFragmentState.LOADING)
            val answer = answersRepo.getAnswer()

            state.onNext(
                when (answer) {
                    Answers.YES -> AskTrumpFragmentState.YES
                    Answers.NO -> AskTrumpFragmentState.NO
                    Answers.IDK -> AskTrumpFragmentState.DUNNO
                }
            )
        }
    }

    fun textFieldChanged() {
        if (state.value != AskTrumpFragmentState.NO_QUESTION)
            state.onNext(AskTrumpFragmentState.NO_QUESTION)
    }

    fun loadAd() {
        val adRequest = AdRequest.Builder().build()
    }
}