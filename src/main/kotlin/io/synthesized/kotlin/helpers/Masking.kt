package io.synthesized.kotlin.helpers

object Masking {
    private const val CAESARS_OFFSET = 1

    /**
     * Transforms a plain text into a masked text using a Caesar's code.
     */
    fun maskString(plainText: String): String =
        plainText.replace("\\w".toRegex()) { (it.value.first() + CAESARS_OFFSET).toString() }
}

object MaskingDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        println(Masking.maskString("Anton Arhipov"))
    }
}