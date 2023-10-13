package io.github.chengzis.hilt.ksp

import kotlin.reflect.KClass


/**
 * 自动给[targets]生成[Binds代码](https://developer.android.google.cn/training/dependency-injection/hilt-android?hl=zh-cn#inject-interfaces)，省去手动编写的步骤
 *
 * 接口必须以Define开始
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class AutoBinds(
    /**
     * 目标接口，必须是接口类型
     */
    val targets: Array<KClass<*>>,

    /**
     * [@InstallIn](https://developer.android.google.cn/training/dependency-injection/hilt-android?hl=zh-cn#generated-components)
     *
     * 为 Hilt 模块添加注解，以告知 Hilt 每个模块将用在或安装在哪个 Android 类中
     *
     * 多个绑定的[installIn]必须一致
     */
    val installIn: KClass<out Any>,

    /**
     * [限定符](https://developer.android.google.cn/training/dependency-injection/hilt-android?hl=zh-cn#multiple-bindings)
     *
     * 如果您需要让 Hilt 以依赖项的形式提供同一类型的不同实现，必须向 Hilt 提供多个绑定。您可以使用限定符为同一类型定义多个绑定。
     *
     * 限定符是一种注解，当为某个类型定义了多个绑定时，您可以使用它来标识该类型的特定绑定。
     */
    val qualifier: KClass<out Annotation> = Annotation::class,
)