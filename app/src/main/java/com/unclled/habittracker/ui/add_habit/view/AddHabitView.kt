package com.unclled.habittracker.ui.add_habit.view

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.unclled.habittracker.navigation.NavRoutes
import com.unclled.habittracker.theme.LocalColors
import com.unclled.habittracker.ui.add_habit.viewmodel.AddHabitVM
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun AddHabitView(
    vm: AddHabitVM = viewModel(),
    navController: NavController,
    onHabitSaved: (Int) -> Unit,
    mode: Int,
    habitId: Long
) {
    val buttonStates = vm.buttonStates
    val selectedItemIndex = vm.selectedItemIndex
    val habitName = vm.habitName
    val habitDescription = vm.habitDescription
    val imageUri = vm.imageUri

    val context = LocalContext.current
    val colors = LocalColors.current
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .background(colors.background)
            .padding(top = 40.dp, bottom = 40.dp)
            .padding(horizontal = 10.dp)
            .fillMaxSize()
    ) {
        NameAndDescriptionFields(
            habitName = habitName,
            habitDescription = habitDescription,
            onHabitNameChange = { vm.habitName = it },
            onHabitDescriptionChange = { vm.habitDescription = it }
        )
        RemainderSettings(
            selectedItemIndex = selectedItemIndex,
            buttonStates = buttonStates,
            onSelectedItemIndexChange = { vm.selectedItemIndex = it },
            onToggleDay = { vm.toggleButtonState(it) },
            onPageSelected = { vm.selectedPeriodValue = it }
        )
        CoverImageSettings(
            imageUri = imageUri,
            onImageUriChanged = { vm.imageUri = it },
            vm = vm
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (habitName.text != "" && habitDescription.text != "") {
                    if (selectedItemIndex != 0 && selectedItemIndex == 1) {
                        var countStates = ""
                        for (i in 0..6)
                            if (buttonStates[i])
                                countStates += i
                        if (countStates != "") {
                            vm.countStates = countStates
                            if (mode == 1) {
                                vm.updateHabitData(habitId, selectedItemIndex)
                            } else {
                                vm.saveToDatabase(selectedItemIndex, countStates)
                            }
                            onHabitSaved(0)
                            navController.navigate(NavRoutes.Habits.route)
                        } else {
                            Toast.makeText(
                                context,
                                "Выберите, по каким дням вы хотите выполнять цель",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        if (mode == 1) {
                            vm.updateHabitData(habitId, selectedItemIndex)
                        } else {
                            vm.saveToDatabase(selectedItemIndex)
                        }
                        onHabitSaved(0)
                        navController.navigate(NavRoutes.Habits.route)
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Заполните поля: название цели и описание цели",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 80.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
        ) {
            Text(
                "Сохранить цель",
                fontSize = 16.sp,
                color = colors.background
            )
        }
    }
}

@Composable
fun CoverImageSettings(imageUri: String?, onImageUriChanged: (String) -> Unit, vm: AddHabitVM) {
    val colors = LocalColors.current
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                onImageUriChanged(it.toString())
            }
        }
    )

    Text(
        "Обложка цели",
        fontSize = 16.sp,
        textAlign = TextAlign.Start,
        color = colors.text,
        modifier = Modifier.padding(start = 6.dp, top = 14.dp, bottom = 8.dp)
    )
    IconButton(
        onClick = {
            galleryLauncher.launch("image/*")
        },
        modifier = Modifier
            .padding(start = 8.dp)
            .size(82.dp)
    ) {
        Box(
            modifier = Modifier.size(82.dp)
        ) {
            if (imageUri == "") {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(colors.primary)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        tint = colors.background,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp)
                    )
                }
            } else {
                val imagePath = vm.saveImageToCache(context, imageUri!!.toUri())
                if (imagePath != null) {
                    vm.imageUri = imagePath
                }
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemainderSettings(
    selectedItemIndex: Int,
    buttonStates: List<Boolean>,
    onSelectedItemIndexChange: (Int) -> Unit,
    onToggleDay: (Int) -> Unit,
    onPageSelected: (Int) -> Unit
) {
    val colors = LocalColors.current

    val daysOfWeek = listOf('П', 'В', 'С', 'Ч', 'П', 'С', 'В')
    val reminderPeriod =
        listOf(
            "Ежедневно",
            "Еженедельно",
            "Раз в несколько недель",
            "Ежемесячно",
            "Раз в несколько месяцев"
        )
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val mutableInteractionSource = remember {
        MutableInteractionSource()
    }
    val pagerState = rememberPagerState(pageCount = {
        11
    })

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = reminderPeriod[selectedItemIndex],
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            textStyle = TextStyle(
                fontSize = 16.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.border,
                unfocusedBorderColor = colors.border,
                focusedTextColor = colors.text,
                unfocusedTextColor = colors.text,
                cursorColor = colors.text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .menuAnchor(MenuAnchorType.PrimaryEditable),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(12.dp),
            containerColor = colors.background,
        ) {
            reminderPeriod.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontSize = 16.sp,
                            fontWeight = if (index == selectedItemIndex) FontWeight.Bold else null,
                            color = if (index == selectedItemIndex) colors.primary else colors.text
                        )
                    },
                    onClick = {
                        onSelectedItemIndexChange(index)
                        expanded = false
                    }
                )
            }
        }
    }
    if (reminderPeriod[selectedItemIndex] == "Еженедельно") {
        Text(
            text = "Напоминать каждый: ",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            color = colors.text,
            modifier = Modifier.padding(start = 6.dp, top = 8.dp, bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (index in 0 until 7) {
                OutlinedButton(
                    onClick = { onToggleDay(index) },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = if (buttonStates[index]) colors.background else colors.primary,
                        containerColor = if (buttonStates[index]) colors.primary else Color.Transparent
                    ),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(44.dp),
                    border = BorderStroke(3.dp, Color(0xffFFE5C1)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = daysOfWeek[index].toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        softWrap = false
                    )
                }
            }
        }
    } else if (reminderPeriod[selectedItemIndex].startsWith("Раз в несколько")) {
        val choose =
            if (reminderPeriod[selectedItemIndex] == "Раз в несколько недель") 0 else 1
        Text(
            text = if (choose == 0) "Напоминать каждую" else "Напоминать каждый",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            color = colors.text,
            modifier = Modifier.padding(start = 6.dp, top = 8.dp, bottom = 8.dp)
        )
        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(0)
            ),
            contentPadding = PaddingValues(horizontal = 180.dp),
            pageSpacing = 32.dp,
        ) { page ->
            val pageNumber: Int = page + 2
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .graphicsLayer {
                        val pageOffset = ((pagerState.currentPage - page) + pagerState
                            .currentPageOffsetFraction).absoluteValue
                        val percentFromCenter = 1.0f - (pageOffset / (5f / 2f))
                        val opacity = 0.25f + (percentFromCenter * 0.75f).coerceIn(0f, 1f)

                        alpha = opacity
                        clip = true
                    }
                    .clickable(
                        interactionSource = mutableInteractionSource,
                        indication = null,
                        enabled = true,
                    ) {
                        scope.launch {
                            pagerState.animateScrollToPage(page)
                            onPageSelected(pageNumber)
                        }
                    }
                ) {
                Text(
                    text = pageNumber.toString(),
                    color = colors.text,
                    modifier = Modifier
                        .size(50.dp)
                        .wrapContentHeight(),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Text(
            text = if (choose == 0) "неделю" else "месяц",
            fontSize = 16.sp,
            textAlign = TextAlign.End,
            color = colors.text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 6.dp)
        )
    }
}

@Composable
fun NameAndDescriptionFields(
    habitName: TextFieldValue,
    habitDescription: TextFieldValue,
    onHabitNameChange: (TextFieldValue) -> Unit,
    onHabitDescriptionChange: (TextFieldValue) -> Unit
) {
    val colors = LocalColors.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        maxLines = 1,
        value = habitName,
        onValueChange = onHabitNameChange,
        label = { Text("Название цели", color = colors.text) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.border,
            unfocusedBorderColor = colors.border,
            focusedTextColor = colors.text,
            unfocusedTextColor = colors.text,
            cursorColor = colors.text
        ),
        shape = RoundedCornerShape(12.dp)
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .height(100.dp),
        maxLines = 3,
        value = habitDescription,
        onValueChange = onHabitDescriptionChange,
        label = { Text("Описание цели", color = colors.text) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.border,
            unfocusedBorderColor = colors.border,
            focusedTextColor = colors.text,
            unfocusedTextColor = colors.text,
            cursorColor = colors.text
        ),
        shape = RoundedCornerShape(12.dp)
    )
}
