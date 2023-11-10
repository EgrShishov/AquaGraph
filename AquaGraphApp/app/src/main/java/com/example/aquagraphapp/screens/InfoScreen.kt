package com.example.aquagraphapp.screens

import android.text.Html
import android.webkit.WebView
import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.seanproctor.datatable.TableColumnDefinition
import com.seanproctor.datatable.Table
import androidx.compose.material3.Text
import com.example.aquagraphapp.models.QualityModel
import androidx.core.text.HtmlCompat


@Composable
fun InfoScreen(dataForTable: List<QualityModel>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Таблица ПДК",
            fontSize = 30.sp,
            color = Color.Green,
            modifier = Modifier.paddingFromBaseline(100.dp, 0.dp)
        )
        CreateTable(dataForTable)
    }
}

@Composable
fun CreateTable(data: List<QualityModel>) {
    //var selectedRow by remember { mutableStateOf(0) }

    Table(
        modifier = Modifier,
            //.fillMaxWidth()
            //.padding(10.dp, 10.dp)
            //.padding(10.dp),
        columns = listOf(
            TableColumnDefinition {
                Text("Критерий")
            },
//            TableColumnDefinition {
//                Text("Ед.Изм.")
//            },
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
                //cell { Text("${item.name}")}
                cell { Text("${removeHtmlTags(item.metric)} - ${removeHtmlTags(item.name)}")}
                //cell { Text("${removeHtmlTags(item.name)}")}
                //cell { Text("${removeHtmlTags(item.metric)}")}
                cell { Text("${item.pdk}")}
                cell { Text("${item.value}")}
            }
        }
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