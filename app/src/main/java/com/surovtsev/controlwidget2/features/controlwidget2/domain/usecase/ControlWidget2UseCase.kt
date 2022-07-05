package com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase

import com.surovtsev.controlwidget2.ControlInformation

interface ControlWidget2UseCase {
    suspend fun getControlInformation(): ControlInformation
}
