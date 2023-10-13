package io.github.chengzis.hilt

import com.squareup.kotlinpoet.ClassName

class BindsMetadata(
    val installIn: ClassName,
    val binds: List<AutoBindsMetadata>,
) {
}