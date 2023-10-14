package com.example.generatepdf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.tooling.preview.Preview
import com.example.generatepdf.ui.theme.GeneratePdfTheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeneratePdfTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PDFGenerator()
                }
            }
        }
    }
}

@Composable
fun PDFGenerator() {
    val pdfFile = File(Environment.getExternalStorageDirectory(), "sample.pdf")
    try {
        val document = PDDocument()
        val page = PDPage(PDRectangle.A4)
        document.addPage(page)

        val contentStream = PDPageContentStream(document, page)

        // Compose UI content to a bitmap
        val bitmap = createBitmap(800, 600, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgColor = ComposeColor.White.toArgb()
        canvas.drawColor(bgColor)

        // Your Compose UI content
        DrawPDFContent(canvas)

        // Convert the bitmap to a byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        // Write the byte array to the PDF
        contentStream.drawImage(PDImageXObject.createFromByteArray(document, byteArrayOutputStream.toByteArray(), "sample.png"), 0f, 0f)
        contentStream.close()

        // Save the PDF
        document.save(pdfFile)
        document.close()

    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@Composable
fun DrawPDFContent(canvas: Canvas) {
    // Draw your Compose UI content on the canvas here
    // Example:
    canvas.drawText("Hello, PDF!", 50f, 100f, Paint().apply {
        color = Color.BLACK
        textSize = 24f
        isAntiAlias = true
    })
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeneratePdfTheme {
        Greeting("Android")
    }
}