package com.example.sqlbasics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sqlbasics.data.AppDatabase
import com.example.sqlbasics.ui.theme.SQLBasicsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val dao = AppDatabase.getDatabase(applicationContext).emailDao()
        setContent {
            SQLBasicsTheme {
                Surface(Modifier.fillMaxSize()) {
                    val emailsFlow = remember { dao.getAll() }
                    val emails by emailsFlow.collectAsState(initial = emptyList())
                    Column(Modifier.safeDrawingPadding().padding(16.dp)) {
                        Text(stringResource(R.string.email_count, emails.size), style = MaterialTheme.typography.titleLarge)
                        Text(stringResource(R.string.hint), style = MaterialTheme.typography.bodySmall)
                        LazyColumn(Modifier.padding(top = 8.dp)) {
                            items(emails, key = { it.id }) { email ->
                                Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                    Text(
                                        email.subject,
                                        fontWeight = if (email.read) FontWeight.Normal else FontWeight.Bold
                                    )
                                    Row {
                                        Text("${email.sender} · ${email.folder}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}
