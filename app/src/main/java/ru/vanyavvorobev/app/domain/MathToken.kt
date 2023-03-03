package ru.vanyavvorobev.app.domain

enum class MathToken(val value: String, val type: TokenType, val priority: Int) {
	EMPTY				(value = "", type = TokenType.Empty, priority = 0),


	ZERO				(value = "0", type = TokenType.Int, priority = 0),
	ONE					(value = "1", type = TokenType.Int, priority = 0),
	TWO					(value = "2", type = TokenType.Int, priority = 0),
	THREE				(value = "3", type = TokenType.Int, priority = 0),
	FOUR				(value = "4", type = TokenType.Int, priority = 0),
	FIVE				(value = "5", type = TokenType.Int, priority = 0),
	SIX					(value = "6", type = TokenType.Int, priority = 0),
	SEVEN				(value = "7", type = TokenType.Int, priority = 0),
	EIGHT				(value = "8", type = TokenType.Int, priority = 0),
	NINE				(value = "9", type = TokenType.Int, priority = 0),

	POINT				(value = ",", type = TokenType.Point, priority = 0),

	PF_MINUS			(value = "-", type = TokenType.PrefixFunction, priority = 0),

	P_MINUS				(value = "* -1", type = TokenType.PostfixFunction, priority = 0),
	PERCENT				(value = "* 0,01", type = TokenType.PostfixFunction, priority = 0),

	PLUS				(value = "+", type = TokenType.PostfixBinaryOperation, priority = 1),
	B_MINUS				(value = "-", type = TokenType.PostfixBinaryOperation, priority = 1),
	MULTIPLY			(value = "*", type = TokenType.PostfixBinaryOperation, priority = 2),
	DIVIDE				(value = "/", type = TokenType.PostfixBinaryOperation, priority = 2),

	RESULT				(value = "", type = TokenType.Empty, priority = 0),
	AC					(value = "", type = TokenType.Empty, priority = 0),
	BACKSPACE			(value = "", type = TokenType.Empty, priority = 0)
}

enum class TokenType {
	Empty,
	Int,
	Double,
	Point,

	PostfixBinaryOperation,

	PrefixFunction,
	PostfixFunction,

	OpenBracket,
	CloseBracket
}
