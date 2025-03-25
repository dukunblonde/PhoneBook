package com.example.phonebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private var contactList: MutableList<Contact>,
    private val listener: OnContactClickListener
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val phone: TextView = view.findViewById(R.id.tvPhone)
        val email: TextView = view.findViewById(R.id.tvEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_list_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]
        holder.name.text = contact.name
        holder.phone.text = contact.phone
        holder.email.text = contact.email

        holder.itemView.setOnClickListener {
            listener.onContactClick(contact)
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun updateContact(updatedContact: Contact) {
        val index = contactList.indexOfFirst { it.id == updatedContact.id }
        if (index != -1) {
            contactList[index] = updatedContact
            notifyItemChanged(index)
        }
    }

    fun deleteContact(contactId: String) {
        val index = contactList.indexOfFirst { it.id == contactId }
        if (index != -1) {
            contactList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}

interface OnContactClickListener {
    fun onContactClick(contact: Contact)
}
