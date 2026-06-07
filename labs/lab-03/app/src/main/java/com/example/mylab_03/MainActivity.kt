package com.example.mylab_03

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// Модель треку
data class Track(
    val id: Int,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    // Лічильник прослуховувань
    var playCount: Int = 0,
    var listenLaterCount: Int = 0
)

// Глобальні дані
object AppData {
    val allTracks = mutableListOf(
        Track(1, "Blinding Lights", "The Weeknd", "After Hours", "Pop"),
        Track(2, "Bohemian Rhapsody", "Queen", "A Night at the Opera", "Rock"),
        Track(3, "Shape of You", "Ed Sheeran", "÷", "Pop"),
        Track(4, "Smells Like Teen Spirit", "Nirvana", "Nevermind", "Rock"),
        Track(5, "God's Plan", "Drake", "Scorpion", "Hip-Hop"),
        Track(6, "Rolling in the Deep", "Adele", "21", "Soul"),
        Track(7, "Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV", "Rock"),
        Track(8, "Bad Guy", "Billie Eilish", "When We All Fall Asleep", "Pop"),
        Track(9, "HUMBLE.", "Kendrick Lamar", "DAMN.", "Hip-Hop"),
        Track(10, "Someone Like You", "Adele", "21", "Soul")
    )

    val listenLater = mutableListOf<Track>()

    // Користувачі
    val users = mutableListOf(
        User(1, "student_01", "Іван Іваненко"),
        User(2, "music_fan", "Марія Коваль")
    )
    var currentUser: User? = users[0]

    // Відгуки
    val reviews = mutableListOf<Review>()
}

// Моделі
data class User(val id: Int, val username: String, val name: String)
data class Review(val trackId: Int, val userId: Int, val text: String, val rating: Int)

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter
    private var showingLater = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnAnalytics).setOnClickListener {
            startActivity(Intent(this, AnalyticsActivity::class.java))
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnShowAll = findViewById<Button>(R.id.btnShowAll)
        val btnShowLater = findViewById<Button>(R.id.btnShowLater)

        showAllTracks()

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().lowercase()
            val filtered = AppData.allTracks.filter {
                it.title.lowercase().contains(query) ||
                        it.artist.lowercase().contains(query) ||
                        it.album.lowercase().contains(query) ||
                        it.genre.lowercase().contains(query)
            }
            adapter.updateTracks(filtered)
            showingLater = false
        }

        btnShowAll.setOnClickListener {
            showAllTracks()
        }

        btnShowLater.setOnClickListener {
            showingLater = true
            adapter.updateTracks(AppData.listenLater)
            updateLaterButton()
        }
    }

    private fun showAllTracks() {
        showingLater = false
        adapter = TrackAdapter(AppData.allTracks.toMutableList()) { track, action ->
            when (action) {
                "later" -> addToLater(track)
                "play" -> openPlayer(track)
            }
        }
        recyclerView.adapter = adapter
    }

    private fun addToLater(track: Track) {
        if (AppData.listenLater.none { it.id == track.id }) {
            AppData.listenLater.add(track)
            track.listenLaterCount++
            Toast.makeText(this, "«${track.title}» додано до «Пізніше»", Toast.LENGTH_SHORT).show()
            updateLaterButton()
        } else {
            Toast.makeText(this, "Вже є у списку «Пізніше»", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openPlayer(track: Track) {
        track.playCount++
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("track_id", track.id)
        startActivity(intent)
    }

    private fun updateLaterButton() {
        findViewById<Button>(R.id.btnShowLater).text = "Пізніше (${AppData.listenLater.size})"
    }
}

// RecyclerView Adapter
class TrackAdapter(
    private var tracks: MutableList<Track>,
    private val onAction: (Track, String) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
        val tvGenre: TextView = view.findViewById(R.id.tvGenre)
        val btnLater: Button = view.findViewById(R.id.btnLater)
        val btnPlay: Button = view.findViewById(R.id.btnPlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.tvTitle.text = track.title
        holder.tvArtist.text = track.artist
        holder.tvGenre.text = "${track.genre} • ${track.album}"
        holder.btnLater.setOnClickListener { onAction(track, "later") }
        holder.btnPlay.setOnClickListener { onAction(track, "play") }
    }

    override fun getItemCount() = tracks.size

    fun updateTracks(newTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }
}