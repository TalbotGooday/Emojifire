package com.gapps.emojifire

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat

class App: Application() {
	override fun onCreate() {
		super.onCreate()

		initEmoji()
	}


	private fun initEmoji() {
		val config = BundledEmojiCompatConfig(this)
		config.setReplaceAll(true)
		EmojiCompat.init(config)
	}

}