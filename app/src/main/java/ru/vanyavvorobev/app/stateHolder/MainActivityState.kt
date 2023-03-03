package ru.vanyavvorobev.app.stateHolder

import ru.vanyavvorobev.app.domain.AtomOfExpression

sealed class MainActivityState {

	object Initial: MainActivityState()

	data class Content(
		val atomList: MutableList<AtomOfExpression> = mutableListOf(),
		var expression: String = "",
		var result: String = ""
		): MainActivityState()

}
