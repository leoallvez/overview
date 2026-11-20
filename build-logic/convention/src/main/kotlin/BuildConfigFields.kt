import com.android.build.api.dsl.VariantDimension

/** Declares a `String` constant in the generated `BuildConfig`. */
fun VariantDimension.stringField(name: String, value: String) {
    buildConfigField("String", name, "\"$value\"")
}

/** Declares an `int` constant in the generated `BuildConfig`. */
fun VariantDimension.intField(name: String, value: Int) {
    buildConfigField("int", name, value.toString())
}

/** Declares a `long` constant in the generated `BuildConfig`. */
fun VariantDimension.longField(name: String, value: Long) {
    buildConfigField("long", name, "${value}L")
}
