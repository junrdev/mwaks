package ke.ac.mwaks.util.annotations

import java.util.regex.Pattern
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidEmail(
    val regex: String = "Your Regex Here",
    val message: String = "Invalid email format"
)

class EmailValidator {
    companion object {
        fun validate(value: String, regex: String): Boolean {
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(value)
            return matcher.matches()
        }
    }
}

class CheckEmailFormat{

//    inner class ValidEmailDelegate(private val regex: String = "Your Regex Here") {
//        operator fun provideDelegate(
//            thisRef: Any?,
//            prop: KProperty<*>
//        ): Delegates.NotNullVar<String> {
//            return Delegates.notNull<String>().apply {
//                println("Delegate initialized for ${prop.name}")
//                setValue(thisRef, prop, "")
//                { _, _, newValue ->
//                    if (!EmailValidator.validate(newValue, regex)) {
//                        throw IllegalArgumentException("Invalid email format for ${prop.name}")
//                    }
//                    newValue
//                }
//            }
//        }
//    }

    /*
    * data class User(
    @ValidEmail(regex = "Your Regex Pattern Here", message = "Invalid email")
    val email: String
) {
    private val emailDelegate = ValidEmailDelegate()

    init {
        emailDelegate.provideDelegate(this, ::email)
    }
}

    * */
}