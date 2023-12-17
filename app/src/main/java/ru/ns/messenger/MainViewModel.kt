package ru.ns.messenger

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ns.messenger.data.remote.Message
import ru.ns.messenger.data.remote.MessageDto
import ru.ns.messenger.data.remote.UserDto
import ru.ns.messenger.data.Resource
import ru.ns.messenger.data.local.User
import ru.ns.messenger.data.local.UserRepository
import ru.ns.messenger.data.remote.MessengerRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val messengerRepository: MessengerRepository
) : ViewModel() {
    private var _user: User? by mutableStateOf(null)
    val user: User?
        get() = _user

    private var _isUserFetching by mutableStateOf(false)
    val isUserFetching: Boolean
        get() = _isUserFetching

    private var _messages by mutableStateOf(emptyList<Message>())
    val messages: List<Message>
        get() = _messages

    init {
        loadUser()
        infiniteMessageLoading()
    }

    fun saveUser(userName: String) {
        viewModelScope.launch {
            userRepository.insertUser(userName)
            loadUser()
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            _isUserFetching = true
            _user = userRepository.getUser()
            _isUserFetching = false
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val messageDto = MessageDto(
                sender = UserDto(_user!!.name),
                message = message
            )
            messengerRepository.sendMessage(messageDto)

            when (val response = messengerRepository.getMessages()) {
                is Resource.Success -> {
                    _messages = response.value!!
                }
                is Resource.Error -> {
                    // Error catching
                }
            }
        }
    }

    private fun infiniteMessageLoading() {
        viewModelScope.launch {
            while (true) {
                when (val response = messengerRepository.getMessages()) {
                    is Resource.Success -> {
                        if (_messages != response.value!!) {
                            _messages = response.value
                        }
                    }
                    is Resource.Error -> {
                        // Error catching
                    }
                }
                delay(2000L)
            }
        }
    }
}