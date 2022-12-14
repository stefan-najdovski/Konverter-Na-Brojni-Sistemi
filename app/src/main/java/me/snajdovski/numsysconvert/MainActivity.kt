package me.snajdovski.numsysconvert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.snajdovski.numsysconvert.ui.theme.КонвертерTheme


class MainActivity : ComponentActivity() {

    companion object {
        init {
            System.loadLibrary("bigint")
        }
    }

    private external fun convertNumberMrCpp(number: String, startbase:String, endbase:String): String
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            КонвертерTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    var resultDiplay by remember {
                        mutableStateOf("")
                    }

                    var converterTypeText by remember {
                        mutableStateOf("")
                    }

                    val scrollState = rememberScrollState()


                    var expanded by remember {
                        mutableStateOf(listOf(false, false, false))
                    }


                    var resultantStringVisibility by remember {
                        mutableStateOf(false)
                    }


                    var fromText by remember {
                        mutableStateOf("")
                    }
                    var toText by remember {
                        mutableStateOf("")
                    }
                    var amount by remember {
                        mutableStateOf("")
                    }

                    val softKeyboardFocusManager = LocalFocusManager.current

                    var firstDropDownVal: String?
                    var secDropDownVal: String?
                    var result = ""

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            modifier = Modifier
                                .align(Alignment.Start),
                            text = "Конвертер на\nБројни Системи",
                            fontSize = 30.sp,
                            style = TextStyle(fontWeight = FontWeight.Black)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        DropDownBox(
                            dropDownList = listOf("Декаден", "Бинарен", "Хексален", "Октален"),
                            textValue = converterTypeText,
                            labelText = "Одбери броен систем",
                            scrollState = scrollState,
                            isExpanded = expanded[0],
                            expandStatus = {
                                expanded = listOf(it, false, false)
                            },
                            valueChange = {
                                resultantStringVisibility = false
                                converterTypeText = it
                                fromText = ""
                                toText = ""
                                amount = ""
                            }
                        )
                        firstDropDownVal = converterTypeText
                        // println(fistDropDownVal)
                        Spacer(modifier = Modifier.height(15.dp))

                        DropDownBox(
                            dropDownList = listOf("Декаден", "Бинарен", "Хексален", "Октален"),
                            textValue = fromText,
                            labelText = "Одбери броен систем во што сакаш да конвертираш",
                            scrollState = scrollState,
                            isExpanded = expanded[1],
                            expandStatus = {
                                if (converterTypeText.isNotEmpty()) {
                                    expanded = listOf(false, it, false)
                                }
                            },
                            valueChange = {
                                fromText = it
                            }
                        )
                        secDropDownVal = fromText
                        Spacer(modifier = Modifier.height(30.dp))

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = amount,
                            onValueChange = {
                                amount = it
                            },
                            label = {
                                Text(text = "Внесете ја вредноста", color = Color.Gray)
                            },
                            placeholder = {
                                Text(text = "0")
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done

                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    softKeyboardFocusManager.clearFocus()
                                }
                            ),
                            // enabled = toText.isNotBlank()
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        //TO:DO Optimize this Monster
                        Button(onClick = {
                            result = (
                                    convertNumberMrCpp(amount,
                                        when(firstDropDownVal){
                                        "Декаден" -> 10.toString()
                                        "Бинарен" -> 2.toString()
                                        "Октален" -> 8.toString()
                                        "Хексален" -> 16.toString()
                                        else -> {""}
                                    },
                                        when(secDropDownVal){
                                        "Декаден" -> 10.toString()
                                        "Бинарен" -> 2.toString()
                                        "Октален" -> 8.toString()
                                        "Хексален" -> 16.toString()
                                        else -> {""}
                                    }
                                    ))
                            resultDiplay = result
                        })
                        {
                            Text(text = "Конвертирај", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        SelectionContainer {
                            Text(

                                text = resultDiplay,
                            )
                        }

                        Spacer(modifier = Modifier.height(25.dp))
                    }
                }
            }
        }
    }

}