package com.example.justmc.utils

import okhttp3.ResponseBody
import okio.BufferedSource
import okio.ForwardingSource
import okio.buffer

class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val onProgress: (Float) -> Unit
) : ResponseBody() {
    override fun contentType() = responseBody.contentType()
    override fun contentLength() = responseBody.contentLength()

    override fun source(): BufferedSource {
        return object : ForwardingSource(responseBody.source()) {
            var bytesRead = 0L
            val totalBytes = contentLength()
            override fun read(sink: okio.Buffer, byteCount: Long): Long {
                val read = super.read(sink, byteCount)
                if (read != -1L) {
                    bytesRead += read
                    if (totalBytes > 0) {
                        onProgress(bytesRead.toFloat() / totalBytes)
                    }
                }
                return read
            }
        }.buffer()
    }
}