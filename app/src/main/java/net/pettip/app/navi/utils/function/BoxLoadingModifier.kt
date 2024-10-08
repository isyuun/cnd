package net.pettip.app.navi.utils.function

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer

/**
 * @Project     : PetTip-Android
 * @FileName    : BoxLoadingModifier
 * @Date        : 2024-05-23
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.function
 * @see net.pettip.app.navi.utils.function.BoxLoadingModifier
 */

/** 특정 영역에 확실히 있는 컴포넌트 ( 프로필, 배너 등 ) 의 로딩 상태용 Modifier */
fun Modifier.shimmerLoadingAnimation(
    isLoadingCompleted: Boolean = true,
    isLightModeActive: Boolean = true,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    if (isLoadingCompleted) {
        return this
    } else {
        return composed {
            val shimmerColors = remember { ShimmerAnimationData(isLightMode = isLightModeActive).getColours() }
            val transition = rememberInfiniteTransition(label = "")
            val currentWidth = rememberUpdatedState(widthOfShadowBrush.toFloat())

            val translateAnimation = transition.animateFloat(
                initialValue = 0f,
                targetValue = (durationMillis + currentWidth.value),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = durationMillis,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Restart,
                ),
                label = "",
            )

            this.graphicsLayer {
                drawBehind {
                    val brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = Offset(x = translateAnimation.value - currentWidth.value, y = 0.0f),
                        end = Offset(x = translateAnimation.value, y = angleOfAxisY)
                    )
                    withTransform({
                        // Apply transformations if needed
                    }) {
                        drawRect(
                            brush = brush,
                            size = size
                        )
                    }
                }
            }
        }
    }
}

data class ShimmerAnimationData(
    private val isLightMode: Boolean
) {
    fun getColours(): List<Color> {
        return if (isLightMode) {
            val color = Color.White
            listOf(
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 1.0f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
            )
        } else {
            val color = Color.Black
            listOf(
                color.copy(alpha = 0.0f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.0f),
            )
        }
    }
}