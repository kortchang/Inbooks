import kotlin.text.all
import kotlin.text.contains
import kotlin.text.isLetterOrDigit
import kotlin.text.toRegex

object UserValidator {
    fun displayNameIsValid(displayName: String): Boolean {
        return displayName.isNotBlank()
    }

    fun emailIsValid(email: String): Boolean {
        val isValid = """^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$"""
            .toRegex()
            .matches(email)

        return isValid
    }

    fun passwordIsValid(password: String): Boolean {
        val isLetterOrDigit = """[a-zA-Z0-9]""".toRegex()
        val validSpecialCharacters = "!@#$%^&*()_+{}|:<>?~`-=[]\\;',./"
        val characterValid = password.all {
            isLetterOrDigit.matches(it.toString()) || it in validSpecialCharacters
        }
        val lengthValid = password.length >= 8
        return lengthValid && characterValid
    }
}