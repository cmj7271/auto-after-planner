package formatter

interface Formatter<Raw, Refined> {
    fun format(data: Refined): Raw
    fun parse(data: Raw): Refined
}