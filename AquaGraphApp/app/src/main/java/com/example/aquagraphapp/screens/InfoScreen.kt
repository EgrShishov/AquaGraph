package com.example.aquagraphapp.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.example.aquagraphapp.models.ListOfMonth
import com.example.aquagraphapp.models.ResponseModel
import java.text.DecimalFormat

@Composable
fun InfoScreen(parsedData: List<ResponseModel>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp, 10.dp, 10.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val items = mutableListOf<String>()
        val dataForTable = parsedData.last().params
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
            if(selectedIndex == -1) {
                Text(
                    text = "Таблица ПДК",
                    fontSize = 30.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(20.dp, 20.dp, 20.dp, 20.dp),
                    fontWeight = FontWeight.Bold
                )
                CreateTable(dataForTable)
            }
            else
               CreateGraphic(parsedData, selectedIndex)
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
fun CreateGraphic(data: List<ResponseModel>, criterionIndex: Int) {

    var origpointsdata = PartitionByPoint(data, criterionIndex)
    var delta = StringToMonth(data.last().time) - 1
    var steps = 20
    var max_y = max_Y(origpointsdata)
    var min_y = min_Y(origpointsdata)
    var gamma = 1f
    if(min_y < 1)
        gamma = 1 / min_y
    max_y *= gamma

    var pointsdata: MutableList<BarData> = mutableListOf()

    for (i in 0..origpointsdata.size - 1)
        pointsdata.add(BarData(Point(origpointsdata[i].point.x, origpointsdata[i].point.y * gamma), origpointsdata[i].color))

    pointsdata.add(0, BarData(Point(0f, max_y), Color.White))
    pointsdata.add(
        pointsdata.size,
        BarData(Point(pointsdata.last().point.x + 1f, max_y), Color.White)
    )

    val xAxisData = AxisData.Builder()
        .backgroundColor(MaterialTheme.colorScheme.tertiaryContainer)
        .steps(pointsdata.size)
        .axisLabelAngle(35f)
        .labelData { i ->
            if (i != pointsdata.size - 1 && i != 0)
            {
                ListOfMonth[(pointsdata[i].point.x.toInt() + delta) % 12]
            }
            else
            {
                ""
            }
        }
        .labelAndAxisLinePadding(10.dp)
        .bottomPadding(35.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(MaterialTheme.colorScheme.tertiaryContainer)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i -> DecimalFormat("#0.00").format(i * min_y)}
        .build()

    val barChartData = BarChartData(
        chartData = pointsdata,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        barStyle = BarStyle(
            paddingBetweenBars = 9.68.dp,
            barWidth = 16.dp
        )
    )

    BarChart(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
        barChartData = barChartData
    )
}

fun StringToMonth(time: String) : Int // time: yyyy-mm-dd
{
    var index1: Int = time.indexOf('-', 0)
    var index2: Int = time.indexOf('-', index1 + 1)
    //Log.d("timestring", "${time.substring(index1 + 1, index2)}")
    return time.substring(index1 + 1, index2).toInt()
}

fun StringToValue(valueStr: String) : Float
{
    var valueStrCopy = valueStr.substring(0)
    //Log.d("valuestring", "${valueStrCopy}")
    if(valueStrCopy[0] == '<' || valueStrCopy[0] == '>' || valueStrCopy[0] == '~')
        valueStrCopy = valueStrCopy.drop(1)
    valueStrCopy = valueStrCopy.replace(",", ".")
    //Log.d("value", "${valueStrCopy}")
    //Log.d("value", "${valueStrCopy.toFloat()}")
    return valueStrCopy.toFloat()
}

@Composable
fun PartitionByPoint(data: List<ResponseModel>, criterionIndex: Int): List<BarData>
{
    //var criterionIndex = CriterionIndex--
    var pointsdata: MutableList<BarData> = mutableListOf()
    var falseMonth = 12f
    for (item in data)
    {
        var value = StringToValue(item.params[criterionIndex].value)
        pointsdata.add(0, BarData(Point(falseMonth, value), MaterialTheme.colorScheme.tertiary))
        falseMonth--
    }
    while(pointsdata.size < 12)
    {
        pointsdata.add(0, BarData(Point(falseMonth, pointsdata[0].point.y), Color.White))
        falseMonth--
    }
    //This func always return twelve months
    return pointsdata
}

fun max_Y(points: List<BarData>): Float {
    var max: Float = points[0].point.y
    for (point in points) {
        if (point.point.y > max)
            max = point.point.y
    }
    return max
}

fun min_Y(points: List<BarData>): Float {
    var min: Float = points[0].point.y
    for (point in points) {
        if (point.point.y < min)
            min = point.point.y
    }
    return min
}

@Composable
fun CreateTable(data: List<QualityModel>) {
    //var selectedRow by remember { mutableStateOf(0) }
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
                //onClick = { selectedRow = index }
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
