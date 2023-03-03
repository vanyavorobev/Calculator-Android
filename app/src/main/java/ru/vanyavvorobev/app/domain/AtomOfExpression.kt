package ru.vanyavvorobev.app.domain

data class AtomOfExpression(var value: String, var type: TokenType, val priority: Int)
