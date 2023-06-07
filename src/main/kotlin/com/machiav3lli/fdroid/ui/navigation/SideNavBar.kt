/*
 * Neo Store: An open-source modern F-Droid client.
 * Copyright (C) 2022  Antonios Hazim
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.machiav3lli.fdroid.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.machiav3lli.fdroid.entity.appCategoryIcon
import com.machiav3lli.fdroid.ui.compose.utils.vertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideNavBar(
    modifier: Modifier = Modifier,
    keys: List<String>,
    selectedKey: MutableState<String>,
    onClick: (String) -> Unit,
) {
    val items = keys.map { Pair(it, it.appCategoryIcon) }

    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .horizontalScroll(rememberScrollState()),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items) { item ->
            SideNavBarItem(
                modifier = Modifier
                    .vertical()
                    .rotate(-90f)
                    .wrapContentHeight(),
                icon = item.second,
                label = item.first,
                selected = item.first == selectedKey.value,
                onClick = {
                    selectedKey.value = item.first
                    onClick(item.first)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideNavBarItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    selected: Boolean = true,
    colors: SelectableChipColors = FilterChipDefaults.filterChipColors(
        containerColor = Color.Transparent,
        selectedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(48.dp),
        selectedLeadingIconColor = MaterialTheme.colorScheme.primary,
        selectedLabelColor = MaterialTheme.colorScheme.primary,
        iconColor = MaterialTheme.colorScheme.onSurface,
        labelColor = MaterialTheme.colorScheme.onSurface,
    ),
    alwaysShowIcon: Boolean = false,
    onClick: () -> Unit,
) {
    FilterChip(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        border = null,
        colors = colors,
        onClick = onClick,
        selected = selected,
        label = {
            Text(
                text = label,
                maxLines = 1,
            )
        },
        leadingIcon = {
            AnimatedVisibility(visible = selected || alwaysShowIcon) {
                Icon(
                    modifier = Modifier,
                    imageVector = icon,
                    contentDescription = label,
                )
            }
        }
    )
}
