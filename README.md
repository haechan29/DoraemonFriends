# 🌟 Doraemon Friends
<img src="https://github.com/haechan29/DoraemonFriends/assets/63138511/5f564b3d-057e-4ba5-bd17-713198672e83" style="width:300px"></img>

</br>

# 🛠️ Skills
## Jetpack Compose
- Implemented ``SharedElementTransition`` animation using ``Animatable``
- Please refer to 사이트 for more details

</br>

# 🥄 Challenges
### 1. ``LaunchedEffect``의 key가 바뀌어도 동작하지 않는 문제
__[상황]__ LaunchedEffect에 MutableState<Boolean> 타입 객체를 전달하고, 해당 객체의 value를 바꿨지만 동작하지 않음.</br>
__[원인]__ LaunchedEffect는 전달된 객체의 속성이 바뀌는 것이 아니라, 객체 자체가 바뀔 때 동작함.</br>
__[해결]__ LaunchedEffect에 MutableState의 value를 전달함.</br>

</br>

### 2. 앞 화면의 ``Scroll``이 뒷 화면에도 적용되는 문제
__[상황]__ 앞 화면을 클릭하면 뒷 화면으로 이동하는데, 뒷 화면은 화면 높이보다 작음에도 불구하고 scroll이 가능한 상태가 됨.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;또, Scroll 값 때문에 계산된 Offset 값이 어긋나 레이아웃이 깨짐.</br>

<img src="https://github.com/haechan29/DoraemonFriends/assets/63138511/a97f6fec-d5d5-4348-a383-52594d466ecf" style="width:300px"></img>

__[분석]__ 1. Scroll 값은 Root 뷰의 최상단으로부터 보이는 화면 최상단까지의 거리임.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. LazyColumn에는 ``userScrollEnabled`` 속성이 있지만, Column에는 Scroll을 막는 별다른 방법이 없음.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3. 뒷 화면에 전달된 Scroll 값은 앞 화면의 Scroll 값과 정확히 일치함.</br>
__[원인]__ 앞 화면에만 ScrollState을 적용해야 하는데, 앞 화면과 뒷 화면의 공통 부모에 ScrollState을 적용하고 있었음.</br>
__[해결]__ 부모에 적용되던 ScrollState을 앞 화면에만 적용함.</br>

</br>

### 3. 앞 화면을 클릭했을 때 앞 화면의 이미지를 투명하게 만들어야 하는 문제
__[상황]__ 앞 화면을 클릭했을 때 앞 화면의 이미지가 확대되며 뒷 화면에 삽입되는데,</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;보다 자연스럽게 보이기 위해서는 앞 화면의 이미지가 투명해져야 했음.
__[해결]__ 애니메이션 진행도를 나타내는 변수 ``progress``, 뒷 화면이 보이는 지를 나타내는 변수 ``isShowingDetail``를 이용하여 해결함. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;해결은 되었지만 제대로 한 것인지 잘 모르겠음. 추후 더 좋은 방법을 고민해봐야 할듯함.




