package com.example

import org.junit.Test
import java.io.File
import kotlin.random.Random

class GenTest {
    @Test
    fun generateStatic() {
        println("Starting generation...")
        val levels = CampaignCatalog.levels // this computes it
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
        val file = File("src/main/java/com/example/CampaignCatalog.kt")
        file.writeText(sb.toString())
        println("Successfully overwrote CampaignCatalog.kt at ${file.absolutePath}")
    }
}
