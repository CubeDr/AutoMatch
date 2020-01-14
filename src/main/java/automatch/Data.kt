package automatch

import java.io.File

data class Player(val name: String, val skill: Int, val gender: String)

fun getPlayerListFromFile(filename: String): List<Player> = File(filename).bufferedReader().readLines()
        .map {
            val (name, skill, gender) = it.split(",").map { it.trim() }
            Player(name, skill.toInt(), gender)
        }

data class Game(val player1: Player, val player2: Player, val player3: Player, val player4: Player) {
    val players = setOf(player1, player2, player3, player4)

    val isValid = !((player1 == player2) || (player1 == player3) || (player1 == player4)
                    || (player2 == player3) || (player2 == player4) || (player3 == player4))

    val score = players.map { it.skill }.let { (it.min()!!) - (it.max()!!) }


    fun contains(player: Player) = players.contains(player)
    fun collideWith(game: Game) = players.intersect(game.players).isNotEmpty()
}

data class GameRow(val games: List<Game>) {
    val players: Set<Player> = games.fold(mutableSetOf()) { acc, game ->
        acc += game.players
        acc
    }

    val isValid: Boolean
        get() {
            val allGameValid = games.fold(true) { acc, game -> acc && game.isValid }
            val allPlayerValid = games.foldIndexed(true) { index, acc, game ->
                acc && games.foldIndexed(true) { i, a, g ->
                    if(i == index) a
                    else a && !game.collideWith(g)
                }
            }
            return allGameValid && allPlayerValid
        }

    val score = games.sumBy { it.score }

    fun contains(player: Player) = games.fold(true) { acc, game -> acc && game.contains(player) }
}

data class Match(
        val gameRows: List<GameRow>,
        val players: Set<Player> = gameRows.fold(mutableSetOf()) { acc, gameRow ->
            acc += gameRow.players
            acc
        }
) {
    val isValid = gameRows.fold(true) { acc, gameRow -> acc && gameRow.isValid }
    val score: Int
        get() {
            val gameScore = gameRows.sumBy { it.score }
            val playerScore = 100 * players.sumBy { p -> gameRows.count { it.contains(p) } }
            return gameScore + playerScore
        }
}