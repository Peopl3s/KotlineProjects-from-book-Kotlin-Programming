fun String.addEnthusiasm(amount: Int = 1) = this + "!".repeat(amount)

val String.numVowels
get() = count{"auieoy".contains(it)}

fun <T> T.easyPrint():T{
    println(this)
    return this
}

infix fun String?.printWithDefault(default: String) = print(this ?: default)

fun main() {
   println("Madrigal has left the building".addEnthusiasm(5).numVowels)
    42.easyPrint()
    val nullableString: String? = null
    nullableString printWithDefault "Default string"
}