import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CalculatorImplTest {

    @Test
    fun calculate() {
        val calc: Calculator = CalculatorImpl()
        calc.calculate("toDollars(737p + toRubles(\$85.4))")
    }
}