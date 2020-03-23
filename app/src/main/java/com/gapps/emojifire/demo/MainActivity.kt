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

		clearEmoji.setOnClickListener {
			text.text = null
		}
	}

	private fun loadEmojiData() {
		text.setText(Emojifire.getAllEmojiList()?.map { it.get() }?.joinToString { it ?: "" })
	}
}