import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# I need to add some imports if they are missing
imports = """
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.fadeIn
"""
for imp in imports.strip().split("\n"):
    if imp not in content:
        content = content.replace("import androidx.compose.ui.graphics.Color\n", f"import androidx.compose.ui.graphics.Color\n{imp}\n")

# To inject animated lobby stuff into MainMenuScreen, let's write a python script to replace the box and column
# We will replace the whole MainMenuScreen body up to the showNewGameDialog stuff.

