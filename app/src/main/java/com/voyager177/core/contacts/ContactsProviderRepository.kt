package com.voyager177.core.contacts

import android.content.Context
import android.provider.ContactsContract

class ContactsProviderRepository(private val context: Context) : ContactsRepository {
    override fun getContacts(): List<Contact> {
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        ).use { cursor ->
            if (cursor == null) return emptyList()

            val nameColumnIndex = cursor.getColumnIndexOrThrow(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            val phoneNumberColumnIndex = cursor.getColumnIndexOrThrow(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            val contacts = ArrayList<Contact>()

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameColumnIndex) ?: "N/A"
                val phoneNumber = cursor.getString(phoneNumberColumnIndex) ?: "N/A"
                contacts.add(Contact(name = name, phoneNumber = phoneNumber))
            }

            return contacts
        }
    }
}