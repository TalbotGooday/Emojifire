package com.gapps.emojifire.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gapps.emojifire.library.core.Emojifire
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		loadEmoji.setOnClickListener {
			loadEmojiData()
		}
	}

	private fun loadEmojiData() {
		text.text = Emojifire.getAllEmojiList()?.map { it.char }?.joinToString { "$it " }
	}
}