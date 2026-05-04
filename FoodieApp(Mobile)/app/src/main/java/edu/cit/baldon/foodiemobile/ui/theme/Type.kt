package edu.cit.baldon.foodiemobile.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val FoodieTypography = Typography(
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold,   fontSize = 22.sp),
    titleLarge     = TextStyle(fontWeight = FontWeight.Bold,   fontSize = 20.sp),
    titleMedium    = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    bodyLarge      = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    bodyMedium     = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp),
    bodySmall      = TextStyle(fontWeight = FontWeight.Normal, fontSize = 11.sp),
    labelSmall     = TextStyle(fontWeight = FontWeight.Bold,   fontSize = 11.sp),
)
