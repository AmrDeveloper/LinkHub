package com.amrdeveloper.linkhub.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.amrdeveloper.linkhub.R

@Composable
fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    selectedIndicatorColor: Color = colorResource(R.color.light_blue_200),
    unSelectedIndicatorColor: Color = colorResource(R.color.light_blue_900),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val isCurrentPage = pagerState.currentPage == iteration
            val indicatorColor = if (isCurrentPage) selectedIndicatorColor
            else unSelectedIndicatorColor

            val size by animateDpAsState(
                targetValue = if (isCurrentPage) 10.dp else 8.dp, label = "Indicator dor color"
            )

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(indicatorColor)
                    .size(size)
            )
        }
    }
}