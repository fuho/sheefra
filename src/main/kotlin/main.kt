import androidx.compose.desktop.Window
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.res.vectorXmlResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import org.jetbrains.skija.Bitmap
import java.lang.Exception
import java.math.BigDecimal

fun main() = Window {
    var message by remember { mutableStateOf("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890") }
    var randomFill by remember { mutableStateOf("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890") }
    var width by remember { mutableStateOf(16) }
    var height by remember { mutableStateOf(16) }
    var length by remember { mutableStateOf(59) }
    fun initializeGenerator(): SelfAvoidingPathGenerator {
        try {
            return SelfAvoidingPathGenerator(
                boundary = Boundary(Position(0, 0), Position(width - 1, height - 1)),
                length = length
            )
        } catch (e: Exception) {
            //noop
            return SelfAvoidingPathGenerator(
                boundary = Boundary(Position(0, 0), Position(1, 1)),
                length = 3
            )

        }
    }

    var generator by remember {
        mutableStateOf(
            initializeGenerator()
        )
    }
    var solutions = remember { mutableStateListOf<Node>() }
    var shownSolution by remember { mutableStateOf(Node(0, 0, CardinalDirection.EAST)) }
    var showDialog: Boolean by remember { mutableStateOf(false) }

    MaterialTheme {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            PuzzleToolbar(
                nextTitle = "Next Solution",
                width = generator.boundary.width,
                height = generator.boundary.height,
                length = generator.length,
                message = message,
                randomFill = randomFill,
                onNextClick = {
                    if (generator.solution.hasNext()) {
                        solutions.add(generator.solution.next())
                        shownSolution = solutions.last()
                    } else {
                        showDialog = true
                    }
                },
                onWidthChanged = {
                    width = it
                    generator = initializeGenerator()
                },
                onHeightChanged = {
                    height = it
                    generator = initializeGenerator()
                },
                onLengthChanged = {
                    length = it
                    generator = initializeGenerator()
                },
                onMessageChanged = { message = it },
                onRandomFillChanged = { randomFill = it }
            )
            Box(
                modifier = Modifier.padding(8.dp)
            ) {
                PuzzleView(generator.boundary, shownSolution, message, randomFill)
            }
        }
        if (showDialog) {
            AlertDialog(onDismissRequest = {}, buttons = {})
        }
    }
}


@Composable
fun PuzzleToolbar(
    nextTitle: String,
    width: Int,
    height: Int,
    length: Int,
    message: String,
    randomFill: String,
    onNextClick: () -> Unit,
    onWidthChanged: (Int) -> Unit,
    onHeightChanged: (Int) -> Unit,
    onLengthChanged: (Int) -> Unit,
    onMessageChanged: (String) -> Unit,
    onRandomFillChanged: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IntegerField(
                value = width,
                min = 3,
                onValueChange = { onWidthChanged(it) },
                label = "Width",
            )
            IntegerField(
                value = height,
                min = 3,
                onValueChange = { onHeightChanged(it) },
                label = "Height",
            )
            IntegerField(
                value = length,
                onValueChange = { onLengthChanged(it) },
                label = "Length",
            )
            Button(onClick = onNextClick) {
                Text(nextTitle)
            }
        }
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChanged,
            label = { Text("Message to write along the path") }
        )
        OutlinedTextField(
            value = randomFill,
            onValueChange = onRandomFillChanged,
            label = { Text("Filler to randomly pick from") }
        )
    }
}

@Composable
fun PuzzleView(boundary: Boundary, node: Node, message: String, randomFill: String) {
    Column {
        for (y in boundary.a.y..boundary.b.y) Row {
            for (x in boundary.a.x..boundary.b.x) {
                node.path.firstOrNull { it.position == Position(x, y) }?.let {
                    PuzzleCellView(
                        if (message.isEmpty()) "☠️" else message[(message.length + it.length - 1) % message.length].toString(),
                        when (it.direction) {
                            CardinalDirection.NORTH -> 270f
                            CardinalDirection.EAST -> 0f
                            CardinalDirection.SOUTH -> 90f
                            CardinalDirection.WEST -> 180f
                        }
                    )
                } ?: run {
                    PuzzleCellView(
                        text = if (randomFill.isEmpty()) "" else randomFill.random().toString(),
                        direction = listOf(270f, 0f, 90f, 180f).random()
                    )
                }
            }
        }
    }

}

@Composable
fun PuzzleCellView(
    text: String = "☠️",
    direction: Float = 45f
) {
    Box(
        modifier = Modifier.size(24.dp)
    ) {
        Text(text, modifier = Modifier.rotate(direction).align(Alignment.Center), style = TextStyle(fontSize = 24.sp))
    }
}

@Composable
fun IntegerField(
    label: String = "",
    value: Int = 0,
    step: Int = 1,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    onValueChange: (Int) -> Unit = {},
) {
    var _value by remember { mutableStateOf(value) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$label: $_value")
        Column {
            IconButton(
                onClick = {
                    if (_value <= max - step) {
                        _value += step
                        onValueChange(_value)
                    }
                },
                modifier = Modifier.height(24.dp),
            ) {
                Icon(Icons.Rounded.KeyboardArrowUp)
            }
            IconButton(
                onClick = {
                    if (_value >= min + step) {
                        _value -= step
                        onValueChange(_value)
                    }
                },
                modifier = Modifier.height(24.dp),
            ) {
                Icon(Icons.Rounded.KeyboardArrowDown)
            }
        }
    }
}
