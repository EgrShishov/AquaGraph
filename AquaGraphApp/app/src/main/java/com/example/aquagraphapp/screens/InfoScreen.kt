package com.example.aquagraphapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
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
import co.yml.charts.ui.barchart.models.SelectionHighlightData


class InfoScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowInfoScreen(data: List<ResponseModel>) {
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
                    ExtendedFloatingActionButton(
                        onClick = {
                            selectedIndex = -1
                            buttonEnabled = false
                        },
                        contentColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        icon = { Icon(Icons.Filled.ArrowBack, "FAB") },
                        text = { Text("Назад") },
                        expanded = true
                    )
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
                        .padding(start = 10.dp, end = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (items.isNotEmpty()) {
                        if (selectedIndex == -1) {
                            Text(
                                text = "Качественный состав воды",
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterHorizontally),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            CreateTable(dataForTable)
                        } else {
                            Text(
                                text = "\n" +
                                        "\n" +
                                        "\nЕдиницы измерения: ${removeHtmlTags(data.last().params[selectedIndex].metric)}",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterHorizontally),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            //com.example.aquagraphapp.screens.BarChartSample(data, selectedIndex)
                            var str: String = ""
                            Box(
                              contentAlignment = Alignment.Center
                            ) {
                                str = CreateGraphic(data = data, criterionIndex = selectedIndex)
                            }
                            Text(
                                text = "* Табличные данные необходимо домножить на: ${
                                    removeHtmlTags(
                                        str
                                    )
                                }",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.CenterHorizontally),
                                fontWeight = FontWeight.Light,
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Text(
                            text = "ОШИБКА СОЕДИНЕНИЯ С СЕРВЕРОМ",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            lineHeight = 44.sp
                        )
                    }
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
            barWidth = 16.dp,
            selectionHighlightData = null
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

fun StringToPDK(pdkStr: String) : Float
{
    var pdkStrCopy = pdkStr.substring(0, pdkStr.indexOf('/'))
    if(pdkStrCopy.indexOf('-') != -1)
        pdkStrCopy = pdkStrCopy.substring(pdkStrCopy.indexOf('-'))
    pdkStrCopy = pdkStrCopy.replace(",", ".")
    return pdkStrCopy.toFloat()
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
        if(value < 1f)//StringToPDK(item.params[criterionIndex].pdk))
        {
            pointsdata.add(0, BarData(Point(falseMonth, value), MaterialTheme.colorScheme.tertiary))
        }
        else
            pointsdata.add(0, BarData(Point(falseMonth, value), MaterialTheme.colorScheme.inversePrimary))
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
    if(data.size >= 4)
        data[5].metric = "оЖ ****"
    //var selectedRow by remember { mutableStateOf(0) }
    Table(
        modifier = Modifier
            .fillMaxWidth(),
        columns = listOf(
            TableColumnDefinition {
                Text("Критерий")
            },
            TableColumnDefinition {
                Text("ПДК")
            },
            TableColumnDefinition(Alignment.CenterEnd) {
                Text(
                    text = "Значение",
                    modifier = Modifier
                        .padding(end = 10.dp))
            },
        )
    ) {
        data.forEachIndexed { index, item ->
            row {
                //onClick = { selectedRow = index }
                cell { Text("${removeHtmlTags(item.name)}") }
                cell { Text("${removeHtmlTags(item.metric).toString() + " - " + item.pdk}") }
                cell {
                    Text(
                        text = "${item.value.toDouble()}",
                        modifier = Modifier
                            .padding(end = 10.dp)
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier.padding(10.dp, 20.dp, 10.dp, 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text =  "*** Показатели физиологической полноценности питьевой воды - показатели общей минерализации, жесткости," +
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
    res = res.replace("<sup>4</sup>", "\u2074")
    res = res.replace("<sup>+4</sup>", "\u207A\u2074")
    res = res.replace("<sup>-4</sup>", "\u207B\u2074")
    res = res.replace("<sup>3</sup>", "\u00B3")
    res = res.replace("<sup>+3</sup>", "\u207A\u00B3")
    res = res.replace("<sup>-3</sup>", "\u207B\u00B3")
    res = res.replace("<sup>2</sup>", "\u00B2")
    res = res.replace("<sup>+2</sup>", "\u207A\u00B2")
    res = res.replace("<sup>-2</sup>", "\u207B\u00B2")
    res = res.replace("10<sup>1</sup>", "10")
    res = res.replace("<sup>1</sup>", "\u00B9")
    res = res.replace("<sup>+1</sup>", "\u207A\u00B9")
    res = res.replace("<sup>-1</sup>", "\u207B\u00B9")
    res = res.replace("10<sup>0</sup>", "1")
    res = res.replace("<sub>4</sub>", "\u2084")
    res = res.replace("<sub>3</sub>", "\u2083")
    res = res.replace("<sub>2</sub>", "\u2082")
    return HtmlCompat.fromHtml(res, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

// нижние 2, 3 - \u2082, \u2083
// верхние 2, 3 - \u00B2, \u00B3
// нижний, верхний минус - \u208B, \u207B
// плюс - \u208A, \u207A
