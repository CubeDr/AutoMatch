import automatch.Game
import automatch.GameRow
import automatch.Player
import kotlin.test.Test
import kotlin.test.assertEquals

class DataTest {
    val players = listOf(
            Player("Player1", 10, "남"),
            Player("Player2", 9, "남"),
            Player("Player3", 8, "남"),
            Player("Player4", 7, "남"),
            Player("Player5", 6, "남"),
            Player("Player6", 6, "남"),
            Player("Player7", 6, "남"),
            Player("Player8", 6, "남"),
            Player("Player9", 6, "남"),
            Player("Player10", 6, "남"),
            Player("Player11", 6, "남"),
            Player("Player12", 6, "남")
    )

    @Test fun gameTest() {
        val goodGame = Game(players[0], players[1], players[2], players[3])
        assertEquals(goodGame.isValid, true)

        val badGame = Game(players[1], players[1], players[2], players[3])
        assertEquals(badGame.isValid, false)
    }

    @Test fun rowTest() {
        val goodRow = GameRow(listOf(
                Game(players[0], players[1], players[2], players[3]),
                Game(players[4], players[5], players[6], players[7]),
                Game(players[8], players[9], players[10], players[11])
        ))
        assertEquals(goodRow.isValid, true)

        val badGame = GameRow(listOf(
                Game(players[1], players[1], players[2], players[3]),
                Game(players[4], players[5], players[6], players[7]),
                Game(players[8], players[9], players[10], players[11])
        ))
        assertEquals(badGame.isValid, false)

        val badRow = GameRow(listOf(
                Game(players[0], players[1], players[2], players[3]),
                Game(players[4], players[5], players[6], players[7]),
                Game(players[0], players[9], players[10], players[11])
        ))
        assertEquals(badRow.isValid, false)
    }
}