package ru.catstack.asktrump.presentation.asktrump

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdRequest
import kotlinx.coroutines.*
import ru.catstack.asktrump.enums.Answers
import ru.catstack.asktrump.data.IAnswersRepo
import ru.catstack.asktrump.data.RandomAnswersRepo

enum class TrumpImageState {
    NO, YES, DUNNO, LOADING, NO_QUESTION
}

class AskTrumpViewModel : ViewModel() {
    private val answersRepo: IAnswersRepo = RandomAnswersRepo()

    private val mutableState = MutableLiveData<TrumpImageState>()
    val state: LiveData<TrumpImageState> = mutableState

    private val mutableAdRequest = MutableLiveData<AdRequest>()
    val adRequest: LiveData<AdRequest> = mutableAdRequest

    fun getAnswer() {
        CoroutineScope(Dispatchers.Main).launch {
            mutableState.value = TrumpImageState.LOADING
            val answer = answersRepo.getAnswer()
            mutableState.value = when (answer) {
                Answers.YES -> TrumpImageState.YES
                Answers.NO -> TrumpImageState.NO
                Answers.IDK -> TrumpImageState.DUNNO
            }
        }
    }

    fun textFieldChanged() {
        if (state.value != TrumpImageState.NO_QUESTION)
            mutableState.value = TrumpImageState.NO_QUESTION
    }

    fun loadAd() {
        mutableAdRequest.value = AdRequest.Builder().build()
    }
}