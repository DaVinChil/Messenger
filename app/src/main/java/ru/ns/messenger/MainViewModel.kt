package ru.ns.messenger

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ns.messenger.api.Message
import ru.ns.messenger.api.MessageDto
import ru.ns.messenger.api.UserDto
import ru.ns.messenger.db.Resource
import ru.ns.messenger.db.dao.User
import ru.ns.messenger.db.dao.UserRepository
import ru.ns.messenger.db.repository.MessengerRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val messengerRepository: MessengerRepository
) : ViewModel() {
    private var _user: MutableState<User?> = mutableStateOf(null)
    val user: State<User?>
        get() = _user
    private var _isUserFetching = mutableStateOf(true)
    val isUserFetching: State<Boolean>
        get() = _isUserFetching
    private val _messages = mutableStateOf(listOf<Message>())
    val message: State<List<Message>>
        get() = _messages

    init {
        loadUser()
        infiniteMessageLoading()
    }

    fun saveUser(userName: String) {
        viewModelScope.launch { userRepository.insertUser(userName); loadUser() }
    }

    private fun loadUser() {
        viewModelScope.launch {
            _user.value = userRepository.getUser()
            _isUserFetching.value = false
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            messengerRepository.sendMessage(MessageDto(UserDto(_user.value!!.name), message))
            when (val response = messengerRepository.getMessages()) {
                is Resource.Success -> {
                    _messages.value = response.value!!
                }
                is Resource.Error -> {}
            }
        }
    }

    private fun infiniteMessageLoading() {
        viewModelScope.launch {
            while (true) {
                when (val response = messengerRepository.getMessages()) {
                    is Resource.Success -> {
                        _messages.value = response.value!!
                    }
                    is Resource.Error -> {}
                }
                delay(10000L)
            }
        }
    }
}