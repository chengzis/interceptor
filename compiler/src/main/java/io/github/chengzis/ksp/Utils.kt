import com.google.devtools.ksp.KSTypeNotPresentException
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.symbol.KSType
import kotlin.reflect.KClass

@OptIn(KspExperimental::class)
fun <T : Annotation> T.getAnnotationClassProperty(get: (T) -> KClass<*>): KSType? {
    return try {
        get(this)
        null
    }catch (e: KSTypeNotPresentException) {
        e.ksType
    }
}


fun String.firstLowercase() : String {
    return replaceFirstChar { it.lowercase() }
}

fun String.firstUppercase() : String {
    return replaceFirstChar { it.uppercase() }
}