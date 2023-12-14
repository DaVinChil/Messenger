package ru.ns.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.ns.messenger.api.Message
import ru.ns.messenger.db.dao.User
import ru.ns.messenger.ui.chat.ChatScreen
import ru.ns.messenger.ui.register.RegisterScreen
import ru.ns.messenger.ui.theme.MessengerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessengerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MainViewModel = viewModel()
                    if(viewModel.isUserFetching.value) {
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    } else {
                        ChatApp(viewModel.user, viewModel::saveUser, viewModel.message.value, viewModel::sendMessage)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatApp(user: User?, onSubmitUsername: (String) -> Unit, messages: List<Message>, onSendMessage: (String) -> Unit) {
    if(user == null) {
        RegisterScreen(onSubmitUsername = onSubmitUsername)
    } else {
        ChatScreen(messages = messages, onSendMessage = onSendMessage, isMineMessage = { it.sender.name == user.name})
    }
}