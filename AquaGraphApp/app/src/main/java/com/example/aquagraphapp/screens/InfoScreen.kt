package com.example.aquagraphapp.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.seanproctor.datatable.TableColumnDefinition
import com.seanproctor.datatable.Table
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.aquagraphapp.models.QualityModel
import androidx.core.text.HtmlCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(dataForTable: List<QualityModel>) {
    val items = mutableListOf<String>()
    dataForTable.forEachIndexed { index, item ->
        items.add(index, removeHtmlTags(item.name).toString())
    }
    if (items.isEmpty()) {
        items.add(0, "")
    }
    var selectedIndex by remember { mutableStateOf(-1) }
    Scaffold(
        topBar = {
            com.example.aquagraphapp.dropdownMenu.LargeDropdownMenu(
                modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp),
                label = "Выберите критерий",
                items = items,
                selectedIndex = selectedIndex,
                onItemSelected = { index, _ -> selectedIndex = index },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (items.isNotEmpty()) {
                    Text(
                        text = "Качественный состав воды",
                        fontSize = 30.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
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
    }
}

@Composable
fun CreateTable(data: List<QualityModel>) {
    var selectedRow by remember { mutableStateOf(0) }
    Table(
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
                cell { Text("${removeHtmlTags(item.metric).toString() + " - " + item.pdk}") }
                cell { Text("${item.value}") }
            }
        }
    }
    Box(
        modifier = Modifier.padding(10.dp, 20.dp,10.dp, 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "*** Показатели физиологической полноценности питьевой воды - показатели общей минерализации, жесткости," +
                    " содержания макро- и микроэлементов, обеспечивающие профилактику заболеваний, устраняя дефицит биологически" +
                    " необходимых элементов." +
                    "\n**** В Республике Беларусь жесткость воды измеряют в градусах жесткости (оЖ), за рубежом приняты " +
                    "другие единицы измерения.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Justify
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
