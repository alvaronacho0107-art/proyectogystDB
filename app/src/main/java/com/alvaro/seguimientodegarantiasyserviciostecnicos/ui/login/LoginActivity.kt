package com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.seguimientodegarantiasyserviciostecnicos.MainActivity
import com.alvaro.seguimientodegarantiasyserviciostecnicos.databinding.ActivityLoginBinding
import com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.register.RegistroActivity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private var ingresando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Inicio de sesión"
        binding.btnIngresar.setOnClickListener { ingresar() }
        binding.btnRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
        binding.txtPass.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ingresar()
                true
            } else false
        }
    }

    private fun ingresar() {
        if (ingresando) return
        val correo = binding.txtCorreo.text.toString().trim()
        val password = binding.txtPass.text.toString()
        binding.textoErrorLogin.visibility = View.GONE
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.txtCorreo.error = "Ingresa un correo válido"
            return
        }
        if (password.isEmpty()) {
            binding.txtPass.error = "Ingresa tu contraseña"
            return
        }
        ingresando = true
        binding.btnIngresar.isEnabled = false
        binding.btnRegistrar.isEnabled = false
        auth.signInWithEmailAndPassword(correo, password).addOnCompleteListener { task ->
            if (isFinishing || isDestroyed) return@addOnCompleteListener
            ingresando = false
            binding.btnIngresar.isEnabled = true
            binding.btnRegistrar.isEnabled = true
            if (task.isSuccessful) {
                Log.d("EmailPassword", "signInWithEmail:success")
                FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.LOGIN,
                    Bundle().apply { putString(FirebaseAnalytics.Param.METHOD, "password") })
                startActivity(Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            } else {
                Log.w("EmailPassword", "signInWithEmail:failure", task.exception)
                binding.textoErrorLogin.text = if (task.exception is FirebaseNetworkException)
                    "No se pudo conectar. Revisa tu conexión e intenta nuevamente."
                else "Credenciales incorrectas"
                binding.textoErrorLogin.visibility = View.VISIBLE
            }
        }
    }
}
