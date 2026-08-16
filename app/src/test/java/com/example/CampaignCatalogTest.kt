package com.example

import org.junit.Test
import org.junit.Assert.*

class CampaignCatalogTest {
    
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

    @Test
    fun testCatalogUniquenessAndShortestPath() {
        val levels = CampaignCatalog.levels
        assertEquals("Should have exactly 106 levels", 106, levels.size)
        
        val seenPairs = mutableSetOf<String>()
        
        for ((index, level) in levels.withIndex()) {
            val cPair = canonicalPair(level.initialPlayerBoard, level.targetBoard)
            assertFalse("Level ${index + 1} is a duplicate!", seenPairs.contains(cPair))
            seenPairs.add(cPair)
            
            // Verify shortest path
            val path = Solver.findShortestPath(level.initialPlayerBoard, level.targetBoard, level.config.moves)
            assertNotNull("Level ${index + 1} should have a valid path", path)
            assertEquals("Level ${index + 1} shortest path mismatch", level.config.moves, path!!.size)
        }
    }
}
