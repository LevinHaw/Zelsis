package com.submission.zelsis.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.submission.zelsis.R
import com.submission.zelsis.databinding.ActivitySplashScreenBinding
import com.submission.zelsis.ui.home.HomeActivity
import com.submission.zelsis.ui.welcome.WelcomeActivity
import com.submission.zelsis.util.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel by viewModels<SplashScreenViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        binding.tvApp.startAnimation(textAnimation)



        viewModel.getSession().observe(this){ user ->
            if (!user.isLogin){
                GlobalScope.launch(Dispatchers.IO) {
                    delay(3400L)
                    Intent(this@SplashScreen, WelcomeActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
            } else {
                Log.d(TAG, user.token)
                GlobalScope.launch(Dispatchers.IO) {
                    delay(3400L)
                    Intent(this@SplashScreen, HomeActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "SplashScreen"
    }
}