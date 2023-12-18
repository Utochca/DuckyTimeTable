package com.android.duckytimetable

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val name: String,
    val description: String
)