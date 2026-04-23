package com.onoffrice.marvel_comics.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.databinding.ActivitySplashBinding
import com.onoffrice.marvel_comics.ui.characters.createCharactersActivityIntent
import com.onoffrice.marvel_comics.utils.extensions.startActivitySlideTransition

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLogoAnimation()
        setDelayForActivity()
    }

    private fun setDelayForActivity() {
        val handle = Handler()
        handle.postDelayed({
            startActivitySlideTransition(createCharactersActivityIntent())
            finish()
            }, 4000)
    }

    private fun setLogoAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_transition)
        animation.repeatCount    = 1
        animation.duration       = 2000
        animation.fillAfter      = true
        animation.repeatMode     = Animation.REVERSE
        binding.splashLogo.startAnimation(animation)
    }
}
