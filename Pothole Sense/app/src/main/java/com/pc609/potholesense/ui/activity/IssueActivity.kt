package com.pc609.potholesense.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pc609.potholesense.R
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton

class IssueActivity : AppCompatActivity() {

    private lateinit var fabAddStory: FloatingActionButton
    private lateinit var predictLayout: LinearLayout
    private lateinit var profileLayout: LinearLayout
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var dateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue)
        supportActionBar?.setTitle(R.string.issue_activity_issue)

        fabAddStory = findViewById(R.id.fab_add_issue)
        predictLayout = findViewById(R.id.predictLL)
        profileLayout = findViewById(R.id.profileLL)
        imageView = findViewById(R.id.imageView)
        titleTextView = findViewById(R.id.titleTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        dateTextView = findViewById(R.id.dateTextView)

        imageView.setImageResource(R.drawable.berlubang)
        titleTextView.setText(R.string.judul)
        descriptionTextView.setText(R.string.deskripsi)
        dateTextView.setText(R.string.date)

        profileLayout.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        predictLayout.setOnClickListener {
            val intent = Intent(this, PredictActivity::class.java)
            startActivity(intent)
        }
    }
}
