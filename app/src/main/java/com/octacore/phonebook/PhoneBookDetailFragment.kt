package com.octacore.phonebook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.octacore.phonebook.databinding.FragmentPhoneBookDetailBinding

class PhoneBookDetailFragment : Fragment() {
    private lateinit var binding: FragmentPhoneBookDetailBinding
    private val args: PhoneBookDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.book != null) {
            binding.content.book = args.book
        }

        binding.content.button.setOnClickListener {
            val firstName = binding.content.firstName.text.toString().trim()
            val lastName = binding.content.lastName.text.toString().trim()
            val phoneNumber = binding.content.phoneNumber.text.toString().trim()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && phoneNumber.isNotEmpty()) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "key",
                    PhoneBook(args.position, firstName, lastName, phoneNumber)
                )

                findNavController().navigateUp()
            }
        }
    }
}