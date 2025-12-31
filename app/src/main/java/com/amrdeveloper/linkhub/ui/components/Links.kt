package com.amrdeveloper.linkhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Link

@Composable
fun LinkList(
    links: List<Link>,
    onClick: (Link) -> Unit = {},
    onLongClick: (Link) -> Unit = {},
    showClickCount: Boolean = false,
    linkItemPadding: Dp = 4.dp
) {
    LazyColumn {
        items(links) { link ->
            LinkItem(
                link = link,
                onClick = onClick,
                onLongClick = onLongClick,
                showClickCount = showClickCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(linkItemPadding)
            )
        }
    }
}

@Composable
fun LinkItem(
    link: Link,
    onClick: (Link) -> Unit = {},
    onLongClick: (Link) -> Unit = {},
    showClickCount: Boolean = false,
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
                .combinedClickable(onClick = {
                    onClick(link)
                }, onLongClick = {
                    onLongClick(link)
                }), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_link),
                contentDescription = "Link Icon",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    link.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    link.subtitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight().width(50.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End) {
                if (link.isPinned) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_heart),
                        contentDescription = "Link is pinned",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(22.dp).padding(2.dp)
                    )
                }

                if (showClickCount) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "${link.clickedCount}",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_click),
                            contentDescription = "Link count",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp).padding(2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinkListPreview() {
    val links = listOf(
        Link("A_001", "sub_title", url = "", isPinned = true),
        Link("A_002", "sub_title", url = "", isPinned = false),
        Link("A_003", "sub_title", url = "", isPinned = true),
        Link("A_004", "sub_title", url = "", isPinned = false),
    )

    LinkList(links, showClickCount = true)
    LinkList(links, showClickCount = false)
}