package ru.vanyavvorobev.app.domain

import java.util.Stack

class MathExecutor {

	// обратная польская запись
	fun execute(atomList: MutableList<AtomOfExpression>): AtomOfExpression {
		val expressionList = convertToRPN(atomList)
		val operandStack: Stack<AtomOfExpression> = Stack()
		while(expressionList.isNotEmpty()) {
			val atom = expressionList.last()
			expressionList.removeLast()
			when(atom.type) {
				TokenType.Point 					-> { throw IllegalArgumentException("Error with Point") }
				TokenType.Empty 					-> {  }

				TokenType.Int 						-> { operandStack.add(atom) }
				TokenType.Double 					-> { operandStack.add(atom) }

				TokenType.PostfixBinaryOperation 	-> {
					val newAtom = calculateBinary(operandStack.firstElement(), operandStack.firstElement(), atom)
					operandStack.pop()
					operandStack.pop()
					operandStack.add(newAtom)
				}

				TokenType.PrefixFunction 			-> {
					val newAtom = calculateUnary(operandStack.firstElement(), atom)
					operandStack.pop()
					operandStack.add(newAtom)
				}
				TokenType.PostfixFunction 			-> {
					val newAtom = calculateUnary(operandStack.firstElement(), atom)
					operandStack.pop()
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

	private fun calculateBinary(first: AtomOfExpression, second: AtomOfExpression, operation: AtomOfExpression): AtomOfExpression {
		if(operation.type != TokenType.PostfixBinaryOperation) throw IllegalArgumentException()

		return first
	}

	private fun calculateUnary(operand: AtomOfExpression, operation: AtomOfExpression): AtomOfExpression {
		if(operation.type != TokenType.PostfixFunction || operation.type != TokenType.PrefixFunction) throw IllegalArgumentException()

		return operand
	}

	// алгоритм для перевода обычного арифметического выражения в обратную польскую запись
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