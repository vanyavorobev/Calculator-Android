package ru.vanyavvorobev.app.stateHolder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.vanyavvorobev.app.domain.AtomOfExpression
import ru.vanyavvorobev.app.domain.MathToken
import ru.vanyavvorobev.app.domain.TokenType

class MainActivityViewModel: ViewModel() {

	private val _state: MutableLiveData<MainActivityState> = MutableLiveData(MainActivityState.Initial)
	val state: LiveData<MainActivityState> = _state
	private val _errorState: MutableLiveData<MainActivityErrorState> = MutableLiveData(MainActivityErrorState.Initial)
	val errorState: LiveData<MainActivityErrorState> = _errorState

	private var expressionState: Int = 0

	companion object {

		private val EMPTY_STATE = MainActivityState.Content(
			atomList = mutableListOf(AtomOfExpression("", TokenType.Empty)),
			expression = ""
		)

	}

	fun initState() {
		_state.value = EMPTY_STATE.copy()
		expressionState = 0
	}

	fun handleToken(inToken: MathToken) {
		val currentState = _state.value as? MainActivityState.Content ?: return
		val lastAtom = if(currentState.atomList.isEmpty()) AtomOfExpression("", TokenType.Empty) else currentState.atomList.last()

		val token = if(inToken == MathToken.B_MINUS && expressionState == 0) MathToken.IU_MINUS else inToken

		when(expressionState) {
			0 		-> {
				when(token.type) {
					TokenType.Int 						-> {
						expressionState = 1
						currentState.expression += token.value
						currentState.atomList.add(AtomOfExpression(token.value, token.type))
					}
					TokenType.InfixUnaryOperation 		-> { //todo
						expressionState = 0
						currentState.expression += token.value
						currentState.atomList.add(AtomOfExpression(token.value, token.type))
					}
					TokenType.InfixBinaryOperation 		-> { //todo
						expressionState = 0
						currentState.expression += token.value
						currentState.atomList.add(AtomOfExpression(token.value, token.type))
					}
					else 								-> {}
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
						lastAtom.value = token.value
						lastAtom.type = token.type
					}
					TokenType.PostfixUnaryOperation 	-> {
						expressionState = 4
						currentState.expression += " ${token.value} "
						currentState.atomList.add(AtomOfExpression(token.value, token.type))
					}
					TokenType.PostfixBinaryOperation 	-> {
						expressionState = 0
						currentState.expression += " ${token.value} "
						currentState.atomList.add(AtomOfExpression(token.value, token.type))
					}
					else 								-> {}
				}
			}
			2 		-> {
				when(token.type) {
					TokenType.Int 						-> {
						expressionState = 3
						currentState.expression += token.value
						lastAtom.value = token.value
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
						lastAtom.value = token.value
						lastAtom.type = token.type
					}
					TokenType.PostfixBinaryOperation 	-> {
						expressionState = 0
						currentState.expression += " ${token.value} "
						currentState.atomList.add(AtomOfExpression(token.value, token.type))
					}
					TokenType.PostfixUnaryOperation 	-> {
						expressionState = 4
						currentState.expression += " ${token.value} "
						currentState.atomList.add(AtomOfExpression(token.value, token.type))
					}
					else 								-> {}
				}
			}
			4		-> {
				when(token.type) {
					TokenType.PostfixBinaryOperation	-> {
						expressionState = 0
						currentState.expression += " ${token.value} "
						currentState.atomList.add(AtomOfExpression(token.value, token.type))
					}
					TokenType.PostfixUnaryOperation 	-> {
						expressionState = 4
						currentState.expression += " ${token.value} "
						currentState.atomList.add(AtomOfExpression(token.value, token.type))
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