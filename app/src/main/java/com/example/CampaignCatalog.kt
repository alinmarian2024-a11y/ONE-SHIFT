package com.example

object CampaignCatalog {
    private const val TOTAL_LEVELS = 106
    
    val levels: List<PuzzleData> by lazy {
        generateCatalog()
    }

    private fun generateCatalog(): List<PuzzleData> {
        val result = mutableListOf<PuzzleData>()
        val seenPairs = mutableSetOf<Pair<List<List<PieceType>>, List<List<PieceType>>>>()
        val recentSolutions = mutableListOf<List<Move>>()
        
        var previousCount = -1
        var consecutiveCount = 0

        var masterSeed = 1000L

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
                
                val pair = Pair(candidate.initialPlayerBoard, candidate.targetBoard)
                if (seenPairs.contains(pair)) {
                    continue
                }
                
                if (level > 15) {
                    var matchesRecent = false
                    for (recentSolution in recentSolutions) {
                        if (candidate.solutionMoves == recentSolution) {
                            matchesRecent = true
                            break
                        }
                    }
                    if (matchesRecent) {
                        continue
                    }
                }
                
                if (config.moves == previousCount) {
                    consecutiveCount++
                } else {
                    previousCount = config.moves
                    consecutiveCount = 1
                }
                
                seenPairs.add(pair)
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
