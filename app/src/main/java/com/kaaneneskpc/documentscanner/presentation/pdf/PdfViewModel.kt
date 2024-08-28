package com.kaaneneskpc.documentscanner.presentation.pdf

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.documentscanner.data.PdfRepository
import com.kaaneneskpc.documentscanner.data.model.Pdf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PdfViewModel @Inject constructor(
    application: Application
) : ViewModel() {

    private val pdfRepository = PdfRepository(application)

    private val _pdfState = MutableStateFlow<List<Pdf>>(arrayListOf())
    val pdfState: StateFlow<List<Pdf>> = _pdfState.asStateFlow()

    var currentPdf: Pdf? by mutableStateOf(null)

    private val _showRenameDialog = MutableStateFlow(false)
    val showRenameDialog: StateFlow<Boolean> = _showRenameDialog.asStateFlow()

    private val _loadingDialog = MutableStateFlow(false)
    val loadingDialog: StateFlow<Boolean> = _loadingDialog.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            pdfRepository.getAllPdf().catch {
                it.printStackTrace()
            }.collect {
                _pdfState.emit(it)
            }
        }
    }

    fun insertPdf(pdf: Pdf) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = pdfRepository.insertPdf(pdf)
        }
    }

    fun updatePdf(pdf: Pdf) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = pdfRepository.updatePdf(pdf)
        }
    }

    fun deletePdf(pdf: Pdf) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = pdfRepository.deletePdf(pdf)
        }
    }

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