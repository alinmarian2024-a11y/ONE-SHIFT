import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Replace generatePuzzle
def repl_generate(m):
    return """fun generatePuzzle(level: Int, seed: Long? = null): PuzzleData {
    if (seed == null) {
        return CampaignCatalog.levels[(level - 1).coerceIn(0, 105)]
    }
    val random = kotlin.random.Random(seed)
    val config = getLevelConfig(level, random)
    return CampaignCatalog.generatePuzzleInternal(config, random)
}"""

content = re.sub(
    r'fun generatePuzzle\(level: Int, seed: Long\? = null\): PuzzleData \{.*?return PuzzleData\(targetBoard, playerBoard, solutionMoves, config\)\n\}',
    repl_generate,
    content,
    flags=re.DOTALL
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

