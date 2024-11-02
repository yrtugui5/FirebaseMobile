package br.unisanta.appsanta

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.unisanta.appsanta.databinding.ActivityUserViewBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


private lateinit var binding: ActivityUserViewBinding
class UserViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val uid = intent.getStringExtra("USER_UID")
        val email = intent.getStringExtra("USER_EMAIL")
        Log.i("USER-INFO", "$uid | $email")

        if(uid != null){
            binding.txvUID.setText(uid)
            binding.edtEmail.setText(email)
        }
        binding.btnDelete.setOnClickListener {
            val user = Firebase.auth.currentUser!!
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("USER-DELETE", "Conta de Usuario $uid deletada.")
                        finish()
                    }
                }
        }
        binding.fabBack.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    finish()
                }

        }
    }
}