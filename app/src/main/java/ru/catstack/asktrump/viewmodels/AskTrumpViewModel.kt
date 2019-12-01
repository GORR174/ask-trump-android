package ru.catstack.asktrump.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdRequest
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*
import ru.catstack.asktrump.enums.Answers
import ru.catstack.asktrump.repositories.IAnswersRepo
import ru.catstack.asktrump.repositories.RandomAnswersRepo

enum class TrumpImageState {
    NO, YES, DUNNO, LOADING, NO_QUESTION
}

class AskTrumpViewModel : ViewModel() {
    private val answersRepo: IAnswersRepo = RandomAnswersRepo()
    val state = BehaviorSubject.createDefault(TrumpImageState.NO_QUESTION)!!
    val adRequest = BehaviorSubject.create<AdRequest>()!!

    fun getAnswer() {
        CoroutineScope(Dispatchers.Main).launch {
            state.onNext(TrumpImageState.LOADING)
            val answer = answersRepo.getAnswer()

            state.onNext(
                when (answer) {
                    Answers.YES -> TrumpImageState.YES
                    Answers.NO -> TrumpImageState.NO
                    Answers.IDK -> TrumpImageState.DUNNO
                }
            )
        }
    }

    fun textFieldChanged() {
        if (state.value != TrumpImageState.NO_QUESTION)
            state.onNext(TrumpImageState.NO_QUESTION)
    }

    fun loadAd() {
        adRequest.onNext(AdRequest.Builder().build())
    }
}