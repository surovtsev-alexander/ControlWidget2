package com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase

import com.surovtsev.controlwidget2.features.controlwidget2.domain.model.ControlsInformation

interface ControlsInformationUseCase {
    suspend fun getControlInformation(): ControlsInformation
}
