package ru.catstack.asktrump.data

import ru.catstack.asktrump.enums.Answers

interface IAnswersRepo {
    suspend fun getAnswer(): Answers
}