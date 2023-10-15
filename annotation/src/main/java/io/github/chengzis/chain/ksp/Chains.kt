package io.github.chengzis.chain.ksp

import kotlin.reflect.KClass


/**
 * 标识这个接口中的方法可以以责任链方式执行
 *
 * 需要遵循接口规范，类名以'I'开始
 *
 * @param extends 继承的接口 必须包含[Chains]注释
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Chains(val extends: KClass<*> = Unit::class)