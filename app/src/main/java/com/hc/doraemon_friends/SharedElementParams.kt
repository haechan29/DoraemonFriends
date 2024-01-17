package com.hc.doraemon_friends

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

data class SharedElementParams(
    var initialOffset: Offset = Offset.Unspecified,
    var initialSize: IntSize = IntSize.Zero
)