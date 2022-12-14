package com.gilang.githubgilang

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val waktu_loading = 4000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar!!.hide()
        Handler().postDelayed(Runnable { //setelah loading maka akan langsung berpindah ke home activity
            val home = Intent(this, MainActivity::class.java)
            startActivity(home)
            finish()
        }, waktu_loading)
    }
}