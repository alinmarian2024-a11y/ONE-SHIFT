package com.example

object CampaignCatalog {
    // PRECOMPUTED CATALOG
    val levels: List<PuzzleData> = listOf(
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 1, 1)),
            config = LevelConfig(2, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 1, 1)),
            config = LevelConfig(2, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 0, 1)),
            config = LevelConfig(2, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 1, 1)),
            config = LevelConfig(2, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, 1)),
            config = LevelConfig(2, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME), listOf(PieceType.LIME, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, 1), Move(true, 1, -1)),
            config = LevelConfig(2, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN)),
            solutionMoves = listOf(Move(false, 1, 1), Move(true, 1, -1)),
            config = LevelConfig(2, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 0, 1), Move(true, 0, -1)),
            config = LevelConfig(2, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, 1), Move(false, 1, -1)),
            config = LevelConfig(2, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 1, 1), Move(true, 0, -1)),
            config = LevelConfig(2, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 2, 1)),
            config = LevelConfig(3, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 2, 1)),
            config = LevelConfig(3, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 2, 1)),
            config = LevelConfig(3, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 0, 1)),
            config = LevelConfig(3, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.LIME, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.LIME, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 0, 1)),
            config = LevelConfig(3, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CORAL, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 0, -1), Move(false, 0, -1)),
            config = LevelConfig(3, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 0, -1), Move(false, 2, 1)),
            config = LevelConfig(3, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.LIME, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 2, 1), Move(true, 2, 1), Move(true, 1, -1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 1, -1), Move(false, 2, 1), Move(true, 0, 1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 1, 1), Move(false, 2, -1)),
            config = LevelConfig(3, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, -1), Move(true, 2, 1)),
            config = LevelConfig(3, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 1, -1), Move(false, 2, -1), Move(false, 0, -1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 0, -1), Move(true, 0, 1), Move(false, 0, 1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 2, 1), Move(false, 0, -1)),
            config = LevelConfig(3, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME)),
            solutionMoves = listOf(Move(true, 1, -1), Move(false, 2, 1)),
            config = LevelConfig(3, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.LIME, PieceType.LIME), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, -1), Move(true, 1, -1), Move(true, 2, 1), Move(false, 2, -1)),
            config = LevelConfig(3, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 0, 1), Move(true, 2, -1), Move(false, 2, 1), Move(true, 1, 1)),
            config = LevelConfig(3, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 0, 1), Move(false, 1, 1), Move(true, 0, -1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 2, 1), Move(false, 1, -1), Move(true, 2, 1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, 1), Move(false, 0, 1), Move(true, 2, 1), Move(false, 1, 1)),
            config = LevelConfig(3, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, 1), Move(true, 1, -1), Move(false, 0, 1), Move(true, 1, -1)),
            config = LevelConfig(3, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 0, 1), Move(true, 2, 1), Move(false, 0, 1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 0, 1), Move(true, 1, -1), Move(false, 2, 1), Move(true, 0, 1)),
            config = LevelConfig(3, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 0, -1), Move(true, 1, -1), Move(true, 2, -1), Move(false, 0, -1)),
            config = LevelConfig(3, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 1, 1), Move(false, 2, -1), Move(false, 0, 1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.LIME), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 0, -1), Move(false, 1, 1), Move(true, 2, 1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, -1), Move(true, 2, -1), Move(false, 2, -1), Move(true, 2, -1)),
            config = LevelConfig(3, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.LIME, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME)),
            solutionMoves = listOf(Move(true, 0, -1), Move(true, 2, -1), Move(false, 2, -1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 0, 1), Move(false, 1, -1), Move(false, 2, -1), Move(true, 2, -1)),
            config = LevelConfig(3, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN)),
            solutionMoves = listOf(Move(false, 2, -1), Move(true, 2, 1), Move(false, 2, -1)),
            config = LevelConfig(3, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.LIME), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.LIME, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 1, -1)),
            config = LevelConfig(4, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.LIME, PieceType.LIME, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.LIME)),
            solutionMoves = listOf(Move(true, 1, -1), Move(false, 3, 1), Move(false, 1, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, -1), Move(false, 2, -1), Move(true, 3, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.LIME, PieceType.CORAL), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN)),
            solutionMoves = listOf(Move(false, 0, 1)),
            config = LevelConfig(4, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CORAL, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 0, 1), Move(false, 2, 1), Move(true, 2, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 2, 1), Move(false, 3, 1), Move(false, 0, -1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 1, 1)),
            config = LevelConfig(4, 1)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.LIME, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, 1), Move(true, 1, 1), Move(false, 0, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CORAL, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 1, -1), Move(false, 3, 1)),
            config = LevelConfig(4, 2)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 1, -1), Move(false, 3, 1), Move(false, 1, -1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.LIME, PieceType.LIME), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CORAL, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.CORAL, PieceType.LIME), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 1, -1), Move(false, 0, -1), Move(false, 2, -1), Move(false, 3, -1), Move(true, 1, -1), Move(false, 0, -1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 1, 1), Move(false, 0, 1), Move(true, 1, 1), Move(false, 1, 1)),
            config = LevelConfig(4, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CORAL, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN)),
            solutionMoves = listOf(Move(false, 0, -1), Move(false, 3, 1), Move(true, 0, -1), Move(true, 3, 1), Move(true, 2, -1), Move(true, 1, -1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.LIME, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, -1), Move(true, 3, -1), Move(false, 1, 1), Move(false, 0, -1)),
            config = LevelConfig(4, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 1, 1), Move(false, 2, -1), Move(true, 0, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.LIME), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 2, 1), Move(false, 2, 1), Move(false, 1, -1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.LIME, PieceType.LIME)),
            solutionMoves = listOf(Move(true, 0, 1), Move(true, 3, -1), Move(false, 1, -1), Move(true, 2, -1), Move(false, 3, 1), Move(false, 2, -1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.LIME, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 0, -1), Move(false, 3, 1), Move(true, 1, 1), Move(false, 1, -1), Move(true, 0, 1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 1, -1), Move(true, 3, -1), Move(false, 0, -1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.CYAN, PieceType.LIME), listOf(PieceType.LIME, PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.CORAL, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CORAL, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.LIME, PieceType.CYAN)),
            solutionMoves = listOf(Move(false, 1, 1), Move(true, 1, 1), Move(true, 3, -1), Move(false, 2, 1)),
            config = LevelConfig(4, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.LIME, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 0, 1), Move(true, 0, 1), Move(false, 1, 1), Move(false, 3, -1), Move(false, 2, -1), Move(true, 0, -1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.LIME, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 1, -1), Move(false, 3, -1), Move(true, 2, -1), Move(false, 3, -1), Move(true, 2, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.LIME, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.LIME, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 1, 1), Move(true, 1, -1), Move(true, 3, 1), Move(true, 0, -1), Move(true, 0, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.CYAN)),
            solutionMoves = listOf(Move(false, 1, 1), Move(false, 2, 1), Move(true, 2, -1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.LIME, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.LIME, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.LIME, PieceType.LIME)),
            solutionMoves = listOf(Move(true, 2, -1), Move(false, 3, -1), Move(true, 1, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 0, 1), Move(true, 2, 1), Move(false, 0, -1), Move(false, 3, 1), Move(false, 2, 1), Move(true, 3, 1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.LIME, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.LIME, PieceType.LIME, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, 1), Move(false, 3, 1), Move(true, 2, -1), Move(true, 1, -1), Move(true, 1, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 1, -1), Move(false, 3, 1), Move(false, 0, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN, PieceType.LIME), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 2, 1), Move(true, 3, -1), Move(false, 3, -1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL, PieceType.LIME), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.LIME, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 1, -1), Move(false, 3, -1), Move(true, 2, 1), Move(false, 1, -1), Move(true, 3, -1), Move(false, 3, 1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.LIME, PieceType.LIME, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 0, -1), Move(false, 0, 1), Move(false, 1, 1), Move(false, 3, 1), Move(false, 1, 1), Move(true, 3, 1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CORAL, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CORAL, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 2, 1), Move(true, 3, 1), Move(false, 1, 1), Move(false, 0, 1)),
            config = LevelConfig(4, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.LIME, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 1, 1), Move(true, 2, -1), Move(false, 1, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CORAL, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 2, -1), Move(true, 2, 1), Move(false, 1, 1), Move(true, 3, 1), Move(false, 2, -1), Move(false, 0, 1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CORAL, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, 1), Move(false, 1, 1), Move(true, 2, -1), Move(true, 1, 1), Move(false, 2, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME, PieceType.LIME), listOf(PieceType.LIME, PieceType.CORAL, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 3, -1), Move(false, 0, 1), Move(false, 1, -1), Move(true, 3, -1), Move(true, 2, 1), Move(true, 0, 1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CORAL, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, 1), Move(true, 2, 1), Move(false, 3, 1), Move(true, 0, -1), Move(false, 0, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.LIME, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CORAL, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 1, -1), Move(true, 3, 1), Move(false, 2, -1), Move(true, 2, 1)),
            config = LevelConfig(4, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.LIME, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL, PieceType.LIME)),
            solutionMoves = listOf(Move(true, 0, 1), Move(false, 3, -1), Move(true, 2, -1), Move(true, 1, -1), Move(true, 0, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 0, -1), Move(true, 2, 1), Move(true, 2, 1), Move(false, 2, 1), Move(false, 1, 1), Move(false, 0, 1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.LIME), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 0, 1), Move(true, 3, -1), Move(false, 1, 1), Move(false, 0, 1), Move(true, 3, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 2, 1), Move(false, 2, 1), Move(false, 0, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 1, -1), Move(true, 2, 1), Move(false, 1, -1), Move(true, 3, 1), Move(false, 2, 1), Move(false, 1, -1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.LIME)),
            solutionMoves = listOf(Move(true, 0, 1), Move(true, 3, 1), Move(false, 3, 1), Move(false, 1, -1)),
            config = LevelConfig(4, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 2, -1), Move(true, 1, -1), Move(true, 3, -1), Move(false, 2, 1), Move(true, 3, -1), Move(true, 2, 1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 0, 1), Move(true, 2, -1), Move(false, 3, -1), Move(false, 2, 1), Move(false, 0, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CORAL, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, 1), Move(false, 0, 1), Move(false, 1, -1), Move(true, 2, 1), Move(true, 0, -1), Move(false, 2, -1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, 1), Move(true, 2, 1), Move(false, 3, 1), Move(false, 0, 1), Move(true, 1, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET)),
            solutionMoves = listOf(Move(false, 2, -1), Move(true, 3, -1), Move(true, 0, -1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.LIME, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.LIME, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 2, 1), Move(false, 0, 1), Move(false, 2, 1), Move(true, 2, -1)),
            config = LevelConfig(4, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.LIME, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.LIME), listOf(PieceType.LIME, PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CORAL, PieceType.CORAL, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 1, 1), Move(true, 2, 1), Move(false, 0, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, 1), Move(false, 0, 1), Move(false, 1, -1), Move(true, 0, -1), Move(false, 1, 1), Move(true, 0, -1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, 1), Move(true, 0, -1), Move(true, 0, -1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CYAN, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.LIME, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME, PieceType.VIOLET, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 3, 1), Move(false, 0, 1), Move(true, 1, -1), Move(false, 3, -1), Move(false, 3, -1), Move(false, 2, -1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.LIME, PieceType.CORAL, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.VIOLET, PieceType.LIME, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, 1), Move(false, 0, 1), Move(true, 3, 1), Move(false, 1, -1), Move(false, 1, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CORAL, PieceType.CORAL)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 1, -1), Move(false, 1, 1), Move(false, 2, 1), Move(false, 0, 1), Move(true, 0, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CORAL, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CORAL, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 0, 1), Move(true, 2, -1), Move(false, 0, 1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CYAN, PieceType.LIME, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 0, -1), Move(false, 2, 1), Move(false, 3, -1), Move(true, 1, -1)),
            config = LevelConfig(4, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME, PieceType.CORAL), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.LIME, PieceType.CYAN, PieceType.CORAL)),
            solutionMoves = listOf(Move(true, 0, -1), Move(true, 2, -1), Move(false, 3, 1), Move(true, 0, 1), Move(false, 1, 1), Move(true, 1, 1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.LIME, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CYAN, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 3, -1), Move(false, 3, 1), Move(true, 1, -1), Move(false, 2, 1)),
            config = LevelConfig(4, 4)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.LIME, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.LIME, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CORAL, PieceType.CYAN, PieceType.CYAN), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.CYAN)),
            solutionMoves = listOf(Move(true, 0, -1), Move(true, 1, 1), Move(true, 1, 1), Move(true, 2, 1), Move(false, 3, 1), Move(false, 2, 1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET), listOf(PieceType.CORAL, PieceType.LIME, PieceType.LIME, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.LIME, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CORAL, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CORAL, PieceType.CORAL, PieceType.CYAN, PieceType.CORAL), listOf(PieceType.CORAL, PieceType.CYAN, PieceType.CYAN, PieceType.LIME)),
            solutionMoves = listOf(Move(false, 2, -1), Move(true, 1, 1), Move(false, 0, -1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN), listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME)),
            initialPlayerBoard = listOf(listOf(PieceType.CYAN, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.VIOLET, PieceType.CYAN, PieceType.LIME), listOf(PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, 1), Move(false, 1, 1), Move(false, 2, 1), Move(true, 3, -1), Move(true, 2, -1)),
            config = LevelConfig(4, 5)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CYAN, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL, PieceType.LIME), listOf(PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.CYAN, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CYAN, PieceType.LIME), listOf(PieceType.VIOLET, PieceType.VIOLET, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CORAL, PieceType.CORAL, PieceType.VIOLET)),
            solutionMoves = listOf(Move(true, 0, -1), Move(false, 0, 1), Move(true, 2, -1), Move(false, 1, -1), Move(false, 0, -1), Move(false, 0, -1)),
            config = LevelConfig(4, 6)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.LIME, PieceType.CYAN, PieceType.VIOLET, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.VIOLET, PieceType.CORAL, PieceType.CYAN), listOf(PieceType.VIOLET, PieceType.LIME, PieceType.VIOLET, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.LIME, PieceType.CYAN, PieceType.CORAL, PieceType.CYAN)),
            solutionMoves = listOf(Move(false, 2, -1), Move(false, 3, -1), Move(false, 2, -1)),
            config = LevelConfig(4, 3)
        ),
        PuzzleData(
            targetBoard = listOf(listOf(PieceType.LIME, PieceType.CORAL, PieceType.LIME, PieceType.CORAL), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.VIOLET, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.CORAL), listOf(PieceType.LIME, PieceType.LIME, PieceType.CYAN, PieceType.CYAN)),
            initialPlayerBoard = listOf(listOf(PieceType.CORAL, PieceType.LIME, PieceType.LIME, PieceType.VIOLET), listOf(PieceType.VIOLET, PieceType.CYAN, PieceType.CORAL, PieceType.CORAL), listOf(PieceType.CYAN, PieceType.CYAN, PieceType.LIME, PieceType.CYAN), listOf(PieceType.LIME, PieceType.LIME, PieceType.CYAN, PieceType.CORAL)),
            solutionMoves = listOf(Move(false, 2, -1), Move(true, 0, -1), Move(false, 3, 1), Move(false, 2, 1)),
            config = LevelConfig(4, 4)
        )
    )
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
