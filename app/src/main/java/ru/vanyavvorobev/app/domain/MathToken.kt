package ru.vanyavvorobev.app.domain

enum class MathToken(val value: String, val type: TokenType) {
	EMPTY				(value = "", type = TokenType.Empty),


	ZERO				(value = "0", type = TokenType.Int),
	ONE					(value = "1", type = TokenType.Int),
	TWO					(value = "2", type = TokenType.Int),
	THREE				(value = "3", type = TokenType.Int),
	FOUR				(value = "4", type = TokenType.Int),
	FIVE				(value = "5", type = TokenType.Int),
	SIX					(value = "6", type = TokenType.Int),
	SEVEN				(value = "7", type = TokenType.Int),
	EIGHT				(value = "8", type = TokenType.Int),
	NINE				(value = "9", type = TokenType.Int),

	POINT				(value = ",", type = TokenType.Point),

	IU_MINUS			(value = "-", type = TokenType.InfixUnaryOperation),
	PU_MINUS			(value = "* (-1)", type = TokenType.PostfixUnaryOperation),
	PERCENT				(value = "* (0,01)", type = TokenType.PostfixUnaryOperation),

	PLUS				(value = "+", type = TokenType.PostfixBinaryOperation),
	B_MINUS				(value = "-", type = TokenType.PostfixBinaryOperation),
	MULTIPLY			(value = "*", type = TokenType.PostfixBinaryOperation),
	DIVIDE				(value = "/", type = TokenType.PostfixBinaryOperation),

	RESULT				(value = "", type = TokenType.Empty),
	AC					(value = "", type = TokenType.Empty),
	BACKSPACE			(value = "", type = TokenType.Empty)
}

enum class TokenType {
	Empty,
	Int,
	Double,
	Point,
	InfixUnaryOperation,
	InfixBinaryOperation,
	PostfixUnaryOperation,
	PostfixBinaryOperation,

	OpenBracket,
	CloseBracket
}
