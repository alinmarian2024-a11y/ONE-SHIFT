package com.example

import java.util.ArrayDeque
import kotlin.random.Random
import java.io.File

enum class PieceType { CYAN, VIOLET, CORAL, LIME }
data class LevelConfig(val size: Int, val moves: Int)
data class Move(val isRow: Boolean, val index: Int, val direction: Int)
data class PuzzleData(
    val targetBoard: List<List<PieceType>>,
    val initialPlayerBoard: List<List<PieceType>>,
    val solutionMoves: List<Move>,
    val config: LevelConfig
)

fun shiftRow(board: List<List<PieceType>>, rowIndex: Int, direction: Int): List<List<PieceType>> {
    val newBoard = board.map { it.toMutableList() }.toMutableList()
    val row = newBoard[rowIndex]
    if (direction > 0) { // right
        val last = row.removeAt(row.size - 1)
        row.add(0, last)
    } else { // left
        val first = row.removeAt(0)
        row.add(first)
    }
    return newBoard
}

fun shiftCol(board: List<List<PieceType>>, colIndex: Int, direction: Int): List<List<PieceType>> {
    val newBoard = board.map { it.toMutableList() }.toMutableList()
    val col = (0 until newBoard.size).map { newBoard[it][colIndex] }.toMutableList()
    if (direction > 0) { // down
        val last = col.removeAt(col.size - 1)
        col.add(0, last)
    } else { // up
        val first = col.removeAt(0)
        col.add(first)
    }
    for (i in 0 until newBoard.size) {
        newBoard[i][colIndex] = col[i]
    }
    return newBoard
}

object Solver {
    fun findShortestPath(
        startBoard: List<List<PieceType>>,
        targetBoard: List<List<PieceType>>,
        maxDepth: Int
    ): List<Move>? {
        val size = startBoard.size
        val allMoves = mutableListOf<Move>()
        for (i in 0 until size) {
            allMoves.add(Move(true, i, 1))
            allMoves.add(Move(true, i, -1))
            allMoves.add(Move(false, i, 1))
            allMoves.add(Move(false, i, -1))
        }

        val forwardQueue = ArrayDeque<List<List<PieceType>>>()
        val backwardQueue = ArrayDeque<List<List<PieceType>>>()
        val forwardVisited = mutableMapOf<List<List<PieceType>>, Pair<Move?, List<List<PieceType>>>>()
        val backwardVisited = mutableMapOf<List<List<PieceType>>, Pair<Move?, List<List<PieceType>>>>()
        val forwardDepth = mutableMapOf<List<List<PieceType>>, Int>()
        val backwardDepth = mutableMapOf<List<List<PieceType>>, Int>()

        forwardQueue.add(startBoard)
        forwardVisited[startBoard] = Pair(null, startBoard)
        forwardDepth[startBoard] = 0

        backwardQueue.add(targetBoard)
        backwardVisited[targetBoard] = Pair(null, targetBoard)
        backwardDepth[targetBoard] = 0

        var intersect: List<List<PieceType>>? = null

        while (forwardQueue.isNotEmpty() && backwardQueue.isNotEmpty()) {
            val currentForward = forwardQueue.first()
            val fDepth = forwardDepth[currentForward]!!
            val currentBackward = backwardQueue.first()
            val bDepth = backwardDepth[currentBackward]!!

            if (fDepth + bDepth > maxDepth) break

            if (fDepth <= bDepth) {
                val curr = forwardQueue.removeFirst()
                if (backwardVisited.containsKey(curr)) {
                    intersect = curr
                    break
                }
                if (fDepth < (maxDepth + 1) / 2) {
                    for (move in allMoves) {
                        val nextBoard = if (move.isRow) shiftRow(curr, move.index, move.direction)
                        else shiftCol(curr, move.index, move.direction)
                        
                        if (!forwardVisited.containsKey(nextBoard)) {
                            forwardVisited[nextBoard] = Pair(move, curr)
                            forwardDepth[nextBoard] = fDepth + 1
                            forwardQueue.add(nextBoard)
                        }
                    }
                }
            } else {
                val curr = backwardQueue.removeFirst()
                if (forwardVisited.containsKey(curr)) {
                    intersect = curr
                    break
                }
                if (bDepth < maxDepth / 2) {
                    for (move in allMoves) {
                        val nextBoard = if (move.isRow) shiftRow(curr, move.index, move.direction)
                        else shiftCol(curr, move.index, move.direction)
                        
                        if (!backwardVisited.containsKey(nextBoard)) {
                            backwardVisited[nextBoard] = Pair(move, curr)
                            backwardDepth[nextBoard] = bDepth + 1
                            backwardQueue.add(nextBoard)
                        }
                    }
                }
            }
        }

        if (intersect == null) {
            for (b in forwardQueue) {
                if (backwardVisited.containsKey(b)) { intersect = b; break }
            }
            if (intersect == null) {
                for (b in backwardQueue) {
                    if (forwardVisited.containsKey(b)) { intersect = b; break }
                }
            }
        }

        if (intersect != null) {
            val path = mutableListOf<Move>()
            var curr = intersect
            while (curr != startBoard) {
                val pair = forwardVisited[curr]!!
                path.add(pair.first!!)
                curr = pair.second
            }
            path.reverse()
            curr = intersect
            while (curr != targetBoard) {
                val pair = backwardVisited[curr]!!
                val move = pair.first!!
                path.add(move.copy(direction = -move.direction))
                curr = pair.second
            }
            return path
        }
        return null
    }
}

object CampaignCatalog {
    private const val TOTAL_LEVELS = 106
    
    val levels: List<PuzzleData> by lazy {
        generateCatalog()
    }

    private fun canonicalPair(start: List<List<PieceType>>, target: List<List<PieceType>>): String {
        val types = listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.LIME)
        val permutations = mutableListOf<Map<PieceType, PieceType>>()
        for (i in types.indices) {
            for (j in types.indices) {
                if (j == i) continue
                for (k in types.indices) {
                    if (k == i || k == j) continue
                    for (l in types.indices) {
                        if (l == i || l == j || l == k) continue
                        permutations.add(
                            mapOf(
                                types[0] to types[i],
                                types[1] to types[j],
                                types[2] to types[k],
                                types[3] to types[l]
                            )
                        )
                    }
                }
            }
        }
        
        var bestCanonical: String? = null
        
        for (perm in permutations) {
            val s = start.joinToString(";") { row -> row.joinToString(",") { perm[it]?.name ?: it.name } }
            val t = target.joinToString(";") { row -> row.joinToString(",") { perm[it]?.name ?: it.name } }
            val rep = "$s|$t"
            if (bestCanonical == null || rep < bestCanonical) {
                bestCanonical = rep
            }
        }
        return bestCanonical!!
    }

    private fun generateCatalog(): List<PuzzleData> {
        val result = mutableListOf<PuzzleData>()
        val seenPairs = mutableSetOf<String>()
        val recentSolutions = mutableListOf<List<Move>>()
        val allSolutions = mutableSetOf<List<Move>>()
        
        var previousCount = -1
        var consecutiveCount = 0

        var masterSeed = 2000L

        for (level in 1..TOTAL_LEVELS) {
            var candidate: PuzzleData
            while (true) {
                masterSeed++
                val random = Random(masterSeed)
                
                val config = if (level <= 15) {
                    when (level) {
                        1 -> LevelConfig(2, 1)
                        in 2..5 -> LevelConfig(2, 1)
                        in 6..10 -> LevelConfig(2, 2)
                        in 11..15 -> LevelConfig(3, 1)
                        else -> LevelConfig(3, 1)
                    }
                } else {
                    val possibleMoves = when (level) {
                        in 16..25 -> 2..3
                        in 26..40 -> 3..4
                        in 41..50 -> 1..3
                        else -> 3..6
                    }
                    
                    var chosenMoves = possibleMoves.random(random)
                    if (chosenMoves == previousCount && consecutiveCount >= 2) {
                        val others = possibleMoves.filter { it != previousCount }
                        if (others.isNotEmpty()) {
                            chosenMoves = others.random(random)
                        }
                    }
                    LevelConfig(if (level <= 40) 3 else 4, chosenMoves)
                }
                
                candidate = generatePuzzleInternal(config, random)
                
                val cPair = canonicalPair(candidate.initialPlayerBoard, candidate.targetBoard)
                if (seenPairs.contains(cPair)) {
                    continue
                }
                
                val shortestPath = Solver.findShortestPath(candidate.initialPlayerBoard, candidate.targetBoard, config.moves)
                if (shortestPath == null || shortestPath.size != config.moves) {
                    continue
                }

                candidate = candidate.copy(solutionMoves = shortestPath)

                if (allSolutions.contains(candidate.solutionMoves)) {
                    continue
                }
                
                if (config.moves == previousCount) {
                    consecutiveCount++
                } else {
                    previousCount = config.moves
                    consecutiveCount = 1
                }
                
                seenPairs.add(cPair)
                allSolutions.add(candidate.solutionMoves)
                recentSolutions.add(candidate.solutionMoves)
                if (recentSolutions.size > 5) {
                    recentSolutions.removeAt(0)
                }
                result.add(candidate)
                break
            }
            println("Generated level $level")
        }
        return result
    }
    
    fun generatePuzzleInternal(config: LevelConfig, random: Random): PuzzleData {
        var targetBoard: List<List<PieceType>>
        var playerBoard: List<List<PieceType>>
        var solutionMoves: List<Move>
        do {
            targetBoard = (0 until config.size).map {
                (0 until config.size).map { PieceType.entries.random(random) }
            }
            val moves = mutableListOf<Move>()
            var currentBoard = targetBoard
            var lastMove: Move? = null
            for (i in 0 until config.moves) {
                var move: Move
                do {
                    val isRow = random.nextBoolean()
                    val index = random.nextInt(config.size)
                    val dir = if (random.nextBoolean()) 1 else -1
                    move = Move(isRow, index, dir)
                } while (lastMove != null && lastMove.isRow == move.isRow && lastMove.index == move.index && lastMove.direction == -move.direction)
                moves.add(move)
                currentBoard = if (move.isRow) shiftRow(currentBoard, move.index, move.direction) else shiftCol(currentBoard, move.index, move.direction)
                lastMove = move
            }
            playerBoard = currentBoard
            solutionMoves = moves.reversed().map { it.copy(direction = -it.direction) }
        } while (playerBoard == targetBoard)
        return PuzzleData(targetBoard, playerBoard, solutionMoves, config)
    }
}

fun main() {
    val levels = CampaignCatalog.levels
    val sb = StringBuilder()
    sb.append("package com.example\n\n")
    sb.append("object CampaignCatalog {\n")
    sb.append("    val levels: List<PuzzleData> = listOf(\n")
    for (level in levels) {
        sb.append("        PuzzleData(\n")
        val tb = level.targetBoard.joinToString(", ") { row -> "listOf(${row.joinToString(", ") { "PieceType.${it.name}" }})" }
        sb.append("            targetBoard = listOf($tb),\n")
        val pb = level.initialPlayerBoard.joinToString(", ") { row -> "listOf(${row.joinToString(", ") { "PieceType.${it.name}" }})" }
        sb.append("            initialPlayerBoard = listOf($pb),\n")
        val sm = level.solutionMoves.joinToString(", ") { move -> "Move(${move.isRow}, ${move.index}, ${move.direction})" }
        sb.append("            solutionMoves = listOf($sm),\n")
        sb.append("            config = LevelConfig(${level.config.size}, ${level.config.moves})\n")
        sb.append("        ),\n")
    }
    sb.append("    )\n")
    sb.append("""
    fun generatePuzzleInternal(config: LevelConfig, random: kotlin.random.Random): PuzzleData {
        var targetBoard: List<List<PieceType>>
        var playerBoard: List<List<PieceType>>
        var solutionMoves: List<Move>
        do {
            targetBoard = (0 until config.size).map {
                (0 until config.size).map { PieceType.entries.random(random) }
            }
            val moves = mutableListOf<Move>()
            var currentBoard = targetBoard
            var lastMove: Move? = null
            for (i in 0 until config.moves) {
                var move: Move
                do {
                    val isRow = random.nextBoolean()
                    val index = random.nextInt(config.size)
                    val dir = if (random.nextBoolean()) 1 else -1
                    move = Move(isRow, index, dir)
                } while (lastMove != null && lastMove.isRow == move.isRow && lastMove.index == move.index && lastMove.direction == -move.direction)
                moves.add(move)
                currentBoard = if (move.isRow) shiftRow(currentBoard, move.index, move.direction) else shiftCol(currentBoard, move.index, move.direction)
                lastMove = move
            }
            playerBoard = currentBoard
            solutionMoves = moves.reversed().map { it.copy(direction = -it.direction) }
        } while (playerBoard == targetBoard)
        return PuzzleData(targetBoard, playerBoard, solutionMoves, config)
    }
}
""")
    File("app/src/main/java/com/example/CampaignCatalog.kt").writeText(sb.toString())
    println("Successfully overwrote CampaignCatalog.kt")
}
