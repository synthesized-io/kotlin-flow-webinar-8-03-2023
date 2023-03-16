package io.synthesized.kotlin.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.chunked(chunkSize: Int): Flow<List<T>> {
    val acc = mutableListOf<T>()

    return flow {
        this@chunked.collect {
            acc.add(it)
            if (acc.size >= chunkSize) {
                emit(acc.toList())
                acc.clear()
            }
        }

        if (acc.isNotEmpty()) {
            emit(acc.toList())
        }
    }
}