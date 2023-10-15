package com.chengzis.interceptor.demo

import io.github.chengzis.chain.ksp.Chains
import io.github.chengzis.ksp.Define

@Chains(extends = DefineRadioRepository::class)
interface DefineRadioFavoriteRepository : DefineRadioRepository {

    /**
     * 添加收藏
     * @param frequency 频率
     */
    fun addFavorite2(frequency: Int)
}