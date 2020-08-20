package com.octacore.phonebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.octacore.phonebook.databinding.FragmentPhoneBookBinding

class PhoneBookFragment : Fragment(), PhoneBookAdapter.OnItemClickListener {
    private lateinit var binding: FragmentPhoneBookBinding
    private lateinit var adapter: PhoneBookAdapter
    private var phoneList = ArrayList<PhoneBook>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (phoneList.isEmpty()) {
            phoneList.add(PhoneBook(0, "John", "Doe", "08012345678"))
            phoneList.add(PhoneBook(1, "Jane", "Doe", "08102345678"))
        }

        adapter = PhoneBookAdapter(phoneList, this)

        val recyclerView = binding.content.phoneBookList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<PhoneBook>("key")
            ?.observe(viewLifecycleOwner) { book ->
                if (book != null) {
                    when (checkPhoneNumber(phoneList, book)) {
                        DEFAULT -> {
                            phoneList.add(book.id, book)
                            adapter.notifyDataSetChanged()
                        }
                        EXISTING_PHONE_NUMBER -> {
                            displayDialogForNonUniquePhoneNumber(book)
                        }
                        EXISTING_CONTACT_BUT_UPDATE -> {
                            displayDialogForExistingContact(book)
                        }
                        EXISTING_CONTACT -> {

                        }
                    }
                }
            }

        binding.fab.setOnClickListener {
            val position = adapter.itemCount
            findNavController().navigate(
                PhoneBookFragmentDirections.actionPhoneBookFragmentToPhoneBookDetailFragment(
                    position = position
                )
            )
        }
    }

    override fun onItemClick(book: PhoneBook) {
        findNavController().navigate(
            PhoneBookFragmentDirections.actionPhoneBookFragmentToPhoneBookDetailFragment(
                position = book.id,
                book = book
            )
        )
    }

    private fun checkPhoneNumber(list: ArrayList<PhoneBook>, contact: PhoneBook): Int {
        for (book in list) {
            if (book.id == contact.id) {
                return if (book == contact)
                    EXISTING_CONTACT
                else
                    EXISTING_CONTACT_BUT_UPDATE
            } else if (book.phoneNumber == contact.phoneNumber)
                return EXISTING_PHONE_NUMBER
        }
        return DEFAULT
    }

    private fun displayDialogForNonUniquePhoneNumber(book: PhoneBook) {
        AlertDialog.Builder(requireContext()).also {
            it.setTitle("Existing Phone Number")
            it.setMessage("The phone number you're trying enter already exists. ")
            it.setPositiveButton("Edit") { dialog, _ ->
                findNavController().navigate(
                    PhoneBookFragmentDirections.actionPhoneBookFragmentToPhoneBookDetailFragment(
                        position = book.id,
                        book = book
                    )
                )
                dialog.dismiss()
            }
            it.setNeutralButton("Discard") { dialog, _ ->
                dialog.cancel()
            }
        }.apply {
            val dialog = this.create()
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun displayDialogForExistingContact(book: PhoneBook) {
        AlertDialog.Builder(requireContext()).also {
            it.setTitle("Update Phone Book")
            it.setMessage("Do you want to update this contact?")
            it.setPositiveButton("Update") { dialog, _ ->
                phoneList.removeAt(book.id)
                phoneList.add(book.id, book)
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            it.setNeutralButton("Discard") { dialog, _ ->
                dialog.cancel()
            }
        }.apply {
            val dialog = this.create()
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    companion object {
        private const val DEFAULT = -1
        private const val EXISTING_CONTACT = 0
        private const val EXISTING_CONTACT_BUT_UPDATE = 1
        private const val EXISTING_PHONE_NUMBER = 2
    }
}