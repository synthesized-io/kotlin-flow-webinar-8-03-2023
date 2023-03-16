package io.synthesized.kotlin.helpers

import com.zaxxer.hikari.HikariDataSource
import org.jooq.Cursor
import org.jooq.Record
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import java.io.Closeable

class Db(private val container: JdbcDatabaseContainer<*>) : Closeable {
    private val ds = HikariDataSource().apply {
        jdbcUrl = container.jdbcUrl
        username = container.username
        password = container.password
    }

    private val dslContext = DefaultConfiguration().apply {
        set(ds)
    }.dsl()

    fun read(table: String): Cursor<Record> = dslContext.selectFrom(DSL.name(table)).fetchLazy()

    fun size(table: String): Int = dslContext.fetchCount(DSL.table(table))

    fun write(table: String, records: List<Record>) {
        with(dslContext) {
            batch(
                records.map {
                    insertInto(DSL.table(table)).set(it.intoMap())
                }
            ).execute()
        }
    }

    fun maskRecord(record: Record, maskedFields: List<String>): Record {
        val newRecord = dslContext.newRecord(record.fields().toList())
        newRecord.from(record.intoMap())
        maskedFields.forEach { f ->
            val df = DSL.field(f, String::class.java)
            newRecord[df] = Masking.maskString(record[df])
        }
        return newRecord
    }

    override fun close() {
        ds.close()
    }

    companion object {
        val inputDb = PostgreSQLContainer("postgres:latest").apply {
            withInitScript("input.ddl")
            portBindings = listOf("5432:5432")
            start()
        }.let(::Db)

        val outputDb = PostgreSQLContainer("postgres:latest").apply {
            withInitScript("output.ddl")
            portBindings = listOf("15432:5432")
            start()
        }.let(::Db)
    }
}