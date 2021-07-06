package com.task.smartdubai.ui.component.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.task.databinding.SplashLayoutBinding
import com.task.smartdubai.ui.base.BaseActivity
import com.task.smartdubai.SPLASH_DELAY
import com.task.smartdubai.ui.component.articles.ArticlesListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity(){

    private lateinit var binding: SplashLayoutBinding

    override fun initViewBinding() {
        binding = SplashLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToMainScreen()
    }

    override fun observeViewModel() {
    }

    private fun navigateToMainScreen() {
        Handler().postDelayed({
            val nextScreenIntent = Intent(this, ArticlesListActivity::class.java)
            startActivity(nextScreenIntent)
            finish()
        }, SPLASH_DELAY.toLong())
    }
}
