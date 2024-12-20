import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

fun main() {
    val latch = CountDownLatch(10)
    val logger = ConsoleLogger()
    val bank = Bank(logger)
    val cashRegister1 = bank.createCashRegister()
    val cashRegister2 = bank.createCashRegister()

    CentralBank.startRateUpdates(5000)

    thread {
        cashRegister2.processExchange("Client2", 200.0, Currency.GBP, Currency.JPY)
        latch.countDown() }

    thread {
        cashRegister1.processExchange("Client1", 100.0, Currency.USD, Currency.EUR)
        latch.countDown() }

    latch.await()
    cashRegister1.shutdown()
    cashRegister2.shutdown()
}
