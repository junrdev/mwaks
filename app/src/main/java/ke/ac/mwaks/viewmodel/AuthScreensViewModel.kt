package ke.ac.mwaks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ke.ac.mwaks.util.ResponseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class AuthState(
    val isLoggedIn: Boolean = false,
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
)

class AuthScreensViewModel : ViewModel() {

    val TAG = "AuthScreensViewModel"
    private val _uiState: MutableStateFlow<AuthState>
    val uiState: StateFlow<AuthState>

    init {
        _uiState = MutableStateFlow(AuthState())
        uiState = _uiState.asStateFlow()
        updateLoginStatus()
    }


    fun updateLoginStatus() {
        _uiState.update { authState ->
            authState.copy(
                isLoggedIn = authState.auth.currentUser != null
            )
        }
    }

    fun getCurrentUser() : FirebaseUser = uiState.value.auth.currentUser!!

    suspend fun registerWithEmailAndPAssword(
        email: String,
        password: String
    ): ResponseWrapper<FirebaseUser, Boolean> {

        val wrapper = ResponseWrapper<FirebaseUser, Boolean>(null, false)

        viewModelScope.launch(Dispatchers.Main) {
            uiState.value.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val result = it.result
                        assert(uiState.value.auth.currentUser == result.user)
                        wrapper.data = uiState.value.auth.currentUser
                        wrapper.isCompleted = it.isComplete
                    }
                }
        }

        return wrapper
    }


    suspend fun loginWithEmailAndPassword(email: String, password: String) {

    }
}

