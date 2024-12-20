import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.random.Random

class CashRegister(private val bank: Bank, private val logger: LoggerInterface) {
    private val lock = ReentrantLock()
    private var exchangeRates = mapOf<Currency, Double>()
    private val executor = Executors.newFixedThreadPool(4) // Пул потоков для асинхронной обработки

    fun updateExchangeRates(newRates: Map<Currency, Double>) {
        lock.lock()
        try {
            exchangeRates = newRates
        } finally {
            lock.unlock()
        }
    }

    fun processExchange(clientId: String, amount: Double, from: Currency, to: Currency) {
        executor.submit {
            Thread.sleep(Random.nextLong(100, 500))

            lock.lock()
            try {
                val rate = exchangeRates[to]!! / exchangeRates[from]!!
                val exchangedAmount = amount * rate
                logger.logInfo("Client $clientId exchanged $amount $from to $exchangedAmount $to")
            } finally {
                lock.unlock()
            }
        }
    }

    fun shutdown() {
        executor.shutdown()
    }
}
