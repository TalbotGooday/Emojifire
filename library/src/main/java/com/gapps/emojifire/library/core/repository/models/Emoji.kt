package com.gapps.emojifire.library.core.repository.models

import kotlinx.serialization.Serializable

@Serializable
data class Emoji(
		var codes: String? = null,
		var category: String? = null,
		var group: String? = null,
		var subgroup: String? = null,
		var char: String? = null,
		var name: String? = null,
		var status: String? = null
)