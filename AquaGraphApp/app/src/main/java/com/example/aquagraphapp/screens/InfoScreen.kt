package com.example.aquagraphapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.example.aquagraphapp.models.ListOfMonth
import com.example.aquagraphapp.models.ResponseModel
import java.text.DecimalFormat
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(data: List<ResponseModel>) {
    val items = mutableListOf<String>()
    var dataForTable = data.last().params
    dataForTable.forEachIndexed { index, item ->
        items.add(index, removeHtmlTags(item.name).toString())
    }
    var selectedIndex by remember { mutableStateOf(-1) }
    var isEnabled by remember { mutableStateOf(false) }
    var buttonEnabled by remember { mutableStateOf(false) }
    if (items.isNotEmpty()) {
        isEnabled = true
    }

    Scaffold(
        topBar = {
            com.example.aquagraphapp.dropdownMenu.LargeDropdownMenu(
                modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp),
                enabled = isEnabled,
                label = "Выберите критерий",
                items = items,
                selectedIndex = selectedIndex,
                onItemSelected = { index, _ ->
                    selectedIndex = index
                    buttonEnabled = index != -1
                },
            )
        },
        floatingActionButton = {
            if (buttonEnabled) {
                FloatingActionButton(
                    onClick = {
                        selectedIndex = -1
                        buttonEnabled = false
                    },
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.ArrowBack, "FAB")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
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
                    if(selectedIndex == -1) {
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
                    }
                    else
                    {
                        Text(
                            text = "Единицы измерения: ${removeHtmlTags(data.last().params[selectedIndex].metric)}",
                            fontSize = 20.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterHorizontally),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        var str = CreateGraphic(data = data, criterionIndex = selectedIndex)
                        Text(
                            text = "${removeHtmlTags(str)}",
                            fontSize = 20.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterHorizontally),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Text(
                        text = "ОШИБКА СОЕДИНЕНИЯ С СЕРВЕРОМ",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 44.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CreateGraphic(data: List<ResponseModel>, criterionIndex: Int) : String {
    var origpointsdata = PartitionByPoint(data, criterionIndex).toMutableList()
    var delta = StringToMonth(data.last().time) - 1
    var steps = 20
    var max_y = max_Y(origpointsdata)
    var min_y = min_Y(origpointsdata)
    var degree = GetDegree(min_y)
    var gamma = 10f / min_y
    Log.d("min", "$min_y")
    min_y = min_y / 10f * Math.pow(10.0, -1.0 * degree).toFloat()
    Log.d("min", "$min_y")
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
            paddingBetweenBars = 7 .dp,
            barWidth = 16.dp
        )
    )

    BarChart(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
        barChartData = barChartData
    )

    return "10<sup>$degree</sup>"
}

fun GetDegree(Num: Float): Int
{
    var num = Num
    var degree = 0
    while(num > 10 || num < 1)
    {
        if(num > 10)
        {
            num /= 10
            degree++
        }
        else
        {
            num *= 10
            degree--
        }
    }
    return degree
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
    for (item in data.reversed())
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
                cell { Text("${removeHtmlTags(item.metric).toString() + " - " + item.pdk}") }
                cell { Text("${item.value}") }
            }
        }
    }
    Box(
        modifier = Modifier.padding(10.dp, 20.dp, 10.dp, 0.dp),
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
    var res = htmlString
    res = res.replace("<sup>4+</sup>", "\u2074\u207A")
    res = res.replace("<sup>4-</sup>", "\u2074\u207B")
    res = res.replace("<sup>3+</sup>", "\u00B3\u207A")
    res = res.replace("<sup>3-</sup>", "\u00B3\u207B")
    res = res.replace("<sup>2+</sup>", "\u00B2\u207A")
    res = res.replace("<sup>2-</sup>", "\u00B2\u207B")
    res = res.replace("<sup>+</sup>", "\u207A")
    res = res.replace("<sup>-</sup>", "\u207A")
    res = res.replace("<sup>2</sup>", "\u00B2")
    res = res.replace("<sup>-2</sup>", "\u207A\u00B2")
    res = res.replace("<sup>1</sup>", "\u00B1")
    res = res.replace("<sup>-1</sup>", "\u207A\u00B1")
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
