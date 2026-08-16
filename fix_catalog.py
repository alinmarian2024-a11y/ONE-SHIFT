import re

with open("app/src/main/java/com/example/CampaignCatalog.kt", "r") as f:
    content = f.read()

# PieceType has CYAN, VIOLET, CORAL, LIME (4 types!)
# We need to include LIME in permutations.
# Wait, 4 types means 4! = 24 permutations.

replacement = """
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
"""

content = re.sub(r"private fun canonicalPair\(.*?return bestCanonical!!\n    \}", replacement.strip(), content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CampaignCatalog.kt", "w") as f:
    f.write(content)

with open("app/src/test/java/com/example/CampaignCatalogTest.kt", "r") as f:
    test_content = f.read()

test_content = re.sub(r"private fun canonicalPair\(.*?return bestCanonical!!\n    \}", replacement.strip(), test_content, flags=re.DOTALL)

with open("app/src/test/java/com/example/CampaignCatalogTest.kt", "w") as f:
    f.write(test_content)

