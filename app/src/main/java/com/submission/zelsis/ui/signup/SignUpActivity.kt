package com.submission.zelsis.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.submission.zelsis.R
import com.submission.zelsis.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

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
}