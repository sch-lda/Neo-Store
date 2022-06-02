package com.looker.droidify.ui.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.looker.droidify.database.entity.Installed
import com.looker.droidify.database.entity.Repository
import com.looker.droidify.entity.ActionState
import com.looker.droidify.entity.ProductItem
import com.looker.droidify.network.CoilDownloader
import com.looker.droidify.ui.compose.utils.ExpandableCard
import com.looker.droidify.ui.compose.utils.NetworkImage

@Composable
fun ProductsListItem(
    item: ProductItem,
    repo: Repository? = null,
    onUserClick: (ProductItem) -> Unit = {},
    onFavouriteClick: (ProductItem) -> Unit = {},
    installed: Installed? = null,
    onActionClick: (ProductItem) -> Unit = {}
) {
    val product by remember(item) { mutableStateOf(item) }
    val imageData by remember(product, repo) {
        mutableStateOf(
            CoilDownloader.createIconUri(
                product.packageName,
                product.icon,
                product.metadataIcon,
                repo?.address,
                repo?.authentication
            ).toString()
        )
    }

    ExpandableCard(
        modifier = Modifier.padding(8.dp),
        onClick = { onUserClick(product) },
        expandedContent = {
            ExpandedItemContent(
                item = product,
                installed = installed,
                onFavourite = onFavouriteClick,
                onActionClicked = onActionClick
            )
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NetworkImage(
                modifier = Modifier.size(56.dp),
                data = imageData
            )

            Column(
                modifier = Modifier.wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f),
                ) {
                    Text(
                        text = product.name,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f),
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = product.version,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    text = product.summary,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ExpandedItemContent(
    modifier: Modifier = Modifier,
    item: ProductItem,
    installed: Installed? = null,
    favourite: Boolean = false,
    onFavourite: (ProductItem) -> Unit = {},
    onActionClicked: (ProductItem) -> Unit = {}
) {
    Box(contentAlignment = Alignment.CenterEnd) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onFavourite(item) }) {
                Icon(
                    imageVector = if (favourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Add to Favourite",
                    tint = if (favourite) Color.Red else MaterialTheme.colorScheme.outline
                )
            }
            AnimatedVisibility(visible = installed == null || installed.launcherActivities.isNotEmpty()) {
                FilledTonalButton(
                    colors = buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = { onActionClicked(item) }
                ) {
                    val action = when {
                        installed != null -> ActionState.Launch
                        else -> ActionState.Install
                    }
                    Icon(
                        imageVector = action.icon,
                        contentDescription = stringResource(id = action.textId)
                    )
                    Text(text = stringResource(id = action.textId))
                }
            }
        }
    }
}

//@Preview
@Composable
fun ProductsListItemPreview() {
    ProductsListItem(ProductItem())
}