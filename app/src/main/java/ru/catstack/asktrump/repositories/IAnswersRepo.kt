package ru.catstack.asktrump.repositories

import ru.catstack.asktrump.enums.Answers

interface IAnswersRepo {
    suspend fun getAnswer(): Answers
}