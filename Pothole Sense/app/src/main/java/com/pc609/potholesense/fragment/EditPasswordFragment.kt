package com.pc609.potholesense.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.pc609.potholesense.R

class EditPasswordFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSavePassword = view.findViewById<Button>(R.id.btnSavePassword)
        val etEditPassword = view.findViewById<EditText>(R.id.etEditPassword)

        btnSavePassword.setOnClickListener {
            val newPassword = etEditPassword.text.toString().trim()
            // Save the new password to the database or perform any necessary action
        }
    }
}
