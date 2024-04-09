package com.example.apiassistant.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Orange,
    primaryContainer = LightDark,
    secondary = White,
    onPrimary = White,
    tertiary = Red,
    background = NotStrongDark,
    onBackground = CustomGray,
    tertiaryContainer = CustomLightGray,
    onSecondaryContainer = LightGrayDark
)

private val LightColorScheme = lightColorScheme(
    primary = Orange,
    primaryContainer = LightGray,
    secondary = White,
    onPrimary = Dark,
    tertiary = Red,
    background = White,
    onBackground = White,
    tertiaryContainer = White,
    onSecondaryContainer = LightGrayEnd

    //1. **ColorPrimary**: Это основной цвет приложения, используемый для выделения основных элементов интерфейса,
// таких как заголовки, фоновые панели и акцентные элементы.
    //
    //2. **ColorPrimaryVariant**: Вариант основного цвета, который может использоваться для различных стилей и
// оттенков внутри приложения.
    //
    //3. **ColorOnPrimary**: Цвет текста и значков, который наилучшим образом контрастирует с основным цветом
// для обеспечения хорошей читаемости.
    //
    //4. **ColorSecondary**: Дополнительный цвет, который обычно используется для выделения второстепенных
// элементов интерфейса или для создания контрастных элементов.
    //
    //5. **ColorSecondaryVariant**: Вариант дополнительного цвета, который может использоваться для
// разнообразия стилей и оттенков внутри интерфейса.
    //
    //6. **ColorOnSecondary**: Цвет текста и значков, который хорошо контрастирует с
// дополнительным цветом для обеспечения читаемости.
    //
    //7. **ColorBackground**: Цвет фона, используемый для общего фона интерфейса или контейнеров.
    //
    //8. **ColorSurface**: Цвет поверхности, который отличается от фона и обычно используется для разграничения элементов интерфейса.
    //
    //9. **ColorOnBackground**: Цвет текста и значков, которые хорошо видны на фоне для удобства чтения.
    //
    //10. **ColorOnSurface**: Цвет текста и значков, обеспечивающий хорошую читаемость на поверхностях и контрастирующий с ними.

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ApiAssistantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}