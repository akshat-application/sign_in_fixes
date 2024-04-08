package com.example.loginusinggoogle

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.loginusinggoogle.ui.theme.LoginUsingGoogleTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val signInResult: MutableStateFlow<String> = MutableStateFlow("")
    val _signInResult: StateFlow<String> = signInResult
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginUsingGoogleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    getStorage("signin using google")
                }
            }
        }
    }

    @Composable
    fun getStorage(name: String, modifier: Modifier = Modifier) {
        var isLogin by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val displayName = _signInResult.collectAsState().value
        val loginUsingEmail = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { uri ->

        }
        Box (modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                if (isLogin) {
                    Text(
                        text = "Hello $displayName!",
                        modifier = modifier
                    )
                } else {
                    val context = LocalContext.current
                    val gmeailSignIn: GoogleSignInOptions =
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken("default_web_client_id")
                            .requestEmail()
                            .build()
                    val mGoogleSignInClient: GoogleSignInClient =
                        GoogleSignIn.getClient(context, gmeailSignIn)
                    Text(
                        text = "Hello $name!",
                        modifier = modifier.fillMaxWidth()
                    )
                    Button(modifier = modifier, onClick = {
                        val signInIntent = mGoogleSignInClient.signInIntent
                        (context as? MainActivity)?.startActivityForResult(signInIntent, RC_SIGN_IN)
                    }) {
                        Text(
                            text = "click to sign",
                            modifier = modifier.fillMaxWidth()
                        )
                        isLogin = true
                    }
                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN ) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
//            val displayName = account?.displayName ?: "Unknown"
            lifecycleScope.launch {
                signInResult.emit("account Name")
            }
        }
    }
}

const val RC_SIGN_IN = 9001