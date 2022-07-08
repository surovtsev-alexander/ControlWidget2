package com.surovtsev.controlwidget2.features.controlsinformation.domain.usecase

import com.surovtsev.controlwidget2.features.controlsinformation.domain.model.ControlsInformation

interface ControlsInformationUseCase {
    suspend fun getControlInformation(): ControlsInformation
}
