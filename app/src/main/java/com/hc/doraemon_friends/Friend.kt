package com.hc.doraemon_friends

data class Friend(val name: String, val description: String, val imageResId: Int)

object Friends {
    val jingu = Friend("진구", "무엇 하나 잘하는 것이 없지만 성격은 착하다. 문제가 생기면 도라에몽에게 달려간다.", R.drawable.jingu)
    val doraemon = Friend("도라에몽", "미래에서 온 만능 고양이. 후손을 기쁘게 하기 위해 진구를 물심양면으로 도와준다.", R.drawable.doraemon)
    val eseul = Friend("이슬이", "진구의 소꿉 친구. 착하고 이뻐서 모든 아이들에게 인기가 많다.", R.drawable.eseul)

    val friends = listOf(jingu, doraemon, eseul)
}