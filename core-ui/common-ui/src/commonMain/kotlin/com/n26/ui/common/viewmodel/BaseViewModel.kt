package com.n26.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n26.ui.common.model.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T> : ViewModel() {
	private val _state: MutableStateFlow<Resource<T>> = MutableStateFlow(Resource.Loading)
	val state: StateFlow<Resource<T>> = _state

	protected fun refreshData(
		action: suspend () -> Result<T>,
	) {
		viewModelScope.launch {
			_state.value = Resource.Loading

			action().fold(onSuccess = {
				_state.value = Resource.Success(it)
			}, onFailure = {
				_state.value = Resource.Error(it)
			})
		}
	}
}
