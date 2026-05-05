package edu.cit.baldon.foodiemobile.shared.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val FoodieColorScheme = lightColorScheme(
    primary         = FoodieYellow,
    onPrimary       = FoodieText,
    secondary       = FoodiePurple,
    onSecondary     = FoodieSurface,
    background      = FoodieBackground,
    onBackground    = FoodieText,
    surface         = FoodieSurface,
    onSurface       = FoodieText,
    error           = FoodieRed,
)

@Composable
fun FoodieTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FoodieColorScheme,
        typography  = FoodieTypography,
        content     = content
    )
}
