package io.synthesized

import io.synthesized.kotlin.helpers.Db.Companion.inputDb
import io.synthesized.kotlin.helpers.Db.Companion.outputDb
import io.synthesized.kotlin.helpers.chunked
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import mu.KotlinLogging
import org.jooq.Record

private val logger = KotlinLogging.logger { }

class DataTransfer(
    val table: String,
    val maskedFields: List<String>,
    val parents: List<DataTransfer> = emptyList(),
    val scope: CoroutineScope? = null
) {
    val chunkSize = 2

    @OptIn(FlowPreview::class)
    val data: Flow<List<Record>> = flow {
        if (parents.isNotEmpty()) {
            parents.asFlow()
                .map { it.data }
                .flattenMerge(concurrency = parents.size)
                .collect()
        }

        inputDb.read(table).forEach {
            emit(it)
        }
    }.map {
        inputDb.maskRecord(it, maskedFields)
    }.onEach {
        logger.info { "incoming record: $it" }
    }.onStart {
        logger.info { "starting transfer: $table" }
    }.chunked(chunkSize).onEach {
        outputDb.write(table, it)
    }.onCompletion {
        logger.info { "finished transfer: $table" }
    }.let {
        if (scope != null) {
            it.shareIn(scope + CoroutineName("$table-shared"), SharingStarted.Lazily)
        } else {
            it
        }
    }.take((inputDb.size(table) + chunkSize - 1) / chunkSize)

    suspend fun run() = data.collect()
}