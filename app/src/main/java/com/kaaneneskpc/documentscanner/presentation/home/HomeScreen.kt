package com.kaaneneskpc.documentscanner.presentation.home

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.kaaneneskpc.documentscanner.components.ErrorScreen
import com.kaaneneskpc.documentscanner.components.LoadingScreen
import com.kaaneneskpc.documentscanner.data.model.Pdf
import com.kaaneneskpc.documentscanner.components.PdfItem
import com.kaaneneskpc.documentscanner.components.RenameDeleteDialog
import com.kaaneneskpc.documentscanner.presentation.pdf.PdfViewModel
import com.kaaneneskpc.documentscanner.utils.showToast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val pdfList = remember { mutableStateListOf<Pdf>() }
    val pdfViewModel = hiltViewModel<PdfViewModel>()
    RenameDeleteDialog(pdfViewModel)
    LoadingScreen(pdfViewModel)
    val scanLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val scanningResult = GmsDocumentScanningResult.fromActivityResultIntent(it.data)
                scanningResult?.pdf?.let { pdf ->
                    Log.d("pdfName", pdf.uri.lastPathSegment.orEmpty())
                    val date = Date()
                    val fileName = SimpleDateFormat(
                        "dd-MMM-yyyy HH:mm:ss",
                        Locale.getDefault()
                    ).format(date) + ".pdf"
                    val pdfItem = Pdf(UUID.randomUUID().toString(), fileName, "10 KB", date)
                    pdfList.add(pdfItem)
                    context.showToast("Success")
                }
            }
        }
    val scanner = remember {
        GmsDocumentScanning.getClient(
            GmsDocumentScannerOptions.Builder()
                .setGalleryImportAllowed(true)
                .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
                .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL).build()
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Document Scanner")
            })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    scanner.getStartScanIntent(activity).addOnSuccessListener {
                        scanLauncher.launch(
                            IntentSenderRequest.Builder(it).build()
                        )
                    }.addOnFailureListener {
                        it.printStackTrace()
                        context.showToast(it.message.toString())
                    }
                },
                text = { Text("Scan Document") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CameraEnhance,
                        contentDescription = "Icon"
                    )
                }
            )
        }
    ) {
        if (pdfList.isEmpty()) {
            ErrorScreen(message = "No PDFs found")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(items = pdfList, key = { pdf ->
                    pdf.id
                }) { index ->
                    PdfItem(pdf = index, pdfViewModel = pdfViewModel)
                }
            }
        }

    }
}