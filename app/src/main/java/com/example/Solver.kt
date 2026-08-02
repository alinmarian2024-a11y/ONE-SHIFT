package com.example

import java.util.ArrayDeque

object Solver {
    fun getHintMove(
        currentBoard: List<List<PieceType>>,
        targetBoard: List<List<PieceType>>,
        fallbackPath: List<Move>
    ): Move? {
        if (currentBoard == targetBoard) return null

        // Attempt Bidirectional BFS
        val shortestPath = findShortestPath(currentBoard, targetBoard, maxDepth = 6)
        val pathToVerify = shortestPath ?: fallbackPath

        if (pathToVerify.isEmpty()) return null

        // Verify the path
        var tempBoard = currentBoard
        for (move in pathToVerify) {
            tempBoard = if (move.isRow) shiftRow(tempBoard, move.index, move.direction)
            else shiftCol(tempBoard, move.index, move.direction)
        }

        if (tempBoard == targetBoard) {
            return pathToVerify.first()
        }

        return null
    }

    private fun findShortestPath(
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
