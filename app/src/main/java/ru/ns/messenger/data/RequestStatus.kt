package ru.ns.messenger.data

sealed class RequestStatus {
    object Success : RequestStatus()
    class Error(val message: String) : RequestStatus()
}