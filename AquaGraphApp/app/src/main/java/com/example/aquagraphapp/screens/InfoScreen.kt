package com.example.aquagraphapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.seanproctor.datatable.TableColumnDefinition
import com.seanproctor.datatable.Table
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.aquagraphapp.models.QualityModel
import androidx.core.text.HtmlCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog

@Composable
fun InfoScreen(dataForTable: List<QualityModel>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp, 10.dp, 10.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val items = mutableListOf<String>()
        dataForTable.forEachIndexed { index, item ->
            items.add(index, removeHtmlTags(item.name).toString())
        }
        var selectedIndex by remember { mutableStateOf(-1) }
        if (items.isNotEmpty()) {
            LargeDropdownMenu(
                label = "Выберите критерий",
                items = items,
                selectedIndex = selectedIndex,
                onItemSelected = { index, _ -> selectedIndex = index },
            )
            Text(
                text = "Таблица ПДК",
                fontSize = 30.sp,
                color = Color.Black,
                modifier = Modifier.padding(20.dp, 20.dp, 20.dp, 20.dp),
                fontWeight = FontWeight.Bold
            )
            CreateTable(dataForTable)
        } else {
            Text(
                text = "521 ОШИБКА\n СЕРВЕР НЕ РАБОТАЕТ",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                maxLines = 3
            )
        }
    }
}

@Composable
fun CreateTable(data: List<QualityModel>) {
    var selectedRow by remember { mutableStateOf(0) }
    Table(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 7.dp),
        columns = listOf(
            TableColumnDefinition {
                Text("Критерий")
            },
            TableColumnDefinition {
                Text("ПДК")
            },
            TableColumnDefinition(Alignment.CenterEnd) {
                Text("Значение")
            },
        )
    ) {
        data.forEachIndexed { index, item ->
            row {
                onClick = { selectedRow = index }
                cell { Text("${removeHtmlTags(item.name)}") }
                cell { Text("${item.pdk + ", " + removeHtmlTags(item.metric)}") }
                cell { Text("${item.value}") }
            }
        }
    }
    Box(
        modifier = Modifier
            .padding(10.dp, 10.dp, 0.dp, 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "*** Показатели физиологической полноценности питьевой воды - показатели общей минерализации, жесткости," +
                    " содержания макро- и микроэлементов, обеспечивающие профилактику заболеваний, устраняя дефицит биологически" +
                    " необходимых элементов. В функционирующих централизованных системах хозяйственно-питьевого водоснабжения " +
                    "собственник системы не реже одного раза в год должен обеспечить проведение лабораторных исследований воды " +
                    "по показателям физиологической полноценности перед поступлением питьевой воды в распределительную сеть " +
                    "(Санитарные нормы и правила Требования к физиологической полноценности питьевой воды" +
                    "\n**** В Республике Беларусь жесткость воды измеряют в градусах жесткости (оЖ), за рубежом приняты " +
                    "другие единицы измерения.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic
        )
    }
}

fun removeHtmlTags(htmlString: String): CharSequence {
    var res = htmlString.replace("<sup>4+</sup>", "\u2074\u207A")
    res = res.replace("<sup>4-</sup>", "\u2074\u207B")
    res = res.replace("<sup>3+</sup>", "\u00B3\u207A")
    res = res.replace("<sup>3-</sup>", "\u00B3\u207B")
    res = res.replace("<sup>2+</sup>", "\u00B2\u207A")
    res = res.replace("<sup>2-</sup>", "\u00B2\u207B")
    res = res.replace("<sup>+</sup>", "\u207A")
    res = res.replace("<sup>-</sup>", "\u207A")
    res = res.replace("<sup>4</sup>", "\u2074")
    res = res.replace("<sup>3</sup>", "\u00B3")
    res = res.replace("<sup>2</sup>", "\u00B2")
    res = res.replace("<sub>4</sub>", "\u2084")
    res = res.replace("<sub>3</sub>", "\u2083")
    res = res.replace("<sub>2</sub>", "\u2082")
    return HtmlCompat.fromHtml(res, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

// нижние 2, 3 - \u2082, \u2083
// верхние 2, 3 - \u00B2, \u00B3
// нижний, верхний минус - \u208B, \u207B
// плюс - \u208A, \u207A

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBar(items: List<QualityModel>) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var selectedOptionText by remember {
        mutableStateOf(items[0].name)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = selectedOptionText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Выберите критерий оценки") },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            items.forEach { item ->
                DropdownMenuItem(text = { Text("${removeHtmlTags(item.name)}") },
                    onClick = {
                        selectedOptionText = removeHtmlTags(item.name).toString()
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> LargeDropdownMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    notSetLabel: String? = null,
    items: List<T>,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (T, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        LargeDropdownMenuItem(
            text = item.toString(),
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.height(IntrinsicSize.Min)) {
        OutlinedTextField(
            label = { Text(label) },
            value = items.getOrNull(selectedIndex)?.let { selectedItemToString(it) } ?: "",
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { },
            readOnly = true,
        )

        // Transparent clickable surface on top of OutlinedTextField
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable(enabled = enabled) { expanded = true },
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
            ) {
                val listState = rememberLazyListState()
                if (selectedIndex > -1) {
                    LaunchedEffect("ScrollToSelected") {
                        listState.scrollToItem(index = selectedIndex)
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                    if (notSetLabel != null) {
                        item {
                            LargeDropdownMenuItem(
                                text = notSetLabel,
                                selected = false,
                                enabled = false,
                                onClick = { },
                            )
                        }
                    }
                    itemsIndexed(items) { index, item ->
                        val selectedItem = index == selectedIndex
                        drawItem(
                            item,
                            selectedItem,
                            true
                        ) {
                            onItemSelected(index, item)
                            expanded = false
                        }

                        if (index < items.lastIndex) {
                            Divider(modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LargeDropdownMenuItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface
        selected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(modifier = Modifier
            .clickable(enabled) { onClick() }
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}
