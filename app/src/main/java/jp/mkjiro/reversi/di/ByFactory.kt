package jp.mkjiro.reversi.di

import javax.inject.Qualifier


@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ByFactory
