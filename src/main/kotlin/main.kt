import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp

fun main() = Window {
    var text by remember { mutableStateOf("First Button") }
    var text2 by remember { mutableStateOf("Second Button") }

    MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                text = "Hello, first Button"
            }) {
                Text(text)
            }
            Button(onClick = {
                text = "Hello, second button"
            }) {
                Text(text)
            }

        }
    }
}