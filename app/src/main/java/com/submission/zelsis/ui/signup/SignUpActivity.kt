package com.submission.zelsis.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.submission.zelsis.R
import com.submission.zelsis.databinding.ActivitySignUpBinding
import com.submission.zelsis.ui.home.HomeActivity
import com.submission.zelsis.ui.login.LoginActivity
import com.submission.zelsis.ui.login.LoginViewModel
import com.submission.zelsis.util.ViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playAnimate()
        setupAction()
        checkResult()

    }

    private fun setupAction(){
        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.email_or_password_isempty),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.register(name, email, password)
            }
        }
    }

    private fun checkResult(){
        viewModel.isError.observe(this) { isError ->
            if (isError) {
                Toast.makeText(
                    this,
                    getString(R.string.register_failed),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun playAnimate() {

        val character = ObjectAnimator.ofFloat(binding.ivChar, View.ALPHA, 1f).setDuration(300)
        val registerText = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(300)
        val helper = ObjectAnimator.ofFloat(binding.tvHelper, View.ALPHA, 1f).setDuration(300)
        val usernameText = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(300)
        val usernameEditText = ObjectAnimator.ofFloat(binding.tilUsername, View.ALPHA, 1f).setDuration(300)
        val emailText = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(300)
        val emailEditText = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(300)
        val passText = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(300)
        val passEditText = ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(300)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1F).setDuration(300)
        val suggest = ObjectAnimator.ofFloat(binding.tvSuggest, View.ALPHA, 1F).setDuration(300)

        val together = AnimatorSet().apply{
            playTogether(registerText, helper)
        }

        val together2 = AnimatorSet().apply {
            playTogether(register, suggest)
        }

        AnimatorSet().apply {
            playSequentially(
                character,
                together,
                usernameText,
                usernameEditText,
                emailText,
                emailEditText,
                passText,
                passEditText,
                together2)
            startDelay = 100
        }.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}