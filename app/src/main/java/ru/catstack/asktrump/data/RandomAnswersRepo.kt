package ru.catstack.asktrump.data

import ru.catstack.asktrump.enums.Answers
import kotlin.random.Random
import kotlin.random.nextInt

class RandomAnswersRepo : IAnswersRepo {
    override fun getRandomAnswer(): Answers {
        return when (Random.nextInt(1..21)) {
            in 1..10 -> Answers.NO
            in 11..20 -> Answers.YES
            else -> Answers.IDK
        }
    }
}
