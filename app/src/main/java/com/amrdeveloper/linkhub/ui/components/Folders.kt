package com.amrdeveloper.linkhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.FolderColor

enum class FolderViewKind {
    List,
    Grid,
}

@Composable
fun FolderItem(
    folder: Folder,
    onClick: (Folder) -> Unit = {},
    onLongClick: (Folder) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(8.dp)
                .combinedClickable(
                    onClick = {
                        onClick(folder)
                    },
                    onLongClick = {
                        onLongClick(folder)
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = folder.folderColor.drawableId),
                contentDescription = "Folder Icon",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                folder.name,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )

            if (folder.isPinned) {
                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_pinned),
                    contentDescription = "Folder is pinned",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun FolderList(
    folders: List<Folder>,
    viewKind: FolderViewKind,
    onClick: (Folder) -> Unit = {},
    onLongClick: (Folder) -> Unit = {},
    folderItemPadding: Dp = 4.dp
) {
    when (viewKind) {
        FolderViewKind.List -> {
            LazyColumn {
                items(folders) { folder ->
                    FolderItem(
                        folder = folder,
                        onClick = onClick,
                        onLongClick = onLongClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(folderItemPadding)
                    )
                }
            }
        }

        FolderViewKind.Grid -> {
            LazyVerticalGrid(columns = GridCells.Fixed(count = 2)) {
                items(folders) { folder ->
                    FolderItem(
                        folder = folder,
                        onClick = onClick,
                        onLongClick = onLongClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(folderItemPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FolderListPreview() {
    val folders = listOf(
        Folder("A_001", folderColor = FolderColor.BLUE),
        Folder("A_002", folderColor = FolderColor.RED),
        Folder("A_003", folderColor = FolderColor.RED),
    )

    FolderList(folders, FolderViewKind.List)
    FolderList(folders, FolderViewKind.Grid)
}