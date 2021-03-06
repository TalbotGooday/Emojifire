package com.gapps.emojifire.library.core.repository

import android.content.Context
import com.gapps.emojifire.library.core.repository.models.Emoji
import com.gapps.emojifire.library.core.repository.models.EmojiBox
import com.gapps.emojifire.library.utils.EMOJI_VERSION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.nio.charset.Charset


class EmojifireRepository {
	companion object {
		const val TAG = "EmojifireRepository"
	}

	private var client = OkHttpClient()

	suspend fun loadData(context: Context) = withContext(Dispatchers.IO) {
		val currentVersion = getCurrentCachedVersion(context)
		val latestVersion = getLatestEmojiVersion()

		val result = mutableListOf<Emoji>()

		if (currentVersion == latestVersion) {
			result.addAll(readEmojiData(context))
		}

		if (result.isNotEmpty()) return@withContext result

		//Make request if needed
		val request: Request = Request.Builder()
				.url("https://unicode.org/Public/emoji/$latestVersion/emoji-test.txt")
				.get()
				.build()


		val emojiBox = EmojiBox()

		client.newCall(request)
				.execute()
				.use { response ->
					return@use response.body
							?.string()
							?.trim()
							?.split("\n")
							?.forEach { line ->
								if (line.startsWith("# group: ")) {
									emojiBox.group = line.substring(9)
								} else if (line.startsWith("# subgroup: ")) {
									emojiBox.subgroup = line.substring(12)
								} else if (line.startsWith('#')) {
									emojiBox.comments = emojiBox.comments + line + '\n'
								} else {
									val emoji = parseLine(line)

									if (emoji != null) {
										emoji.group = emojiBox.group
										emoji.subgroup = emojiBox.subgroup

										result.add(emoji)
									}
								}
							}

				}

		writeEmojiData(context, result)

		return@withContext result
	}

	private suspend fun readEmojiData(context: Context): List<Emoji> = withContext(Dispatchers.IO) {
		val json = Json(JsonConfiguration.Stable)

		val emojiDir = getEmojiCacheDir(context)

		if (emojiDir.exists()) {
			val emojiDataStr = File(emojiDir, EMOJI_VERSION).readText(Charset.defaultCharset())

			json.parse(Emoji.serializer().list, emojiDataStr)
		} else {
			emptyList()
		}
	}

	private suspend fun writeEmojiData(context: Context, result: MutableList<Emoji>) = withContext(Dispatchers.IO) {
		try {
			val json = Json(JsonConfiguration.Stable)

			val emojiDir = getEmojiCacheDir(context)

			if (emojiDir.exists()) {
				emojiDir.delete()
			}

			emojiDir.mkdir()

			File(emojiDir, EMOJI_VERSION).writeText(json.stringify(Emoji.serializer().list, result), Charset.defaultCharset())
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	private fun parseLine(line: String): Emoji? {
		val data = line.trim().split("\\s+[;#] ".toRegex())

		if (data.size != 3) return null

		val charAndName = "^\\S+ E\\d+\\.\\d+ (.+)\$".toRegex().find(data[2])?.groups

		return Emoji().apply {
			codes = data[0]
			name = charAndName?.get(1)?.value ?: ""
		}
	}

	private fun getCurrentCachedVersion(context: Context): String? {
		val emojiDir = getEmojiCacheDir(context)

		return emojiDir.list()?.getOrNull(0)
	}

	private fun getEmojiCacheDir(context: Context) =
			File(context.cacheDir, "emoji-cache")

	private suspend fun getLatestEmojiVersion() = withContext(Dispatchers.IO) {
		val request = Request.Builder()
				.url("https://unicode.org/Public/emoji/")
				.get()
				.build()

		val response = client.newCall(request).execute().use { response ->
			return@use response.body
					?.string()
		} ?: return@withContext null

		return@withContext "(?:<a.+>(\\S+)\\/<\\/a>).+(\\d{2}-\\w{3}-\\d{4} \\d{2}:\\d{2})"
				.toRegex().findAll(response).maxBy {
			it.groupValues.getOrNull(1)?.toFloatOrNull() ?: 0f
		}?.groups?.get(1)?.value
	}
}