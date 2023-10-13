package io.github.chengzis.ksp

/**
 * 定义一个接口
 * - 不能在代码中使用
 * - 会被被注解处理器处理
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "只能被注解处理器处理，不能在代码中使用",
)
@MustBeDocumented
annotation class Define