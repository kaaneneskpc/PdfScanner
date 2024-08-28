package com.kaaneneskpc.documentscanner.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaaneneskpc.documentscanner.presentation.pdf.PdfViewModel
import com.kaaneneskpc.documentscanner.utils.deleteFile
import com.kaaneneskpc.documentscanner.utils.renameFile
import java.util.Date

@Composable
fun RenameDeleteDialog(pdfViewModel: PdfViewModel = hiltViewModel()) {

    var newNameText by remember(pdfViewModel.currentPdf) { mutableStateOf(pdfViewModel.currentPdf?.name.orEmpty()) }
    val showRenameDialog by pdfViewModel.showRenameDialog.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (showRenameDialog) {
        Dialog(onDismissRequest = { pdfViewModel.onHideRenameDialog() }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Rename Pdf", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newNameText,
                        onValueChange = { newText -> newNameText = newText },
                        label = { Text("Pdf Name") })
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        IconButton(onClick = {
                            pdfViewModel.currentPdf?.let {
                                pdfViewModel.onHideRenameDialog()
                                if (deleteFile(context, it.name.orEmpty())) {
                                    pdfViewModel.deletePdf(it)
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { pdfViewModel.onHideRenameDialog() }) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            pdfViewModel.currentPdf?.let {
                                if (!it.name.equals(newNameText, true)) {
                                    pdfViewModel.onHideRenameDialog()
                                    renameFile(context, it.name.orEmpty(), newNameText)
                                    val updateFile =
                                        it.copy(name = newNameText, lastModifiedTime = Date())
                                    pdfViewModel.updatePdf(updateFile)
                                } else {
                                    pdfViewModel.onHideRenameDialog()
                                }
                            }
                        }) {
                            Text(text = "Update")
                        }
                    }

                }
            }
        }
    }
}