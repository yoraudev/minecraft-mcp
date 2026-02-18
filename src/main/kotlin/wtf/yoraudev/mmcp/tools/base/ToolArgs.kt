package wtf.yoraudev.mmcp.tools.base

object ToolArgs {
    fun int(
        arguments: Map<String, Any>,
        name: String,
        defaultValue: Int,
        min: Int,
        max: Int
    ): Int {
        val parsed = when (val value = arguments[name]) {
            is Number -> value.toInt()
            is String -> value.toIntOrNull()
            else -> null
        } ?: defaultValue
        return parsed.coerceIn(min, max)
    }

    fun double(
        arguments: Map<String, Any>,
        name: String,
        defaultValue: Double,
        min: Double,
        max: Double
    ): Double {
        val parsed = when (val value = arguments[name]) {
            is Number -> value.toDouble()
            is String -> value.toDoubleOrNull()
            else -> null
        } ?: defaultValue
        return parsed.coerceIn(min, max)
    }

    fun boolean(
        arguments: Map<String, Any>,
        name: String,
        defaultValue: Boolean
    ): Boolean {
        return when (val value = arguments[name]) {
            is Boolean -> value
            is Number -> value.toInt() != 0
            is String -> value.equals("true", true) || value == "1"
            else -> defaultValue
        }
    }
}
