package ru.catstack.asktrump.repositories

import kotlinx.coroutines.delay
import ru.catstack.asktrump.enums.Answers
import kotlin.random.Random
import kotlin.random.nextInt

class RandomAnswersRepo : IAnswersRepo {
    override suspend fun getAnswer(): Answers {
        val randomValue = Random.nextInt(1..21)

        delay(2500)

        return when (randomValue) {
            in 1..10 -> Answers.NO
            in 11..20 -> Answers.YES
            else -> Answers.IDK
        }
    }
}
