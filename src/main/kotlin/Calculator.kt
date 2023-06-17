import java.lang.StringBuilder
import java.util.Stack

interface Calculator {
    fun calculate(s: String): Currency
}

class CalculatorImpl : Calculator {

    companion object {
        private val operators = "()+-"
    }

    override fun calculate(s: String): Currency {

        val st = Stack<StackEntry>()

        val buff = StringBuilder()
        var isPlus = true
        var inNumber = false
        for (ch in s) {
            if(ch in "123456789." && inNumber){

            }

            val operatorType = StackEntry.Operator.OperatorType.parseOperator(buff)
            when (operatorType) {
                StackEntry.Operator.OperatorType.TO_RUBLES,
                StackEntry.Operator.OperatorType.TO_DOLLARS,
                StackEntry.Operator.OperatorType.OPEN_BRACKET -> {
                    st.push(
                        StackEntry.Operator(operatorType, isPlus)
                    )
                }

                StackEntry.Operator.OperatorType.MINUS -> {
                    isPlus = false
                }
                else -> Unit
            }
        }
    }

    private sealed class StackEntry {
        class Operator(
            val type: OperatorType,
            val isPlus: Boolean = true
        ) : StackEntry() {
            enum class OperatorType {
                TO_RUBLES,
                TO_DOLLARS,
                PLUS,
                MINUS,
                OPEN_BRACKET;

                companion object {
                    fun parseOperator(s: CharSequence): OperatorType? {
                        return when (s) {
                            "toRubles(" -> OperatorType.TO_RUBLES
                            "toDollars(" -> OperatorType.TO_DOLLARS
                            "+" -> OperatorType.PLUS
                            "-" -> OperatorType.MINUS
                            "(" -> OperatorType.OPEN_BRACKET
                            else -> null
                        }
                    }
                }
            }
        }

        class Operand(
            val currency: Currency
        ) : StackEntry()
    }


}