package com.machiav3lli.fdroid.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.machiav3lli.fdroid.database.entity.Downloaded
import com.machiav3lli.fdroid.database.entity.IconDetails
import com.machiav3lli.fdroid.database.entity.Installed
import com.machiav3lli.fdroid.database.entity.Repository
import com.machiav3lli.fdroid.entity.ProductItem
import com.machiav3lli.fdroid.network.CoilDownloader
import com.machiav3lli.fdroid.service.DownloadService
import com.machiav3lli.fdroid.ui.components.appsheet.DownloadProgress

@Composable
fun DownloadsListItem(
    item: ProductItem,
    repo: Repository? = null,
    installed: Installed? = null,
    state: DownloadService.State,
    onUserClick: (ProductItem) -> Unit = {},
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

    ListItem(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { onUserClick(product) }
            .fillMaxWidth(),
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        leadingContent = {
            NetworkImage(
                modifier = Modifier.size(PRODUCT_CARD_ICON),
                data = imageData
            )
        },
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = product.name,
                    modifier = Modifier
                        .weight(1f),
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (installed != null && installed.version != state.version) "${product.installedVersion} → ${state.version}"
                    else state.version,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        },
        supportingContent = {
            DownloadProgress(
                totalSize = if (state is DownloadService.State.Downloading) state.total
                    ?: 1L else 1L,
                isIndeterminate = state !is DownloadService.State.Downloading,
                downloaded = if (state is DownloadService.State.Downloading) state.read else 0L,
            )
        },
    )
}

@Composable
fun DownloadedItem(
    item: Downloaded,
    iconDetails: IconDetails?,
    repo: Repository? = null,
    state: DownloadService.State,
    onUserClick: (Downloaded) -> Unit = {},
) {
    val download by remember(item) { mutableStateOf(item) }
    val imageData by remember(download, repo) {
        mutableStateOf(
            CoilDownloader.createIconUri(
                download.packageName,
                iconDetails?.icon ?: "",
                iconDetails?.metadataIcon ?: "",
                repo?.address,
                repo?.authentication
            ).toString()
        )
    }

    ListItem(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { onUserClick(download) }
            .fillMaxWidth(),
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        leadingContent = {
            NetworkImage(
                modifier = Modifier.size(PRODUCT_CARD_ICON),
                data = imageData
            )
        },
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = download.state.name,
                    modifier = Modifier
                        .weight(1f),
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = state.version,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        },
        supportingContent = {
            DownloadProgress(
                totalSize = when (state) {
                    is DownloadService.State.Downloading -> state.total ?: 1L
                    is DownloadService.State.Cancel      -> 0L
                    is DownloadService.State.Error       -> -1L
                    else                                 -> 1L
                },
                isIndeterminate = state is DownloadService.State.Pending || state is DownloadService.State.Connecting,
                downloaded = when (state) {
                    is DownloadService.State.Downloading -> state.read
                    is DownloadService.State.Success     -> 1L
                    else                                 -> 0L
                },
                finishedTime = download.changed,
            )
        }
    )
}
