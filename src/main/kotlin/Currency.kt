sealed class Currency(
    var amount: Double,
    val sign: String
) {

    class Dollars(amount: Double) : Currency(amount, sign = "$") {
        override fun toString(): String {
            return "$sign$amount"
        }
    }

    class Rubles(amount: Double) : Currency(amount, sign = "p") {

        override fun toString(): String {
            return "$amount$sign"
        }
    }

    fun add(currency: Currency): Currency {
        if (this is Dollars && currency is Dollars) {
            return Dollars(
                amount+currency.amount,
            )
        }
        if (this is Rubles && currency is Rubles) {
            return Rubles(
                amount+currency.amount,
            )
        }
        throw RuntimeException("Can't add $this and $currency")
    }
    companion object {
        fun parseCurrency(s: String): Currency? {
            return when {
                s.startsWith("$") -> {
                    Dollars(
                        amount = s.removePrefix("$").toDouble()
                    )
                }

                s.endsWith("p") -> {
                    Rubles(
                        amount = s.removeSuffix("p").toDouble()
                    )
                }

                else -> null
            }
        }

        const val cCh = "123456789\$p."
    }
}


const val dollarToRublesRate = 60.0
const val rublesToDollarsRate = 1 / 60.0
fun Currency.Dollars.toRubles(): Currency.Rubles {
    return Currency.Rubles(
        this.amount * dollarToRublesRate
    )
}

fun Currency.Rubles.toDollars(): Currency.Dollars {
    return Currency.Dollars(
        this.amount * rublesToDollarsRate
    )
}