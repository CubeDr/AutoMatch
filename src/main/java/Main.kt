import automatch.Player
import automatch.getPlayerListFromFile

const val DUPLICATE_SCORE = 1000

fun gameScore(players: List<Player>): Int {
    // invalid game
    if(players.hasDuplicate()) return DUPLICATE_SCORE
    // game balance
    val balance = (players.maxBy { it.skill }!!).skill - (players.minBy { it.skill }!!).skill

    return balance
}

fun score(list: List<Player>, courts: Int): Int {
    var score = 0

    list.chunked(4 * courts) { row ->
        if(row.hasDuplicate()) score += DUPLICATE_SCORE
        row.chunked(4) { g ->
            if(g.size == 4) {
                score += gameScore(g)
            } else if(g.hasDuplicate()) // 같은 사람이 중복으로 제외된 경우
                score += DUPLICATE_SCORE
        }
        Unit
    }

    return score
}

fun step(match: MutableList<Player>, courts: Int) {
    val nextStep = match.toMutableList()
    var nextStepScore = score(nextStep, courts)

    for(p1Idx in 0 until match.size - 1) {
        for(p2Idx in (p1Idx + 1) until match.size) {
            val swapped = List(match.size) {
                when (it) {
                    p1Idx -> match[p2Idx]
                    p2Idx -> match[p1Idx]
                    else -> match[it]
                }
            }
            val swappedScore = score(swapped, courts)

            if(swappedScore < nextStepScore) {
                nextStep.clear()
                nextStep.addAll(swapped)
                nextStepScore = swappedScore
            }
        }
    }

    match.clear()
    match.addAll(nextStep)
}

fun optimize(players: List<Player>, courts: Int, times: Int): List<Player> {
    val matches = mutableListOf<Player>().apply {
        repeat(4 * courts * times / players.size) { addAll(players) }
    }.shuffled().toMutableList()

    var originalScore: Int
    do {
        originalScore = score(matches, courts)
        step(matches, courts)
    } while (originalScore != score(matches, courts))

    return matches
}

fun main(args: Array<String>) {
    val players = getPlayerListFromFile("players.csv")
    val courts = 3
    val times = 2

    repeat(5) {
        val matches = optimize(players, courts, times)

        matches.chunked(courts * 4) { row ->
            row.chunked(4) { g ->
                if(g.size >= 4) {
                    g.forEach { print(it.name + " ") }
                    print("\t|\t")
                }
            }
            println()
        }
        println()
    }
}

fun<T> List<T>.hasDuplicate() = toSet().size != size