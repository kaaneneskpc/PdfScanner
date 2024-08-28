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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kaaneneskpc.documentscanner.presentation.pdf.PdfViewModel

@Composable
fun RenameDeleteDialog(pdfViewModel: PdfViewModel = hiltViewModel()) {
    var newNameText by remember { mutableStateOf("") }
    val showRenameDialog by pdfViewModel.showRenameDialog.collectAsStateWithLifecycle()
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
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Update")
                        }
                    }

                }
            }
        }
    }
}