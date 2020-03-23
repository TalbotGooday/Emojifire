package com.gapps.emojifire.library.core

import android.content.Context

object Emojifire {
	private var emojifireWorker: EmojifireWorker? = null

	internal fun init(context: Context) {
		emojifireWorker = EmojifireWorker(context).also {
			it.load()
		}
	}

	fun getAllEmojiList() = emojifireWorker?.emojiList
}