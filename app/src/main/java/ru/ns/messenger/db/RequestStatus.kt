package ru.ns.messenger.db

sealed class RequestStatus {
    object Success : RequestStatus()
    class Error(val message: String) : RequestStatus()
}