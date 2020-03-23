package com.gapps.emojifire.library.utils

fun String.unicodeToUtf16(): String {
	val result = this.split(" ").map { Character.toChars(it.toInt(16)) }
			.reduce { acc, chars -> acc + chars }
	return String(result)
}