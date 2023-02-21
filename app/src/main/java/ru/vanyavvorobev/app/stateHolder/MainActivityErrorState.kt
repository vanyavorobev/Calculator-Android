package ru.vanyavvorobev.app.stateHolder

sealed class MainActivityErrorState {

	object Initial: MainActivityErrorState()

	data class Content(
		val error: String
	): MainActivityErrorState()

}
