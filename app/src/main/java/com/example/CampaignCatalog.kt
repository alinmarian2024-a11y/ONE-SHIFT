package com.example

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
                val random = kotlin.random.Random(masterSeed)
                
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
                
                // Verify true shortest path is exactly config.moves
                val shortestPath = Solver.findShortestPath(candidate.initialPlayerBoard, candidate.targetBoard, config.moves)
                if (shortestPath == null || shortestPath.size != config.moves) {
                    continue
                }

                // Actually use the optimal shortest path as the solution (in case it differs from the generated one but has same length)
                candidate = candidate.copy(solutionMoves = shortestPath)

                // Reject duplicate sequences for the ENTIRE campaign
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
        }
        return result
    }
    
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
