package org.example

import java.text.SimpleDateFormat
import java.util.*

class Fecha {
    companion object {
        fun formatoFecha(date: Date): String {
            val formato = "yyyy/MM/dd"
            val simpleDateFormat = SimpleDateFormat(formato)
            val dateFormated = simpleDateFormat.format(date)
            return dateFormated
        }
    }
}