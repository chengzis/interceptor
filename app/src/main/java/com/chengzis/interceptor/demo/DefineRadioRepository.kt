package com.chengzis.interceptor.demo

import io.github.chengzis.chain.ksp.Chains
import io.github.chengzis.ksp.Define
import io.github.chengzis.ksp.Ignore

/**
 * radio仓库
 */
@Chains
interface DefineRadioRepository {

    /**
     * 添加收藏
     * @param frequency 频率
     */
    fun addFavorite(frequency: Int)


    /**
     * 是否收藏
     * @param frequency 频率
     */
    @Ignore
    fun isFavorite(frequency: Int) : Boolean

}

