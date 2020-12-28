package org.fuho.sheefra

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Menu
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.MenuItem

fun main() =
    Window(
        title = "Sheera",
        size = IntSize(1024, 768),
        menuBar = MenuBar(
            Menu(
                name = "Menu 1",
                MenuItem("MenuItem 1-1"),
                MenuItem("MenuItem 1-2"),
                MenuItem("MenuItem 1-3"),
            ),
            Menu(
                name = "Menu 2",
                MenuItem("MenuItem 2-1"),
                MenuItem("MenuItem 2-2"),
                MenuItem("MenuItem 2-3"),
                MenuItem("MenuItem 2-4"),
            ),
            Menu(name = "Menu 3"),
            Menu(name = "Menu 4"),
        )
    ) {
        Sheefra()
    }
