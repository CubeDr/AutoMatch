import automatch.Game
import automatch.GameRow
import automatch.Player
import automatch.getPlayerListFromFile

fun generateValidGames(players: List<Player>): List<Game> {
    val validGames = mutableListOf<Game>()

    players.forEachIndexed { p1Idx, p1 ->
        players.forEachIndexedFrom(p1Idx + 1) { p2Idx, p2 ->
            players.forEachIndexedFrom(p2Idx + 1) { p3Idx, p3 ->
                players.forEachIndexedFrom(p3Idx + 1) { _, p4 ->
                    val g = Game(p1, p2, p3, p4)
                    if(g.isValid) validGames += g
                }
            }
        }
    }

    return validGames
}

fun dfsRow(games: List<Game>, courts: Int, result: MutableList<GameRow>, cur: MutableList<Game> = mutableListOf(), idx: Int = 0) {
    if(cur.size == courts) {
        val row = GameRow(cur)
        if(row.isValid) {
            result += row
            println(result.size)
        }
        return
    }

    games.forEachIndexedFrom(idx) { index, game ->
        val collide = cur.fold(false) { a, g -> a || g.collideWith(game) }
        if(!collide) {
            cur.add(game)
            dfsRow(games, courts, result, cur, index + 1)
            cur.removeAt(cur.lastIndex)
        }
    }
}

fun generateValidRows(games: List<Game>, courts: Int): List<GameRow> {
    val validRows = mutableListOf<GameRow>()

    dfsRow(games, courts, validRows)

    return validRows
}

fun main(args: Array<String>) {
    val players = getPlayerListFromFile("players.csv")

    val courts = 6
    val times = 12

    // 적당한 수의 게임만 남김
    val validGames = generateValidGames(players)
            .sortedByDescending { it.score }
            .subList(0, courts * courts * times * times)

    val validRows = generateValidRows(validGames, 6)
    validRows.forEach { println(it) }

}

fun<T> List<T>.forEachIndexedFrom(index: Int, action: (index: Int, item: T) -> Unit) {
    forEachIndexed { i, t ->
        if(i >= index) action.invoke(i, t)
    }
}