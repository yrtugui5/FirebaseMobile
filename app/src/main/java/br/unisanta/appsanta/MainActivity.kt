package br.unisanta.appsanta

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.unisanta.appsanta.databinding.ActivityMainBinding
import com.facebook.FacebookSdk
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse


private const val RC_SIGN_IN = 123
private lateinit var auth: FirebaseAuth
private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        FacebookSdk.setClientToken("1234567890")
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            auth.signInWithEmailAndPassword(
                binding.edtEmail.text.toString(),
                binding.edtSenha.text.toString()
            ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("LOGIN", "SUCESSO")
                        val user = Firebase.auth.currentUser
                        user?.let {
                            val uid = it.uid
                            val email = it.email
                            Log.i("USER-INFO", "$uid | $email")

                            val intent = Intent(this, UserViewActivity::class.java).apply {
                                putExtra("USER_EMAIL", email)
                                putExtra("USER_UID", uid)
                            }
                            startActivity(intent)
                        }
                    } else {
                        Log.d("LOGIN", "FALHOU")
                        Toast.makeText(this, "FALHOU", Toast.LENGTH_LONG).show()
                    }
                }
        }
        binding.txvForgot.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FORGOT-PW", "Email enviado para $email.")
                        Toast.makeText(this, "Email de redefinição enviado!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Log.d("FORGOT-PW", "Falha ao enviar o email.")
                        Toast.makeText(
                            this,
                            "Falha ao enviar email de redefinição.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        binding.btnCadastro.setOnClickListener {
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.TwitterBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build()
            )

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()

            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                // Login bem-sucedido
                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    val uid = it.uid
                    val email = it.email

                    Log.i("USER-INFO", "UID: $uid, Email: $email")

                    // Navegue para a próxima tela ou faça outras ações
                    val intent = Intent(this, UserViewActivity::class.java).apply {
                        putExtra("USER_EMAIL", email)
                        putExtra("USER_UID", uid)
                    }
                    startActivity(intent)
                }
            } else {
                // Falha no login
                Log.d("LOGIN", "Falha no login")
                Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_SHORT).show()
            }
        }


    }
}