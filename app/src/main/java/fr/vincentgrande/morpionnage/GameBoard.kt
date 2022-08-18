package fr.vincentgrande.morpionnage

data class GameBoard(
    var topLine: List<Int>? = listOf<Int>(0,0,0),
    var middleLine: List<Int>? = listOf<Int>(0,0,0),
    var bottomLine: List<Int>? = listOf<Int>(0,0,0)
) {
}