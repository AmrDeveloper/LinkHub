package com.amrdeveloper.linkhub.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R


@Composable
fun FloatingQuickActionsButton(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Create new link action
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically() + scaleIn(),
            exit = fadeOut() + shrinkVertically() + scaleOut(),
            content = {
                SmallFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.ic_link),
                            contentDescription = "Create Link",
                            tint = Color.Unspecified
                        )
                    },
                    onClick = {
                        navController.navigate(R.id.linkFragment)
                        expanded = false
                    })
            })

        // Create new folder action
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically() + scaleIn(),
            exit = fadeOut() + shrinkVertically() + scaleOut(),
            content = {
                SmallFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.ic_directory_blue),
                            contentDescription = "Create Folder",
                            tint = Color.Unspecified
                        )
                    },
                    onClick = {
                        navController.navigate(R.id.folderFragment)
                        expanded = false
                    })
            })

        // Main Floating Action button
        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = colorResource(if (expanded) R.color.light_blue_900 else R.color.sky)
        ) {
            val rotation by animateFloatAsState(if (expanded) 45f else 0f, label = "rotation")
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = "Quick insert action",
                modifier = Modifier.rotate(rotation),
                tint = Color.Unspecified
            )
        }
    }
}