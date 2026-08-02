package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class SolverTest {
    @Test
    fun testSolverWorks() {
        val sizes = listOf(2, 3, 4)
        for (size in sizes) {
            // Generate target board
            val targetBoard = (0 until size).map {
                (0 until size).map { PieceType.values().random() }
            }

            var currentBoard = targetBoard
            val moves = mutableListOf<Move>()

            // Apply 5 random moves
            for (i in 0 until 5) {
                val isRow = Random.nextBoolean()
                val index = Random.nextInt(size)
                val dir = if (Random.nextBoolean()) 1 else -1
                val move = Move(isRow, index, dir)
                moves.add(move)
                currentBoard = if (move.isRow) shiftRow(currentBoard, move.index, move.direction) else shiftCol(currentBoard, move.index, move.direction)
            }

            val fallback = moves.reversed().map { it.copy(direction = -it.direction) }

            // Use Solver to find shortest path
            val hint = Solver.getHintMove(currentBoard, targetBoard, fallback)
            assertTrue("Hint should not be null", hint != null)
            
            // Verify step by step
            var solveBoard = currentBoard
            var step = 0
            while (solveBoard != targetBoard && step < 20) {
                // Just use fallback for this test mock
                val h = Solver.getHintMove(solveBoard, targetBoard, fallback) ?: break
                solveBoard = if (h.isRow) shiftRow(solveBoard, h.index, h.direction) else shiftCol(solveBoard, h.index, h.direction)
                step++
            }
            assertEquals(targetBoard, solveBoard)
        }
    }
}
