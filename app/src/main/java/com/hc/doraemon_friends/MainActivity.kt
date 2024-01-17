package com.hc.doraemon_friends

import android.content.Context
import android.os.Bundle
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hc.doraemon_friends.Friends.friends
import com.hc.doraemon_friends.Friends.jingu
import com.hc.doraemon_friends.ui.theme.DoraemonFriendsTheme

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
    if (isShowingDetailState.value) {
        FriendDetail(detailFriendState.value)
    } else {
        FriendMultiLine(isShowingDetailState, detailFriendState)
    }
}

@Composable
fun FriendMultiLine(
    isShowingDetailState: MutableState<Boolean>,
    detailFriendState: MutableState<Friend>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        for (i in friends.indices) {
            FriendSingleLine(
                friends[i],
                isShowingDetailState,
                detailFriendState)
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
    detailFriendState: MutableState<Friend>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                isShowingDetailState.value = true
                detailFriendState.value = friend
            }
    ) {
        FriendImage(friend)
        Spacer(modifier = Modifier.width(10.dp))
        FriendInfo(friend)
    }
}

@Composable
fun FriendImage(friend: Friend) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .aspectRatio(1f)
            .background(color = Color(0xff51a1c4), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.padding(5.dp),
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
fun FriendDetail(friend: Friend) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        FriendImageDetail(friend)
        Spacer(modifier = Modifier.height(10.dp))
        FriendInfoDetail(friend)
    }
}

@Composable
fun FriendImageDetail(friend: Friend) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
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