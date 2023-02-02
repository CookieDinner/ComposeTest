package com.cookiedinner.composetest

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.cookiedinner.composetest.ViewModels.MainActivityViewModel
import com.cookiedinner.composetest.ui.theme.ComposeTestTheme
import kotlinx.coroutines.delay

private const val PORTFOLIO_ITEM_ANIMATION_DELAY = 200
private const val PORTFOLIO_ANIMATION_DURATION = 600

class MainActivity: ComponentActivity() {
    private val themeViewModel by viewModels<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme = themeViewModel.darkTheme
            ComposeTestTheme(darkTheme = darkTheme.value) {
                CreateBizCard(viewModel = themeViewModel)
            }
        }
    }
}

@Composable
fun CreateBizCard(isPreview: Boolean = false, viewModel: MainActivityViewModel) {
    var buttonClickedState by remember {
        mutableStateOf(false)
    }

    /**
     * Necessary to render previews in IDE correctly (LaunchedEffect composable doesn't get launched in them)
     */
    if (isPreview)
        buttonClickedState = true

    Surface(modifier = Modifier) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(12.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(corner = CornerSize(15.dp))
            ) {
                Box(modifier = Modifier) {
                    IconButton(
                        modifier = Modifier
                            .size(60.dp)
                            .align(alignment = Alignment.TopEnd),
                        onClick = {
                            viewModel.changeTheme()
                        }
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color.LightGray),
                            elevation = 4.dp,
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_lightbulb_24),
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.scale(0.7f)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CreateImageProfile(
                            modifier = Modifier
                                .size(160.dp)
                                .padding(20.dp)
                        )
                        Divider(
                            modifier = Modifier,
                            color = Color.LightGray,
                            thickness = 3.dp
                        )
                        CreateInfo()
                        Button(
                            modifier = Modifier.padding(bottom = 10.dp),
                            onClick = {
                                buttonClickedState = !buttonClickedState
                            }
                        ) {
                            Text(
                                text = "Fruit projects", style = MaterialTheme.typography.button
                            )
                        }
                        ClickClackContent(buttonClickedState, isPreview)
                    }
                }
            }
        }
    }
}

@Composable
fun ClickClackContent(buttonClickedState: Boolean, isPreview: Boolean = false) {
    val listOfStrings = listOf("Banana smoothies", "Orange cakes", "Apple pies", "Chocolate cherries", "Crunchy kiwis")
    AnimatedVisibility(
        visible = buttonClickedState,
        enter = expandVertically(
            animationSpec = tween(
                durationMillis = PORTFOLIO_ANIMATION_DURATION,
                easing = EaseInOutSine
            ),
            expandFrom = Alignment.CenterVertically
        ),
        exit = shrinkVertically(
            animationSpec = tween(
                durationMillis = PORTFOLIO_ANIMATION_DURATION,
                easing = EaseInOutSine,
                delayMillis = PORTFOLIO_ANIMATION_DURATION
            ),
            shrinkTowards = Alignment.CenterVertically
        )
    ) {
        Portfolio(data = listOfStrings, buttonClickedState, isPreview)
    }
}

@Composable
fun Portfolio(
    data: List<String>,
    buttonClickedState: Boolean,
    isPreview: Boolean = false
) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth()
                .height(350.dp),
            shape = RoundedCornerShape(corner = CornerSize(6.dp)),
            border = BorderStroke(width = 2.dp, color = Color.LightGray)
        ) {
            LazyColumn(modifier = Modifier.padding(5.dp)) {
                itemsIndexed(data) { index, item ->
                    var itemAppeared by remember {
                        mutableStateOf(false)
                    }

                    /**
                     * Necessary to render previews in IDE correctly (LaunchedEffect composable doesn't get launched in them)
                     */
                    if (isPreview)
                        itemAppeared = true

                    LaunchedEffect(buttonClickedState) {
                        itemAppeared = if (buttonClickedState) {
                            delay(PORTFOLIO_ANIMATION_DURATION.toLong())
                            true
                        } else {
                            false
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (index % 2 == 0) Arrangement.Start else Arrangement.End
                    ) {
                        AnimatedVisibility(
                            visible = itemAppeared,
                            enter = expandHorizontally(
                                animationSpec = tween(
                                    durationMillis = PORTFOLIO_ANIMATION_DURATION,
                                    easing = EaseInOutSine,
                                    delayMillis = (index * PORTFOLIO_ITEM_ANIMATION_DELAY)
                                ),
                                expandFrom = if (index % 2 == 0) Alignment.End else Alignment.Start
                            ),
                            exit = shrinkHorizontally(
                                animationSpec = tween(
                                    durationMillis = PORTFOLIO_ANIMATION_DURATION,
                                    easing = EaseInOutSine,
                                ),
                                shrinkTowards = if (index % 2 == 0) Alignment.End else Alignment.Start
                            )
                        ) {
                            FruitProject(index, item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FruitProject(index: Int, item: String) {
    Card(
        modifier = Modifier
            .height(80.dp)
            .width(260.dp)
            .padding(5.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        CompositionLocalProvider(
            LocalLayoutDirection provides if (index % 2 == 0) LayoutDirection.Ltr else LayoutDirection.Rtl
        ) {
            Row(
                modifier = Modifier
                    .height(75.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Box(modifier = Modifier.width(10.dp))
                CreateImageProfile(
                    modifier = Modifier
                        .size(45.dp)
                        .padding(5.dp),
                    image = R.drawable.baseline_free_breakfast_24,
                    borderWidth = 1.dp
                )
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(alignment = Alignment.CenterVertically)
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Because ${item.substringAfter(" ")} matter",
                        style = MaterialTheme.typography.body1,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateInfo() {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = "George J.",
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primaryVariant,
        )
        Text(
            text = "Competent Fruit Seller",
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.subtitle1
        )
        @Suppress("SpellCheckingInspection")
        Text(
            text = "@bananananas578",
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
private fun CreateImageProfile(
    modifier: Modifier = Modifier,
    image: Int = R.drawable.ic_baseline_person_24,
    borderWidth: Dp = 0.5.dp
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        border = BorderStroke(borderWidth, Color.LightGray),
        elevation = 4.dp,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = image),
            contentDescription = "Profile Image",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

private const val previewDevice = "id:pixel_5_xl"

@Preview(
    name = "Light Mode",
    device = previewDevice,
    showSystemUi = true,
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    device = previewDevice,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun Preview() {
    ComposeTestTheme {
        CreateBizCard(isPreview = true, MainActivityViewModel())
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun FruitProjectPreview() {
    ComposeTestTheme {
        FruitProject(index = 0, item = "Banana smoothies")
    }
}
