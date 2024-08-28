package com.kaaneneskpc.documentscanner.presentation.pdf

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PdfViewModel @Inject constructor(
) : ViewModel() {

    private val _showRenameDialog = MutableStateFlow(false)
    val showRenameDialog: StateFlow<Boolean> = _showRenameDialog.asStateFlow()

    private val _loadingDialog = MutableStateFlow(false)
    val loadingDialog: StateFlow<Boolean> = _loadingDialog.asStateFlow()

    fun onShowRenameDialog() {
        _showRenameDialog.value = true
    }

    fun onHideRenameDialog() {
        _showRenameDialog.value = false
    }

    fun onHideLoadingDialog() {
        _loadingDialog.value = false
    }
}