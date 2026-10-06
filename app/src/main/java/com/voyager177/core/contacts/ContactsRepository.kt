package com.voyager177.core.contacts

interface ContactsRepository {
    fun getContacts(): List<Contact>
}