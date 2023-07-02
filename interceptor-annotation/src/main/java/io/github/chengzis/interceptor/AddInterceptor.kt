package io.github.chengzis.interceptor

import kotlin.reflect.KClass

/**
 * 在接口上生成拦截器
 *
 * 一共生成4个类文件：
 *
 * * IUserRepositoryArgs.kt: 方法参数包装类，将方法的参数包装成一个类
 * * IUserRepositoryInterceptor.kt： 拦截器接口
 * * IUserRepositoryExt.kt: 添加拦截器的扩展方法
 * * HasInterceptorIUserRepository.kt: 接口实现类，包含拦截器功能。如果需要使用拦截器，则接口实现需要继承此类，而非直接实现接口
 *
 * 示例代码：
 * ```
 * @AddInterceptor
 * interface IUserRepository {
 *     fun findUserById(id: String) : User
 * }
 * ```
 *
 * 生成代码：
 *
 * IUserRepositoryArgs.kt
 * ```
 * public sealed class IUserRepositoryArgs {
 *
 *     public abstract val iUserRepository: IUserRepository
 *
 *     public data class FindUserById(
 *         override val iUserRepository: IUserRepository,
 *         public val id: String,
 *     ) : IUserRepositoryArgs()
 * }
 * ```
 * IUserRepositoryInterceptor.kt
 * ```
 * public interface IUserRepositoryInterceptor<T : IUserRepositoryArgs, R> {
 *
 *     public fun invoke(chain: Chain<T, R>, args: T): R
 *
 *     public class Chain<T : IUserRepositoryArgs, R>(
 *         private val interceptors: List<IUserRepositoryInterceptor<T, R>>
 *     ) {
 *         public fun invoke(args: T): R {
 *             if (interceptors.isEmpty()) {
 *                 throw NullPointerException("最后一个必须返回结果")
 *             }
 *             val interceptor = interceptors.first()
 *             val chain = Chain<T, R>(interceptors.subList(1, interceptors.size))
 *             return interceptor.invoke(chain, args)
 *         }
 *     }
 * }
 * ```
 * IUserRepositoryExt.kt
 * ```
 * public fun IUserRepository.addFindUserByIdInterceptor(interceptor: IUserRepositoryInterceptor<IUserRepositoryArgs.FindUserById, User>) {
 *     if (this is HasInterceptorIUserRepository) {
 *         findUserByIdInterceptors.add(interceptor)
 *     }
 * }
 * ```
 * HasInterceptorIUserRepository.kt
 * ```
 * public abstract class HasInterceptorIUserRepository : IUserRepository {
 *     internal val findUserByIdInterceptors: ArrayList<IUserRepositoryInterceptor<IUserRepositoryArgs.FindUserById, User>> = ArrayList()
 *
 *     final override fun findUserById(id: String): User {
 *         val interceptors = ArrayList(findUserByIdInterceptors)
 *         interceptors.add(object :
 *             IUserRepositoryInterceptor<IUserRepositoryArgs.FindUserById, User> {
 *             override fun invoke(
 *                 chain: IUserRepositoryInterceptor.Chain<IUserRepositoryArgs.FindUserById, User>, args: IUserRepositoryArgs.FindUserById
 *             ): User {
 *                 return onFindUserById(args.id)
 *             }
 *         })
 *         val args = IUserRepositoryArgs.FindUserById(this, id)
 *         return IUserRepositoryInterceptor.Chain(interceptors).invoke(args)
 *     }
 *
 *     protected abstract fun onFindUserById(id: String): User
 * }
 * ```
 *
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class AddInterceptor(
    val baseInterface: KClass<*> = Unit::class
)
