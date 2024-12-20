import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.random.Random

object CentralBank {
    private val observers = mutableListOf<BankObserver>()
    private val exchangeRates = mutableMapOf<Currency, Double>()
    private val lock = ReentrantLock()
    private val executor = Executors.newSingleThreadExecutor()

    init {
        Currency.values().forEach { exchangeRates[it] = 1.0 }
    }

    fun subscribe(observer: BankObserver) {
        lock.lock()
        try {
            observers.add(observer)
        } finally {
            lock.unlock()
        }
    }

    fun updateExchangeRates() {
        lock.lock()
        try {
            Currency.values().forEach { currency ->
                val rateMultiplier = 0.8 + Random.nextDouble(0.4) // Ручное значение от 0.8 до 1.2
                exchangeRates[currency] = rateMultiplier * (exchangeRates[currency] ?: 1.0)
            }
            observers.forEach { it.onExchangeRateUpdated(exchangeRates) }
        } finally {
            lock.unlock()
        }
    }

    fun getRate(currency: Currency): Double {
        lock.lock()
        return try {
            exchangeRates[currency] ?: throw IllegalArgumentException("Currency not found: $currency")
        } finally {
            lock.unlock()
        }
    }

    fun startRateUpdates(intervalMillis: Long) {
        executor.submit {
            while (true) {
                Thread.sleep(intervalMillis)
                updateExchangeRates()
            }
        }
    }
}

interface BankObserver {
    fun onExchangeRateUpdated(newRates: Map<Currency, Double>)
}
