package com.pc609.potholesense.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.pc609.potholesense.R

class EditEmailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSaveEmail = view.findViewById<Button>(R.id.btnSaveEmail)
        val etEditEmail = view.findViewById<EditText>(R.id.etEditEmail)

        btnSaveEmail.setOnClickListener {
            val newEmail = etEditEmail.text.toString().trim()

        }
    }
}
