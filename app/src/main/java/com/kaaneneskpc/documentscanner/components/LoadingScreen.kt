package com.kaaneneskpc.documentscanner.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaaneneskpc.documentscanner.presentation.pdf.PdfViewModel

@Composable
fun LoadingScreen(pdfViewModel: PdfViewModel = hiltViewModel()) {
    val showLoadingDialog by pdfViewModel.loadingDialog.collectAsStateWithLifecycle()
    if (showLoadingDialog) {
        Dialog(onDismissRequest = { pdfViewModel.onHideLoadingDialog() }) {
            Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

}