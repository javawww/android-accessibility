package com.example.android.basicandroidaccessibility.pojo

import java.time.LocalDate
import java.time.LocalTime

data class Examination (
        val id: Int,
        val createTime: String,
        val createDate: String,
        val question: String,
        val label: String,
        val image: String,
        val ans1: String,
        val ans2: String,
        val ans3: String,
        val ans4: String,
        val correct: Int,
        val analyze: String
)