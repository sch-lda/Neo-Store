package com.machiav3lli.fdroid.database.entity

import androidx.room.Entity
import com.machiav3lli.fdroid.FIELD_CACHEFILENAME
import com.machiav3lli.fdroid.FIELD_VERSION
import com.machiav3lli.fdroid.ROW_PACKAGE_NAME
import com.machiav3lli.fdroid.TABLE_DOWNLOADED
import com.machiav3lli.fdroid.service.DownloadService

@Entity(
    tableName = TABLE_DOWNLOADED,
    primaryKeys = [ROW_PACKAGE_NAME, FIELD_VERSION, FIELD_CACHEFILENAME]
)
data class Downloaded(
    var packageName: String = "",
    var version: String = "",
    var cacheFileName: String = "",
    var changed: Long = 0L,
    var state: DownloadService.State,
)