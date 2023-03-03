package ru.vanyavvorobev.app.stateHolder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vanyavvorobev.app.domain.AtomOfExpression
import ru.vanyavvorobev.app.domain.MathExecutor
import ru.vanyavvorobev.app.domain.MathToken
import ru.vanyavvorobev.app.domain.TokenType

class MainActivityViewModel: ViewModel() {

	private val _state: MutableLiveData<MainActivityState> = MutableLiveData(MainActivityState.Initial)
	val state: LiveData<MainActivityState> = _state

	private val executor: MathExecutor = MathExecutor()
	private var expressionState: Int = 0

	companion object {

		private val EMPTY_STATE = MainActivityState.Content(
			atomList = mutableListOf(AtomOfExpression("", TokenType.Empty, 0)),
			expression = ""
		)

	}

	fun initState() {
		_state.value = EMPTY_STATE.copy()
		expressionState = 0
	}


	// это конечный автомат, который запрещает вводить некорректные последовательности
	// здесь не отлавливаются арифметические ошибки, типа, деления на 0
	// также в нем происходит перевод в токены, через которые удобно работать
	// операнд, скобка, функция и операция являются одним токеном
	fun handleToken(inToken: MathToken) {
		val currentState = _state.value as? MainActivityState.Content ?: return
		val lastAtom = if(currentState.atomList.isEmpty()) AtomOfExpression("", TokenType.Empty, 0) else currentState.atomList.last()

		val token = if(inToken == MathToken.B_MINUS && expressionState == 0) MathToken.PF_MINUS else inToken

		if(token == MathToken.RESULT) {
			executor.execute(currentState.atomList)
		}

		when(expressionState) {
			0 		-> {
				when(token.type) {
					TokenType.Int                  -> {
						expressionState = 1
						currentState.expression += token.value
						currentState.atomList.add(AtomOfExpression(token.value, token.type, token.priority))
					}
					TokenType.PrefixFunction  -> { //todo
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
						currentState.expression += " ${token.value} "
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
						lastAtom.type = token.type
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
						currentState.expression += " ${token.value} "
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
						currentState.expression += " ${token.value} "
						currentState.atomList.add(AtomOfExpression(token.value, token.type, token.priority))
					}
					else								-> {}
				}
			}
			else 	-> {

			}
		}

		_state.value = currentState.copy()
	}




}