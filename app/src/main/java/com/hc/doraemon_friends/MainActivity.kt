package com.hc.doraemon_friends

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hc.doraemon_friends.Friends.friends
import com.hc.doraemon_friends.Friends.jingu
import com.hc.doraemon_friends.ui.theme.DoraemonFriendsTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoraemonFriendsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DoraemonFriends()
                }
            }
        }
    }
}

@Composable
fun DoraemonFriends() {
    val isShowingDetailState = remember { mutableStateOf(false) }
    val detailFriendState = remember { mutableStateOf(jingu) }
    val sharedElementParamsState = remember { mutableStateOf(SharedElementParams()) }
    Box(modifier = Modifier.fillMaxSize()) {
        if (isShowingDetailState.value) {
            FriendDetail(
                detailFriendState.value,
                sharedElementParamsState
            )
        } else {
            FriendMultiLine(
                isShowingDetailState,
                detailFriendState,
                sharedElementParamsState
            )
        }
    }
}

@Composable
fun FriendMultiLine(
    isShowingDetailState: MutableState<Boolean>,
    detailFriendState: MutableState<Friend>,
    sharedElementParamsState: MutableState<SharedElementParams>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        for (i in friends.indices) {
            FriendSingleLine(
                friends[i],
                isShowingDetailState,
                detailFriendState,
                sharedElementParamsState
            )
            if (i != friends.indices.last) {
                Divider()
            }
        }
    }
}

@Composable
fun FriendSingleLine(
    friend: Friend,
    isShowingDetailState: MutableState<Boolean>,
    detailFriendState: MutableState<Friend>,
    sharedElementParams: MutableState<SharedElementParams>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        FriendImage(
            friend,
            isShowingDetailState,
            detailFriendState,
            sharedElementParams
        )
        Spacer(modifier = Modifier.width(10.dp))
        FriendInfo(friend)
    }
}

@Composable
fun FriendImage(
    friend: Friend,
    isShowingDetailState: MutableState<Boolean>,
    detailFriendState: MutableState<Friend>,
    sharedElementParams: MutableState<SharedElementParams>
) {
    var offset = Offset.Unspecified
    var size = IntSize.Zero
    Box(
        modifier = Modifier
            .width(80.dp)
            .aspectRatio(1f)
            .background(color = Color(0xff51a1c4), shape = CircleShape)
            .padding(5.dp)
            .onGloballyPositioned { coordinates ->
                offset = coordinates.positionInRoot()
                size = coordinates.size
            }
            .clickable {
                isShowingDetailState.value = true
                detailFriendState.value = friend
                sharedElementParams.value.initialOffset = offset
                sharedElementParams.value.initialSize = size
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = friend.imageResId),
            contentDescription = null
        )
    }
}

@Composable
fun FriendInfo(friend: Friend) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "이름: ${friend.name}")
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "소개: ${friend.description}")
    }
}

@Composable
fun FriendDetail(friend: Friend, sharedElementParamsState: MutableState<SharedElementParams>) {
    Column(modifier = Modifier.fillMaxSize()) {
        FriendImageDetail(friend, sharedElementParamsState)
        Spacer(modifier = Modifier.height(10.dp))
        FriendInfoDetail(friend)
    }
}

@Composable
fun FriendImageDetail(friend: Friend, sharedElementParamsState: MutableState<SharedElementParams>) {
    val (x, y) = sharedElementParamsState.value.initialOffset
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .width(pxToDp(context, sharedElementParamsState.value.initialSize.width))
            .aspectRatio(1f)
            .offset(pxToDp(context, x.toInt()), pxToDp(context, y.toInt()))
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = friend.imageResId),
            contentDescription = null
        )
    }
}

@Composable
fun FriendInfoDetail(friend: Friend) {
    BodyText(text = "이름: ${friend.name}")
    Spacer(modifier = Modifier.height(10.dp))
    BodyText(text = "소개: ${friend.description}")
}

@Composable
fun BodyText(text: String) {
    Text(
        text = text,
        fontSize = 30.sp,
        lineHeight = 40.sp
    )
}

@Preview(showBackground = true)
@Composable
fun DoraemonFriendsPreview() {
    DoraemonFriendsTheme {
        DoraemonFriends()
    }
}

fun pxToDp(context: Context, px: Int): Dp {
    val density = context.resources.displayMetrics.density
    return (px / density).dp
}

fun dpToPx(context: Context, dp: Dp): Int {
    val density = context.resources.displayMetrics.density
    return (dp.value * density).roundToInt()
}