package com.gapps.emojifire.library.core

import android.content.Context
import com.gapps.emojifire.library.core.repository.EmojifireRepository
import com.gapps.emojifire.library.core.repository.models.Emoji
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EmojifireWorker(private val context: Context) : CoroutineScope {
	override val coroutineContext: CoroutineContext
		get() = SupervisorJob() + Dispatchers.Main

	private val repository = EmojifireRepository()

	val emojiList: MutableList<Emoji> = mutableListOf()

	fun load() {
		launch {

			emojiList.addAll(repository.loadData(context))
		}
	}
}