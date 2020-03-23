package com.gapps.emojifire.library.core.repository.models

import kotlinx.serialization.Serializable

@Serializable
data class Emoji(
		var codes: String? = null,
		var group: String? = null,
		var subgroup: String? = null,
		var name: String? = null
) {
	fun get(): String? {
			return codes?.let { codeToStr(it) }
	}

	private fun codeToStr(it: String): String{
		val result = it.split(" ").map { Character.toChars(it.toInt(16))}
				.reduce { acc, chars -> acc + chars }
		return String(result)
	}
}