package com.example.learn_new_language



import com.google.common.truth.Truth.assertThat
import org.junit.Test

class checkLoginTest {

    @Test
    fun emptyEmailTurnFalse() {
        val result = checkLogin.validationOfLogin(
            email = "",
            password = "123456"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun emptyPasswordTurnFalse() {
        val result = checkLogin.validationOfLogin(
            email = "ahmad@hotmail.com",
            password = ""
        )
        assertThat(result).isFalse()
    }



}