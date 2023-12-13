package ru.ns.messenger.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ns.messenger.api.Message

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    isMineMessage: (Message) -> Boolean
) {
    Column(modifier = modifier.fillMaxSize()) {
        ChatContent(
            modifier = Modifier.weight(1f),
            messages = messages,
            isMineMessage = isMineMessage
        )
        InputMessage(onSendMessage = onSendMessage)
    }
}

@Composable
fun ChatContent(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    isMineMessage: (Message) -> Boolean
) {
    LazyColumn(modifier = modifier) {
        items(count = messages.size) {
            val arrangement =
                if (isMineMessage(messages[it])) Arrangement.End else Arrangement.Start
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = arrangement) {
                Message(message = messages[it])
            }
        }
    }
}

@Composable
fun Message(modifier: Modifier = Modifier, message: Message) {
    Column(modifier = modifier.widthIn(min = 0.dp, max = 200.dp)) {
        Text(
            modifier = Modifier
                .align(Alignment.Start),
            fontSize = 10.sp,
            text = message.sender.name
        )
        Text(
            modifier = Modifier,
            fontSize = 22.sp,
            text = message.message
        )
        Text(
            modifier = Modifier
                .align(Alignment.End),
            fontSize = 10.sp,
            text = message.date.toString()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputMessage(onSendMessage: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        var msg by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.weight(1f),
            value = msg,
            onValueChange = { msg = it },
            placeholder = {
                Text(
                    text = "Enter message"
                )
            })
        IconButton(onClick = { onSendMessage(msg) }) {
            Icon(imageVector = Icons.Filled.Send, contentDescription = "send")
        }
    }
}