package br.unisanta.appsanta

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.unisanta.appsanta.databinding.ActivityUserViewBinding
import com.firebase.ui.auth.AuthUI

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
        binding.fabBack.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    finish()
                }

        }
    }
}