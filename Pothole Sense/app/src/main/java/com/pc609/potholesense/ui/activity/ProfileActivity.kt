package com.pc609.potholesense.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.pc609.potholesense.R
import com.pc609.potholesense.fragment.EditNameFragment
import com.pc609.potholesense.ui.SharedPreferencesHelper
import com.pc609.potholesense.ui.viewmodel.ProfileViewModel
import com.pc609.potholesense.ui.viewmodel.ViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var editNameButton: Button
    private lateinit var editEmailButton: Button
    private lateinit var editPasswordButton: Button
    private lateinit var logoutButton: Button
    private lateinit var viewModel: ProfileViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()

        nameTextView = findViewById(R.id.tvProfileName)
        editNameButton = findViewById(R.id.btnEditName)
        editEmailButton = findViewById(R.id.btnEditEmail)
        editPasswordButton = findViewById(R.id.btnChangePassword)
        logoutButton = findViewById(R.id.btnLogout)

        auth = FirebaseAuth.getInstance()
        viewModel =
            ViewModelProvider(this, ViewModelFactory(this, SharedPreferencesHelper(this))).get(
                ProfileViewModel::class.java
            )

        viewModel.user.observe(this, Observer { user ->
            nameTextView.text = user.name
        })

        editNameButton.setOnClickListener {
            val editNameFragment = EditNameFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, editNameFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

//        editEmailButton.setOnClickListener {
//            val editEmailFragment = EditEmailFragment()
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragmentContainer, editEmailFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//        }
//
//        editPasswordButton.setOnClickListener {
//            val editPasswordFragment = EditPasswordFragment()
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragmentContainer, editPasswordFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//        }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}


