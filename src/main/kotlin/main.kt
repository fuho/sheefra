import androidx.compose.desktop.Window
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.Exception

fun main() = Window {
    var btnNextTitle by remember { mutableStateOf("Get Next Solution") }
    var message by remember { mutableStateOf("01234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ") }
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
    var node by remember { mutableStateOf(Node(generator.start, generator.startDirection)) }


    MaterialTheme {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column {
                    Button(onClick = {
                        if (generator.solution.hasNext()) {
                            node = generator.solution.next()
                        } else {
                            btnNextTitle = "No more solutions"
                        }
                    }) {
                        Text(btnNextTitle)
                    }
                    Row {
                        OutlinedTextField(
                            value = width.toString(),
                            onValueChange = {
                                width = it.toInt()
                                generator = initializeGenerator()
                            },
                            label = { Text("Width") },
                            modifier = Modifier.width(50.dp)
                        )
                        OutlinedTextField(
                            value = height.toString(),
                            onValueChange = {
                                height = it.toInt()
                                generator = initializeGenerator()
                            },
                            label = { Text("Height") },
                            modifier = Modifier.width(50.dp)
                        )
                        OutlinedTextField(
                            value = length.toString(),
                            onValueChange = {
                                length = it.toInt()
                                generator = initializeGenerator()
                            },
                            label = { Text("Length") },
                            modifier = Modifier.width(50.dp)
                        )

                    }

                }
                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        message = it
                    },
                    label = { Text("Message to hide") }
                )
            }
            PuzzleView(generator.boundary, node, message)
        }
    }
}

@Composable
fun PuzzleView(boundary: Boundary, node: Node, message: String) {
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
                    PuzzleCellView()
                }
            }
        }
    }

}

@Composable
fun PuzzleCellView(
    text: String = (('A'..'Z') + ('a'..'z') + ('0'..'9')).random().toString(),
    direction: Float = listOf(270f, 0f, 90f, 180f).random()
) {
    Box(
        modifier = Modifier.size(24.dp)
    ) {
        Text(text, modifier = Modifier.rotate(direction).align(Alignment.Center), style = TextStyle(fontSize = 24.sp))
    }
}
