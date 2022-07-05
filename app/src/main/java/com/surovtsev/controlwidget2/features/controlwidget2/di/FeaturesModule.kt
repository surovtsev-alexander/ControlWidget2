package com.surovtsev.controlwidget2.features.controlwidget2.di

import com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase.ControlsInformationUseCase
import com.surovtsev.controlwidget2.features.controlwidget2.domain.usecase.ControlsInformationUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FeaturesModule {

    @Binds
    abstract fun provideControlsInformationUseCase(
        controlsInformationUseCaseImp: ControlsInformationUseCaseImp,
    ): ControlsInformationUseCase

}