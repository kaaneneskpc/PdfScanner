package com.kaaneneskpc.documentscanner.presentation.home

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.documentscanner.data.PdfRepository
import com.kaaneneskpc.documentscanner.data.model.Pdf
import com.kaaneneskpc.documentscanner.utils.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PdfViewModel @Inject constructor(
    application: Application
) : ViewModel() {

    private val pdfRepository = PdfRepository(application)

    private val _pdfState = MutableStateFlow<RequestState<List<Pdf>>>(RequestState.Idle)
    val pdfState: StateFlow<RequestState<List<Pdf>>> = _pdfState.asStateFlow()

    var currentPdf: Pdf? by mutableStateOf(null)

    private val _showRenameDialog = MutableStateFlow(false)
    val showRenameDialog: StateFlow<Boolean> = _showRenameDialog.asStateFlow()

    private val _message: Channel<RequestState<String>> = Channel()
    val message = _message.receiveAsFlow()

    var isDarkMode by mutableStateOf(true)


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _pdfState.emit(RequestState.Loading)
            delay(2000)
            pdfRepository.getAllPdf().catch {
                _pdfState.emit(RequestState.Error(it.message ?: "Something went wrong"))
                it.printStackTrace()
            }.collect {
                _pdfState.emit(RequestState.Success(it))
            }
        }
    }

    fun insertPdf(pdf: Pdf) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _pdfState.emit(RequestState.Loading)
                delay(1000)
                val result = pdfRepository.insertPdf(pdf)
                if (result.toInt() != -1) {
                    _message.send(RequestState.Success("Pdf inserted successfully"))
                } else {
                    _message.send(RequestState.Error("Something went wrong"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _message.send(RequestState.Error(e.message ?: "Something went wrong"))
            }
        }
    }

    fun updatePdf(pdf: Pdf) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _pdfState.emit(RequestState.Loading)
                delay(1000)
                pdfRepository.updatePdf(pdf)
                _message.send(RequestState.Success("Pdf updated successfully"))

            } catch (e: Exception) {
                e.printStackTrace()
                _message.send(RequestState.Error(e.message ?: "Something went wrong"))
            }
        }
    }

    fun deletePdf(pdf: Pdf) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _pdfState.emit(RequestState.Loading)
                delay(1000)
                pdfRepository.deletePdf(pdf)
                _message.send(RequestState.Success("Pdf deleted successfully"))

            } catch (e: Exception) {
                e.printStackTrace()
                _message.send(RequestState.Error(e.message ?: "Something went wrong"))
            }
        }
    }

    fun onShowRenameDialog() {
        _showRenameDialog.value = true
    }

    fun onHideRenameDialog() {
        _showRenameDialog.value = false
    }
}