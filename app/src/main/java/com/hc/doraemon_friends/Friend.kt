package com.hc.doraemon_friends

data class Friend(val name: String, val description: String, val imageResId: Int)

object friends {
    val jinGu = Friend("진구", "무엇 하나 잘하는 것이 없지만 성격은 착하다. 문제가 생기면 도라에몽에게 달려간다.", R.drawable.jingu)
}