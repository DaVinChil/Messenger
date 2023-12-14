package ru.ns.messenger.ui.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ns.messenger.api.Message
import java.text.SimpleDateFormat
import java.util.Date

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
    LazyColumn(modifier = modifier.padding(horizontal = 5.dp)) {
        items(
            count = messages.size,
            key = { messages[it].id }
        ) {
            val arrangement =
                if (isMineMessage(messages[it])) Arrangement.End else Arrangement.Start
            Spacer(modifier = Modifier.height(5.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = arrangement) {
                Message(message = messages[it])
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun formatDate(date: Date): String = SimpleDateFormat("hh:mm").format(date)

@Composable
fun Message(modifier: Modifier = Modifier, message: Message) {
    Column(
        modifier = modifier
            .widthIn(min = 50.dp, max = 200.dp)
            .background(color = Color(0x5019D8E1), shape = RoundedCornerShape(20.dp))
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
    ) {
        
        Text(
            modifier = Modifier
                .align(Alignment.Start),
            fontSize = 13.sp,
            text = message.sender.name,
            color = Color(0xFF0EB355),
            fontWeight = Bold
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                fontSize = 17.sp,
                text = message.message
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                modifier = Modifier,
                fontSize = 10.sp,
                text = formatDate(message.date)
            )
        }

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