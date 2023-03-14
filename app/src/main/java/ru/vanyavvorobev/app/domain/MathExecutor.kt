package ru.vanyavvorobev.app.domain

import java.util.Stack

class MathExecutor {

	fun execute(atomList: MutableList<AtomOfExpression>): AtomOfExpression {
		return reversePolishNotation(convertToRPN(atomList))
	}

	private fun reversePolishNotation(expressionList: MutableList<AtomOfExpression>): AtomOfExpression {
		val operandStack: Stack<AtomOfExpression> = Stack()
		while(expressionList.isNotEmpty()) {
			val atom = expressionList.first()
			expressionList.removeFirst()
			when(atom.type) {
				TokenType.Point 					-> { throw IllegalArgumentException("Error with Point") }
				TokenType.Empty 					-> {  }

				TokenType.Int 						-> { operandStack.add(atom) }
				TokenType.Double 					-> { operandStack.add(atom) }

				TokenType.PostfixBinaryOperation 	-> {
					val second = operandStack.lastElement()
					operandStack.pop()
					val first = operandStack.lastElement()
					operandStack.pop()
					val newAtom = calculateBinary(first, second, atom)
					operandStack.add(newAtom)
				}

				TokenType.PrefixFunction 			-> {
					val newAtom = calculateUnary(operandStack.lastElement(), atom)
					operandStack.pop()
					operandStack.add(newAtom)
				}
				TokenType.PostfixFunction 			-> {
					val second = operandStack.lastElement()
					operandStack.pop()
					val first = operandStack.lastElement()
					operandStack.pop()
					val newAtom = calculateBinary(first, second, atom)
					operandStack.add(newAtom)
				}

				TokenType.OpenBracket           	-> { throw IllegalArgumentException("Error with brackets") }
				TokenType.CloseBracket 				-> { throw IllegalArgumentException("Error with brackets") }
			}
		}
		if(operandStack.count() == 1) {
			return operandStack.lastElement()
		}
		else {
			throw IllegalArgumentException("Incorrect expression")
		}
	}

	@Suppress("IMPLICIT_CAST_TO_ANY")
	private fun calculateBinary(first: AtomOfExpression, second: AtomOfExpression, operation: AtomOfExpression): AtomOfExpression {
		if(operation.type != TokenType.PostfixBinaryOperation) throw IllegalArgumentException()
		val firstNum: Float =
			when (first.type) {
				TokenType.Double -> first.value.toFloat()
				TokenType.Int -> first.value.toFloat()
				else -> throw IllegalArgumentException()
			}
		val secondNum: Float =
			when (second.type) {
				TokenType.Double -> second.value.toFloat()
				TokenType.Int -> second.value.toFloat()
				else -> throw IllegalArgumentException()
			}
		var isDouble = false
		var type = TokenType.Int
		if(first.type == TokenType.Double || second.type == TokenType.Double) {
			isDouble = true
			type = TokenType.Double
		}
		val value: Float = when(operation.value) {
			"+"		-> { firstNum + secondNum}
			"-"		-> { firstNum - secondNum }
			"*"		-> { firstNum * secondNum }
			"/"		-> {
				if(firstNum % secondNum != 0f) {
					type = TokenType.Double
				}
				firstNum / secondNum
			}
			else	-> throw IllegalArgumentException()
		}

		return AtomOfExpression(if(isDouble) value.toString() else value.toInt().toString(), type, 0)
	}

	@Suppress("IMPLICIT_CAST_TO_ANY")
	private fun calculateUnary(operand: AtomOfExpression, operation: AtomOfExpression): AtomOfExpression {
		if(operation.type != TokenType.PostfixFunction || operation.type != TokenType.PrefixFunction) throw IllegalArgumentException()
		return operand
	}

	private fun convertToRPN(atomList: MutableList<AtomOfExpression>): MutableList<AtomOfExpression> {
		val expressionList = mutableListOf<AtomOfExpression>()
		val operationStack: Stack<AtomOfExpression> = Stack()

		while(atomList.isNotEmpty()) {
			val atom = atomList.first()
			atomList.removeFirst()
			when(atom.type) {
				TokenType.Point 					-> { throw IllegalArgumentException("Error with Point") }
				TokenType.Empty 					-> {  }

				TokenType.Int 						-> { expressionList.add(atom) }
				TokenType.Double 					-> { expressionList.add(atom) }

				TokenType.PostfixBinaryOperation 	-> {
					while(operationStack.isNotEmpty() && (
						operationStack.lastElement().type == TokenType.PrefixFunction ||
						operationStack.lastElement().priority >= atom.priority)) {
						expressionList.add(operationStack.lastElement())
						operationStack.pop()
					}
					operationStack.add(atom)
				}

				TokenType.PrefixFunction 			-> { operationStack.add(atom) }
				TokenType.PostfixFunction 			-> { expressionList.add(atom) }

				TokenType.OpenBracket           	-> { operationStack.add(atom) }
				TokenType.CloseBracket 				-> {
					while(operationStack.isNotEmpty() && operationStack.lastElement().type != TokenType.OpenBracket) {
						expressionList.add(operationStack.lastElement())
						operationStack.pop()
					}
					if(operationStack.isEmpty()) {
						throw IllegalArgumentException("Error with brackets")
					}
					operationStack.pop()
				}
			}
		}
		while(operationStack.isNotEmpty()) {

			if(operationStack.lastElement().type == TokenType.OpenBracket) throw IllegalArgumentException("Error with brackets")
			expressionList.add(operationStack.lastElement())
			operationStack.pop()
		}
		return expressionList
	}
}