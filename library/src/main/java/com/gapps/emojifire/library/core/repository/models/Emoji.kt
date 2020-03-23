package com.gapps.emojifire.library.core.repository.models

import com.gapps.emojifire.library.utils.unicodeToUtf16
import kotlinx.serialization.Serializable

@Serializable
data class Emoji(
		var codes: String? = null,
		var group: String? = null,
		var subgroup: String? = null,
		var name: String? = null
) {
	private var emoji: String? = null
	fun get(): String? {
		if (emoji == null) {
			emoji = codes?.unicodeToUtf16()
		}

		return emoji
	}
}