interface LoggerInterface {
    fun logInfo(message: String)
    fun logError(message: String)
}

class ConsoleLogger : LoggerInterface {
    override fun logInfo(message: String) {
        println("INFO: $message")
    }

    override fun logError(message: String) {
        println("ERROR: $message")
    }
}