package fr.vincentgrande.morpionnage

data class Game(val id: String? = null, val ownerId: String? = null,var gameBoard: GameBoard? = null, val playerId: String? = null, val winnerId:String? = null,val lapId: String? = null)