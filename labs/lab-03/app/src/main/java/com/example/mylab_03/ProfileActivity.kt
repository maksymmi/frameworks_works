package com.example.mylab_03

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvCurrentUser = findViewById<TextView>(R.id.tvCurrentUser)
        val rvReviews = findViewById<RecyclerView>(R.id.rvReviews)
        rvReviews.layoutManager = LinearLayoutManager(this)

        fun refreshUI() {
            val user = AppData.currentUser
            tvCurrentUser.text = "Поточний: ${user?.name ?: "—"}\n(@${user?.username ?: "—"})"
            val myReviews = AppData.reviews.filter { it.userId == user?.id }
            rvReviews.adapter = ReviewAdapter(myReviews)
        }

        refreshUI()

        findViewById<Button>(R.id.btnUser1).setOnClickListener {
            AppData.currentUser = AppData.users[0]
            Toast.makeText(this, "Профіль: ${AppData.users[0].name}", Toast.LENGTH_SHORT).show()
            refreshUI()
        }

        findViewById<Button>(R.id.btnUser2).setOnClickListener {
            AppData.currentUser = AppData.users[1]
            Toast.makeText(this, "Профіль: ${AppData.users[1].name}", Toast.LENGTH_SHORT).show()
            refreshUI()
        }
    }
}

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewVH>() {

    class ReviewVH(view: View) : RecyclerView.ViewHolder(view) {
        val tvReview: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ReviewVH(view)
    }

    override fun onBindViewHolder(holder: ReviewVH, position: Int) {
        val r = reviews[position]
        val track = AppData.allTracks.firstOrNull { it.id == r.trackId }
        holder.tvReview.text = "[${"★".repeat(r.rating)}] ${track?.title ?: "?"}: ${r.text}"
    }

    override fun getItemCount() = reviews.size
}