package com.voyager177.core.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voyager177.R
import com.voyager177.core.contacts.Contact
import com.voyager177.core.contacts.ContactsRepository
import com.voyager177.core.navigation.Dialer

@Composable
fun ContactsScreen(
    contactsRepository: ContactsRepository,
    dialer: Dialer,
    hasContactsPermission: Boolean,
    onRequestContactsPermission: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (!hasContactsPermission) onRequestContactsPermission()
    }

    if (!hasContactsPermission) {
        PermissionRequiredMessage()
        return
    }

    val contacts = remember {
        contactsRepository.getContacts()
    }

    val contactsFoundMessage = pluralStringResource(
        R.plurals.contacts_found,
        contacts.size,
        contacts.size
    )

    LaunchedEffect(contacts.size) {
        Toast.makeText(
            context,
            contactsFoundMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    if (contacts.isEmpty()) {
        EmptyContactsMessage()
        return
    }

    ContactsList(
        contacts = contacts,
        onContactClick = {
            contact -> dialer.open(contact.phoneNumber)
        }
    )
}

@Composable
private fun PermissionRequiredMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Text(text = stringResource(R.string.contacts_permission_required))
    }
}

@Composable
private fun EmptyContactsMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Text(text = stringResource(R.string.no_contacts_found))
    }
}

@Composable
private fun ContactsList(contacts: List<Contact>, onContactClick: (Contact) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        items(
            items = contacts
        ) { contact ->
            ContactItem(
                contact = contact,
                onClick = {
                    onContactClick(contact)
                }
            )
        }
    }
}

@Composable
private fun ContactItem(contact: Contact, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
    ) {
        Text(text = contact.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = contact.phoneNumber, fontSize = 16.sp)
    }
}