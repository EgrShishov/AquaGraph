package com.example.aquagraphapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.aquagraphapp.models.ListOfMonth
import java.text.DecimalFormat
import java.util.Collections.copy

@Composable
fun ProblemsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ProblemsScreen",
            fontSize = 30.sp,
            color = Color.Blue
        )
        CreateGraphic()
    }
}

@Composable
fun CreateGraphic() {
    var origpointsdata = mutableListOf(
        Point(1f, 0.0234f),
        Point(2f, 0.24f),
        Point(3f, 0.094f),
        Point(4f, 0.044f),
        Point(5f, 0.115f),
        Point(6f, 0.146f),
        Point(7f, 0.097f),
        Point(8f, 0.08f),
        Point(9f, 0.0232f),
        Point(10f, 0.099f),
        Point(11f, 0.111f),
        Point(12f, 0.0913f)
    )

    var steps = 20
    var max_y = max_Y(origpointsdata)
    var min_y = min_Y(origpointsdata)
    var gamma = 1f
    if(min_y < 1)
        gamma = 1 / min_y
    max_y *= gamma

    var pointsdata: MutableList<BarData> = mutableListOf()

    for (i in 0..origpointsdata.size - 1)
        pointsdata.add(BarData(Point(origpointsdata[i].x, origpointsdata[i].y * gamma), MaterialTheme.colorScheme.primary))

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
            if (i != pointsdata.size - 1)
            {
                ListOfMonth[pointsdata[i].point.x.toInt()]
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

fun max_Y(points: List<Point>): Float {
    var max: Float = points[0].y
    for (point in points) {
        if (point.y > max)
            max = point.y
    }
    return max
}

fun min_Y(points: List<Point>): Float {
    var min: Float = points[0].y
    for (point in points) {
        if (point.y < min)
            min = point.y
    }
    return min
}
