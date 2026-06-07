package com.example.mylab_03

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnalyticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        val sb = StringBuilder()

        // Загальна статистика
        val totalPlays = AppData.allTracks.sumOf { it.playCount }
        val totalReviews = AppData.reviews.size
        sb.appendLine("=== Загальна статистика ===")
        sb.appendLine("Всього прослуховувань: $totalPlays")
        sb.appendLine("Всього відгуків: $totalReviews")
        sb.appendLine("В «Пізніше» додано: ${AppData.listenLater.size} треків")
        sb.appendLine()

        // Топ треків
        sb.appendLine("=== Топ 3 треки за прослуховуваннями ===")
        AppData.allTracks
            .sortedByDescending { it.playCount }
            .take(3)
            .forEachIndexed { i, t ->
                sb.appendLine("${i + 1}. ${t.title} — ${t.playCount} разів")
            }
        sb.appendLine()

        // Жанри
        sb.appendLine("=== Прослуховування за жанрами ===")
        AppData.allTracks
            .groupBy { it.genre }
            .map { (genre, tracks) -> genre to tracks.sumOf { it.playCount } }
            .sortedByDescending { it.second }
            .forEach { (genre, count) ->
                sb.appendLine("$genre: $count прослуховувань")
            }
        sb.appendLine()

        // Активність користувачів
        sb.appendLine("=== Відгуки по користувачах ===")
        AppData.users.forEach { user ->
            val count = AppData.reviews.count { it.userId == user.id }
            sb.appendLine("${user.name}: $count відгуків")
        }

        findViewById<TextView>(R.id.tvStats).text = sb.toString()
    }
}