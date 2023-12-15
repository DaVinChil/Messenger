package ru.ns.messenger.ui.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
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
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val chatListState = rememberLazyListState()
        ChatContent(
            modifier = Modifier.weight(1f),
            messages = messages,
            isMineMessage = isMineMessage,
            chatListState = chatListState
        )
        InputMessage(
            onSendMessage = onSendMessage
        )
    }
}

@Composable
fun ChatContent(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    isMineMessage: (Message) -> Boolean,
    chatListState: LazyListState
) {
    val showScrollToBottom by remember {
        derivedStateOf {
            chatListState.isNotScrolledToTheEnd()
        }
    }
    Box(modifier = modifier) {
        LazyColumn(
            state = chatListState,
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(
                count = messages.size,
                key = { messages[it].id }
            ) {
                val arrangement =
                    if (isMineMessage(messages[it])) Arrangement.End else Arrangement.Start
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = arrangement
                ) {
                    Message(message = messages[it])
                }
            }
        }
        if (showScrollToBottom) {
            val coroutineScope = rememberCoroutineScope()
            ScrollDownButton(
                onClick = {
                    coroutineScope.launch {
                        chatListState.animateScrollToItem(messages.lastIndex)
                    }
                },
                itemsCountToScrollThrough = chatListState.itemsCountAfterLastVisibleItem(),
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }

}

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
fun InputMessage(
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        var msg by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.weight(1f),
            value = msg,
            onValueChange = { msg = it },
            placeholder = {
                Text(text = "Enter a message")
            }
        )
        IconButton(
            onClick = {
                if (msg != "") {
                    onSendMessage(msg)
                    msg = ""
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                tint = Color(0xFF19D8E1),
                contentDescription = "send"
            )
        }
    }
}

@Composable
fun ScrollDownButton(
    onClick: () -> Unit,
    itemsCountToScrollThrough: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(5.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "arrow_down",
            tint = Color(0xFF4304F3),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(20.dp)
                .size(30.dp)
        )
        Text(
            text = itemsCountToScrollThrough.toString(),
            color = Color(0xFF4304F3),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(5.dp)
        )
    }
}

@SuppressLint("SimpleDateFormat")
fun formatDate(date: Date): String = SimpleDateFormat("hh:mm").format(date)

fun LazyListState.itemsCountAfterLastVisibleItem(): Int {
    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
    val lastItemIndex = layoutInfo.totalItemsCount - 1
    return lastItemIndex - lastVisibleItemIndex
}

fun LazyListState.isNotScrolledToTheEnd() = itemsCountAfterLastVisibleItem() > 0

