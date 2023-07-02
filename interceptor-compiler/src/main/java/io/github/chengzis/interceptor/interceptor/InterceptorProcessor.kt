package io.github.chengzis.interceptor.interceptor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.chengzis.interceptor.IAnnotationProcessor
import io.github.chengzis.interceptor.Logger
import com.neusoft.android.hilt.extension.compiler.interceptor.InterceptorMetadata
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import getAnnotationClassProperty
import io.github.chengzis.interceptor.AddInterceptor
import io.github.chengzis.interceptor.Ignore

/**
 * [AddInterceptor]处理器
 */
class InterceptorProcessor : IAnnotationProcessor {

    @OptIn(KspExperimental::class)
    override fun process(environment: SymbolProcessorEnvironment, resolver: Resolver) {
        val logger = Logger(environment.logger, InterceptorProcessor::class, "process")

        val elements =
            resolver.getSymbolsWithAnnotation(AddInterceptor::class.qualifiedName!!)
        for (symbol in elements) {
            if (symbol !is KSClassDeclaration) {
                logger.error("被InterceptorChain修饰的类必须是Class", symbol)
                return
            }
            if (symbol.classKind != ClassKind.INTERFACE) {
                logger.error("被InterceptorChain修饰的类必须是接口", symbol)
                return
            }
        }

        for (element in elements) {
            element as KSClassDeclaration

            val addInterceptor = element.getAnnotationsByType(AddInterceptor::class).first()
            val baseInterface = addInterceptor.getAnnotationClassProperty { it.baseInterface }
            if (baseInterface != null && baseInterface != resolver.builtIns.unitType) {
                baseInterface.declaration as KSClassDeclaration
                if (!baseInterface.declaration.getAnnotationsByType(AddInterceptor::class).any()) {
                    logger.error("被AddInterceptor的baseInterface必须添加AddInterceptor注解", element)
                    return
                }
            }

            val metadata = InterceptorMetadata(
                element.toClassName(),
                baseInterface?.toClassName(),
                element.docString,
                element.getDeclaredFunctions()
                    .filterNot { it.getAnnotationsByType(Ignore::class).any() }
                    .map {
                        InterceptorMetadata.Func(
                            it.simpleName.asString(),
                            it.docString,
                            it.parameters.map { p ->
                                InterceptorMetadata.Parameter(
                                    p.name!!.getShortName(),
                                    p.type.toTypeName(),
                                )
                            },
                            it.returnType?.toTypeName()?: UNIT,
                        )
                    }
            )

            logger.info("clazz = ${metadata.clazz} baseInterface = ${metadata.baseInterface}")
            create(environment, resolver, metadata)
        }
    }

    private fun create(
        environment: SymbolProcessorEnvironment,
        resolver: Resolver,
        metadata: InterceptorMetadata,
    ) {
        ArgsGenerator(metadata).generate(environment)
        InterceptorGenerator(metadata).generate(environment)
        ExtGenerator(metadata).generate(environment)
        HasInterceptorClassGenerator(metadata).generate(environment)
    }

}