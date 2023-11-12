package com.example.myapplication.data

class UserState {
    private var user: User? = null
    fun setUser(user: User?) {
        this.user = user
    }

    fun getUser(): User? {
        return user
    }

    companion object {
        private var instance: UserState? = null
        fun getInstance() : UserState? {
            if (instance == null)
                instance = UserState()
            return instance
        }
    }
}