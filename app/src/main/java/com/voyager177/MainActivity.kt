package com.voyager177

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.voyager177.core.contacts.ContactsProviderRepository
import com.voyager177.core.contacts.ContactsRepository
import com.voyager177.core.navigation.Dialer
import com.voyager177.core.navigation.IntentDialer
import com.voyager177.core.ui.ContactsScreen
import com.voyager177.ui.theme.Voyager177Theme

class MainActivity : ComponentActivity() {
    private val contactsRepository: ContactsRepository by lazy {
        ContactsProviderRepository(context = applicationContext)
    }

    private val dialer: Dialer by lazy {
        IntentDialer(activity = this)
    }

    private var hasContactsPermission by mutableStateOf(false)

    private val contactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasContactsPermission = granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hasContactsPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        enableEdgeToEdge()

        setContent {
            Voyager177Theme {
                ContactsScreen(
                    contactsRepository = contactsRepository,
                    dialer = dialer,
                    hasContactsPermission = hasContactsPermission,
                    onRequestContactsPermission = {
                        contactsPermissionLauncher.launch(
                            Manifest.permission.READ_CONTACTS
                        )
                    }
                )
            }
        }
    }
}