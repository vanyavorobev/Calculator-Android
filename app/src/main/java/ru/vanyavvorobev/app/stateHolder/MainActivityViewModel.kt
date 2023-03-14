package ru.vanyavvorobev.app.stateHolder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.vanyavvorobev.app.domain.AtomOfExpression
import ru.vanyavvorobev.app.domain.MathExecutor
import ru.vanyavvorobev.app.domain.MathToken
import ru.vanyavvorobev.app.domain.TokenType

class MainActivityViewModel: ViewModel() {

	private val mState: MutableStateFlow<MainActivityState> = MutableStateFlow(MainActivityState.Initial)
	val state: StateFlow<MainActivityState> = mState

	private val executor: MathExecutor = MathExecutor()
	private var expressionState: Int = 0

	companion object {

		private val EMPTY_STATE = MainActivityState.Content(
			atomList = mutableListOf(AtomOfExpression("", TokenType.Empty, 0)),
			expression = ""
		)

	}

	fun initState() {
		mState.value = EMPTY_STATE.copy()
		expressionState = 0
	}


	// это конечный автомат, который запрещает вводить некорректные последовательности
	// здесь не отлавливаются арифметические ошибки, типа, деления на 0
	// также в нем происходит перевод в токены, через которые удобно работать
	// операнд, скобка, функция и операция являются одним токеном
	fun handleToken(inToken: MathToken) {
		val currentState = EMPTY_STATE.copy()
		if(mState.value !is MainActivityState.Content) { throw java.lang.IllegalStateException() }
		else {
			currentState.atomList = (mState.value as MainActivityState.Content).atomList
			currentState.expression = (mState.value as MainActivityState.Content).expression
		}
		val lastAtom = if(currentState.atomList.isEmpty()) AtomOfExpression("", TokenType.Empty, 0) else currentState.atomList.last()

		val token = if(inToken == MathToken.B_MINUS && expressionState == 0) MathToken.PF_MINUS else inToken

		when (token) {
			MathToken.RESULT -> {
				val ans = executor.execute(currentState.atomList)
				currentState.result = ans.value
				expressionState = 0
				mState.value = currentState.copy(atomList = mutableListOf(), expression = "")
				return
			}
			MathToken.AC -> {
				mState.value = EMPTY_STATE.copy(result = (mState.value as MainActivityState.Content).result)
				return
			}
			MathToken.BACKSPACE -> {
				if(currentState.expression.isEmpty()) return
				while(currentState.expression.last().toString() == " ") {
					currentState.expression.removeSuffix(" ")
				}
				val del = currentState.expression.last().toString()
				currentState.expression = currentState.expression.substring(0, currentState.expression.length - del.length)
				currentState.atomList.last().value = currentState.atomList.last().value.substring(0, currentState.atomList.last().value.length - del.length)
				if(currentState.atomList.last().value.isEmpty()) {
					currentState.atomList.removeLast()
				}
				mState.value = currentState.copy(result = (mState.value as MainActivityState.Content).result)
				return
			}
			else -> {  }
		}

		when(expressionState) {
			0 		-> {
				when(token.type) {
					TokenType.Int                  -> {
						expressionState = 1
						currentState.expression += token.value
						currentState.atomList.add(AtomOfExpression(token.value, token.type, token.priority))
					}
					TokenType.PrefixFunction  -> {
						expressionState = 0
						currentState.expression += token.value
						currentState.atomList.add(AtomOfExpression(token.value, token.type, token.priority))
					}
					else                            -> {}
				}
			}
			1 		-> {
				when(token.type) {
					TokenType.Int 						-> {
						expressionState = 1
						currentState.expression += token.value
						lastAtom.value += token.value
					}
					TokenType.Point 					-> {
						expressionState = 2
						currentState.expression += token.value
						lastAtom.value += token.value
						lastAtom.type = token.type
					}
					TokenType.PostfixFunction 	-> {
						expressionState = 4
						currentState.expression += " ${token.value}"
						currentState.atomList.add(AtomOfExpression(token.value, token.type, token.priority))
					}
					TokenType.PostfixBinaryOperation 	-> {
						expressionState = 0
						currentState.expression += " ${token.value} "
						currentState.atomList.add(AtomOfExpression(token.value, token.type, token.priority))
					}
					else 								-> {}
				}
			}
			2 		-> {
				when(token.type) {
					TokenType.Int 						-> {
						expressionState = 3
						currentState.expression += token.value
						lastAtom.value += token.value
						lastAtom.type = TokenType.Double
					}
					else 								-> {}
				}
			}
			3 		-> {
				when(token.type) {
					TokenType.Int 						-> {
						expressionState = 3
						currentState.expression += token.value
						lastAtom.value += token.value
						lastAtom.type = token.type
					}
					TokenType.PostfixBinaryOperation 	-> {
						expressionState = 0
						currentState.expression += " ${token.value} "
						currentState.atomList.add(AtomOfExpression(token.value, token.type, token.priority))
					}
					TokenType.PostfixFunction 	-> {
						expressionState = 4
						currentState.expression += " ${token.value}"
						currentState.atomList.add(AtomOfExpression(token.value, token.type, token.priority))
					}
					else 								-> {}
				}
			}
			4		-> {
				when(token.type) {
					TokenType.PostfixBinaryOperation	-> {
						expressionState = 0
						currentState.expression += " ${token.value} "
						currentState.atomList.add(AtomOfExpression(token.value, token.type, token.priority))
					}
					TokenType.PostfixFunction 	-> {
						expressionState = 4
						currentState.expression += " ${token.value}"
						val tokens = token.value.split(" ")
						currentState.atomList.add(AtomOfExpression(tokens[0], TokenType.PostfixBinaryOperation, 2))
						currentState.atomList.add(AtomOfExpression(tokens[1], TokenType.Double, 0))
					}
					else								-> {}
				}
			}
			else 	-> {

			}
		}

		mState.value = currentState.copy(result = (mState.value as MainActivityState.Content).result)
	}




}