import java.util.*

interface Calculator {
    fun calculate(s: String): Currency
}

class CalculatorImpl : Calculator {

    companion object {
        private val operators = "()+-"
    }

    override fun calculate(s: String): Currency {
        val ops = Stack<OperatorType>()
        val vals = Stack<Currency>()

        var i = 0
        while (i < s.length) {

            when (s[i]) {
                ' ' -> Unit
                in Currency.cCh -> {
                    val sb = StringBuilder()
                    while (i < s.length && s[i] in Currency.cCh) {
                        sb.append(s[i])
                        i++
                    }

                    vals.push(Currency.parseCurrency(sb.toString()))
                    i--
                }

                '(', 't' -> {
                    val type = if (s[i] == 't') {
                        if (s[i + 2] == 'R'){
                            i += 8
                            OperatorType.TO_RUBLES
                        } else {
                            i += 9
                            OperatorType.TO_DOLLARS
                        }

                    } else {
                        OperatorType.OPEN_BRACKET
                    }
                    ops.push(type)
                }

                ')' -> {
                    val closingList = listOf(OperatorType.TO_RUBLES, OperatorType.TO_DOLLARS, OperatorType.OPEN_BRACKET)
                    while (ops.peek() !in closingList) {
                        val op = ops.pop()

                        vals.push(
                            applyOp(op,vals.pop(), vals.pop())
                        )
                    }

                    val op = ops.pop()
                    if (op == OperatorType.TO_DOLLARS)
                        vals.push((vals.pop() as Currency.Rubles).toDollars())
                    else if (op == OperatorType.TO_RUBLES)
                        vals.push((vals.pop() as Currency.Dollars).toRubles())
                }

                in "+-" -> {
                    while (!ops.empty() &&
                        hasPrecedence(OperatorType.parseType(s[i].toString())!!,
                            ops.peek()))
                        vals.push(applyOp(ops.pop(),
                            vals.pop(),
                            vals.pop()))

                    // Push current token to 'ops'.
                    ops.push(OperatorType.parseType(s[i].toString()));
                }
            }

            i++;
        }

        return vals.pop()!!
    }

    private fun applyOp(op: OperatorType, b: Currency, a:Currency): Currency {
        if (op == OperatorType.MINUS){
            b.amount = -b.amount
        }
        return a.add(b)
    }

    private fun hasPrecedence(
        op1: OperatorType, op2: OperatorType
    ): Boolean {
        return !(op2 == OperatorType.OPEN_BRACKET || op2 == OperatorType.TO_RUBLES || op2 == OperatorType.TO_DOLLARS)
    }

    enum class OperatorType {
        TO_RUBLES,
        TO_DOLLARS,
        PLUS,
        MINUS,
        OPEN_BRACKET;
        companion object{
            fun parseType(s: String): OperatorType? {
                return when (s) {
                    "toRubles(" -> TO_RUBLES
                    "toDollars(" -> TO_DOLLARS
                    "+" -> PLUS
                    "-" -> MINUS
                    "(" -> OPEN_BRACKET
                    else -> null
                }
            }
        }


    }


}