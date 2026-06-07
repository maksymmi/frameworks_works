package com.example.mylab_03

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {

    private var isPlaying = false
    private var progress = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var currentTrack: Track
    private var selectedRating = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val trackId = intent.getIntExtra("track_id", 1)
        currentTrack = AppData.allTracks.first { it.id == trackId }

        // Заповнення UI
        findViewById<TextView>(R.id.tvPlayerTitle).text = currentTrack.title
        findViewById<TextView>(R.id.tvPlayerArtist).text = currentTrack.artist
        findViewById<TextView>(R.id.tvPlayerGenre).text = "${currentTrack.genre} • ${currentTrack.album}"

        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val btnPlayPause = findViewById<Button>(R.id.btnPlayPause)

        // Кнопка Play/Pause
        btnPlayPause.setOnClickListener {
            isPlaying = !isPlaying
            btnPlayPause.text = if (isPlaying) "⏸" else "▶"
            if (isPlaying) startProgress(seekBar)
        }

        // Завантаження (імітація)
        findViewById<Button>(R.id.btnDownload).setOnClickListener {
            Toast.makeText(this, "⬇ Завантаження «${currentTrack.title}»...", Toast.LENGTH_LONG).show()
        }

        // Відгук
        val etReview = findViewById<EditText>(R.id.etReview)

        listOf(
            Pair(R.id.btnRate1, 1),
            Pair(R.id.btnRate3, 3),
            Pair(R.id.btnRate5, 5)
        ).forEach { (btnId, rating) ->
            findViewById<Button>(btnId).setOnClickListener {
                selectedRating = rating
                val reviewText = etReview.text.toString()
                if (reviewText.isBlank()) {
                    Toast.makeText(this, "Напишіть відгук перед оцінкою", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val user = AppData.currentUser ?: return@setOnClickListener
                AppData.reviews.add(Review(currentTrack.id, user.id, reviewText, rating))
                Toast.makeText(this, "Відгук збережено! Оцінка: $rating★", Toast.LENGTH_SHORT).show()
                etReview.text.clear()
            }
        }

        // Попередній/наступний трек
        findViewById<Button>(R.id.btnPrev).setOnClickListener { navigateTrack(-1) }
        findViewById<Button>(R.id.btnNext).setOnClickListener { navigateTrack(1) }

        // Профіль
        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun startProgress(seekBar: SeekBar) {
        val runnable = object : Runnable {
            override fun run() {
                if (isPlaying && progress < 100) {
                    progress++
                    seekBar.progress = progress
                    handler.postDelayed(this, 300)
                } else if (progress >= 100) {
                    isPlaying = false
                    progress = 0
                    seekBar.progress = 0
                    findViewById<Button>(R.id.btnPlayPause).text = "▶"
                }
            }
        }
        handler.post(runnable)
    }

    private fun navigateTrack(direction: Int) {
        val index = AppData.allTracks.indexOfFirst { it.id == currentTrack.id }
        val newIndex = (index + direction + AppData.allTracks.size) % AppData.allTracks.size
        currentTrack = AppData.allTracks[newIndex]
        currentTrack.playCount++ // Рівень 4
        isPlaying = false
        progress = 0
        findViewById<SeekBar>(R.id.seekBar).progress = 0
        findViewById<Button>(R.id.btnPlayPause).text = "▶"
        findViewById<TextView>(R.id.tvPlayerTitle).text = currentTrack.title
        findViewById<TextView>(R.id.tvPlayerArtist).text = currentTrack.artist
        findViewById<TextView>(R.id.tvPlayerGenre).text = "${currentTrack.genre} • ${currentTrack.album}"
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}