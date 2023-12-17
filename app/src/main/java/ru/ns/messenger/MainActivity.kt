package ru.ns.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.ns.messenger.ui.chat.ChatScreen
import ru.ns.messenger.ui.register.RegisterScreen
import ru.ns.messenger.ui.theme.MessengerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatApp()
        }
    }
}

@Composable
fun LoadingPage() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = Color(0xFF19D8E1),
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ChatApp() {
    MessengerTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            val viewModel: MainViewModel = viewModel()
            when {
                viewModel.isUserFetching -> LoadingPage()
                viewModel.user == null -> RegisterScreen(onSubmitUsername = viewModel::saveUser)
                else -> {
                    ChatScreen(
                        messages = viewModel.messages,
                        onSendMessage = viewModel::sendMessage,
                        isMineMessage = { it.sender.name == viewModel.user?.name }
                    )
                }
            }
        }
    }
}