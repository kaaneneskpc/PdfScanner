package com.kaaneneskpc.documentscanner.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaaneneskpc.documentscanner.data.model.Pdf
import com.kaaneneskpc.documentscanner.presentation.pdf.PdfViewModel
import com.kaaneneskpc.documentscanner.utils.getFileUri

@Composable
fun PdfItem(pdf: Pdf, pdfViewModel: PdfViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    Card(
        modifier = Modifier
            .padding(10.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        onClick = {
            val getFileUri = getFileUri(context, pdf.name.orEmpty())
            val browserIntent = Intent(Intent.ACTION_VIEW, getFileUri)
            browserIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            activity.startActivity(browserIntent)
        }
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(80.dp),
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = "Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pdf.name.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Size: ${pdf.size}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = {
                pdfViewModel.currentPdf = pdf
                pdfViewModel.onShowRenameDialog()
            }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Icon")
            }
        }
    }
}