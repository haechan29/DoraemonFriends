package com.hc.doraemon_friends

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hc.doraemon_friends.Friends.friends
import com.hc.doraemon_friends.Friends.jingu
import com.hc.doraemon_friends.MainActivity.Companion.TARGET_OFFSET_Y
import com.hc.doraemon_friends.MainActivity.Companion.TARGET_SIZE
import com.hc.doraemon_friends.ui.theme.DoraemonFriendsTheme
import kotlinx.coroutines.launch
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

    companion object {
        const val TARGET_SIZE = 500
        const val TARGET_OFFSET_Y = 100f
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
                sharedElementParamsState,
                isShowingDetailState
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
fun FriendDetail(
    friend: Friend,
    sharedElementParamsState: MutableState<SharedElementParams>,
    isShowingDetailState: MutableState<Boolean>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        WithTransitionProgress(isShowingDetailState) { progress ->
            FriendImageDetail(friend, sharedElementParamsState, progress)
            Spacer(modifier = Modifier.height(10.dp))
            FriendInfoDetail(friend)
        }
    }
}

@Composable
fun FriendImageDetail(
    friend: Friend,
    sharedElementParamsState: MutableState<SharedElementParams>,
    progress: Float
) {
    val context = LocalContext.current
    val initialSize = sharedElementParamsState.value.initialSize
    val targetSize = IntSize(TARGET_SIZE, TARGET_SIZE)
    val sizeProgress = lerp(initialSize, targetSize, progress)
    val initialOffset = sharedElementParamsState.value.initialOffset
    val targetOffset = Offset((getScreenWidth() - targetSize.width) / 2f, TARGET_OFFSET_Y)
    val offsetProgress = lerp(initialOffset, targetOffset, progress)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(pxToDp(context, TARGET_SIZE + TARGET_OFFSET_Y.toInt()))
    ) {
        Box(
            modifier = Modifier
                .width(pxToDp(context, sizeProgress.width))
                .aspectRatio(1f)
                .offset(
                    pxToDp(context, offsetProgress.x.toInt()),
                    pxToDp(context, offsetProgress.y.toInt())
                )
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = friend.imageResId),
                contentDescription = null
            )
        }
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

@Composable
fun getScreenWidth(): Int {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    return dpToPx(context, configuration.screenWidthDp.dp)
}

@Composable
fun WithTransitionProgress(
    isShowingDetailState: MutableState<Boolean>,
    f: @Composable (Float) -> Unit
) {
    val transitionProgress = remember {
        Animatable(if (isShowingDetailState.value) 0f else 1f)
    }
    LaunchedEffect(key1 = isShowingDetailState) {
        launch {
            transitionProgress.animateTo(
                targetValue = if (isShowingDetailState.value) 1f else 0f,
                animationSpec = tween(1000),
            )
        }
    }
    f(transitionProgress.value)
}

fun lerp(initialSize: IntSize, targetSize: IntSize, value: Float): IntSize {
    return IntSize(
        lerp(initialSize.width, targetSize.width, value),
        lerp(initialSize.width, targetSize.width, value)
    )
}

fun lerp(initialValue: Int, targetValue: Int, value: Float): Int {
    return ((initialValue * (1 - value)) + targetValue * value).toInt()
}

fun pxToDp(context: Context, px: Int): Dp {
    val density = context.resources.displayMetrics.density
    return (px / density).dp
}

fun dpToPx(context: Context, dp: Dp): Int {
    val density = context.resources.displayMetrics.density
    return (dp.value * density).roundToInt()
}