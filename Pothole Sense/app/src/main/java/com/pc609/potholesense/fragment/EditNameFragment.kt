package com.pc609.potholesense.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.pc609.potholesense.R
import com.pc609.potholesense.ui.viewmodel.ProfileViewModel

class EditNameFragment : Fragment() {
    private lateinit var etEditName: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEditName = view.findViewById(R.id.etEditName)
        val btnSaveName = view.findViewById<Button>(R.id.btnSaveName)

        btnSaveName.setOnClickListener {
            val newName = etEditName.text.toString().trim()
            if (newName.isNotEmpty()) {
                val profileViewModel =
                    ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
                profileViewModel.editName(newName)
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
            } else {
                etEditName.error = "Please enter a name"
            }
        }
    }
}
