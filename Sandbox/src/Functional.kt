
private fun <K,V> flipMap(mapToFlip: Map<K,V>): Map<V,K> = mapToFlip.map{it.value to it.key}.toMap()
//private fun <K,V> flipMap(mapToFlip: Map<K,V>): Map<V,K> = mapToFlip.map{pair -> Pair(pair.value, pair.key)}.toMap()

fun main() {
    val gradesByStudent = mapOf("Josh" to 4.0, "Alex" to 2.0, "Jane" to 3.0)

    println(gradesByStudent)
    println(flipMap(gradesByStudent))

    var valuesToAdd = listOf(1, 18, 73, 3, 44, 6, 1, 33, 2, 22, 5, 7)

    valuesToAdd = valuesToAdd.filter { it >= 5 }
    println("Step 1: $valuesToAdd")

    var newList = valuesToAdd.slice(0 until valuesToAdd.size - 1 step 2)
        .zip(valuesToAdd.slice(1 until valuesToAdd.size step 2))
    println("Step 2:${newList}")

    var mulList = newList.map{it.first * it.second}.toList()

    println("Step 3: ${mulList}")

    var aacumList = mulList.fold(0){accum, number -> accum + number}

    println("Step:4 ${aacumList}")
}