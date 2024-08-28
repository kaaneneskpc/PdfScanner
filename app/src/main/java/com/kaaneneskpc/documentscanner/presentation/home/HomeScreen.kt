package com.kaaneneskpc.documentscanner.presentation.home

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.kaaneneskpc.documentscanner.components.ErrorScreen
import com.kaaneneskpc.documentscanner.data.model.Pdf
import com.kaaneneskpc.documentscanner.components.PdfItem
import com.kaaneneskpc.documentscanner.components.PdfItemDialog
import com.kaaneneskpc.documentscanner.utils.RequestState
import com.kaaneneskpc.documentscanner.utils.copyPdfFileToAppDirectory
import com.kaaneneskpc.documentscanner.utils.getFileSize
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
    val pdfViewModel = hiltViewModel<PdfViewModel>()
    val pdfState by pdfViewModel.pdfState.collectAsStateWithLifecycle()
    val message = pdfViewModel.message
    PdfItemDialog(pdfViewModel)

    LaunchedEffect(Unit) {
        message.collect {
            when(it) {
                is RequestState.Success -> {
                    context.showToast(it.data)
                }
                is RequestState.Error -> {
                    context.showToast(it.message)
                }
                is RequestState.Loading -> {
                    context.showToast("Loading")
                }
                else -> {
                    context.showToast("Something went wrong")
                }
            }
        }
    }
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

                    copyPdfFileToAppDirectory(context, pdf.uri, fileName)

                    val pdfItem = Pdf(
                        UUID.randomUUID().toString(),
                        fileName,
                        getFileSize(context, fileName),
                        date
                    )
                    pdfViewModel.insertPdf(pdfItem)
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
            }, actions = {
                Switch(checked = pdfViewModel.isDarkMode, onCheckedChange = {
                    pdfViewModel.isDarkMode = it
                })
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
    ) { paddingValues ->

        pdfState.DisplayResult(onLoading = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }, onSuccess = { pdfList ->
            if (pdfList.isEmpty()) {
                ErrorScreen(message = "No PDFs found")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(items = pdfList, key = { pdf ->
                        pdf.id
                    }) { index ->
                        PdfItem(pdf = index, pdfViewModel = pdfViewModel)
                    }
                }
            }
        }, onError = {
            ErrorScreen(message = it)
        })
    }
}