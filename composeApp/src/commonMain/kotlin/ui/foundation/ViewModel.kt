package ui.foundation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharingStarted

val ViewModel.started get() = SharingStarted.WhileSubscribed(5000)