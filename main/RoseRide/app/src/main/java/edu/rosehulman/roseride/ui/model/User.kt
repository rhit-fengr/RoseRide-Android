package edu.rosehulman.roseride.ui.model

data class User(var name: String="",
                var phone: String="",
                var email: String="") {
    override fun toString(): String {
        return if (name.isNotBlank()) "$name, phone: $phone, email addr: $email" else ""
    }
}