class Bank(private val logger: LoggerInterface) : BankObserver {
    private val cashRegisters = mutableListOf<CashRegister>()

    init {
        CentralBank.subscribe(this)
    }

    fun createCashRegister(): CashRegister {
        val cashRegister = CashRegister(this, logger)
        cashRegisters.add(cashRegister)
        return cashRegister
    }

    override fun onExchangeRateUpdated(newRates: Map<Currency, Double>) {
        logger.logInfo("Exchange rates updated: $newRates")
        cashRegisters.forEach { it.updateExchangeRates(newRates) }
    }
}