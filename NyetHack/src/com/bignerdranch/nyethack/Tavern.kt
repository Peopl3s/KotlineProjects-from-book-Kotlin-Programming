package com.bignerdranch.nyethack

import  kotlin.math.roundToInt
import java.io.File
import com.bignerdranch.nyethack.extensions.random as randomizer

const val TAVERN_NAME = "Taernyl's Folly"
const val PINT_TO_GALLON = 0.125
const val GOLD_TO_DRAGONCOIN = 1.43

var playerGold = 10
var playerSilver = 10
var playerDragonCoin = 5

val patronList: MutableList<String> = mutableListOf("Eli", "Mordoc", "Sophie")
val lastName = listOf("Ironfoot", "Fernsworth", "Baggins")
val uniquePatrons = mutableSetOf<String>()
val readOnlyPatronList = patronList.toList()
val menuList = File("data/tavern-menu-items.txt").readText().split("\n")
val patronGold = mutableMapOf<String, Double>()


private fun String.toDragonSpeak(): String = this.replace(Regex("[aeiouAEIOU]")){
    when (it.value.toLowerCase()){
        "a" -> "4"
        "e" -> "3"
        "i" -> "1"
        "o" -> "0"
        "u" -> "|_|"
        else -> it.value
    }
}


fun main() {
    /*var bevarage = readLine()?.let{
        if(it.isNotBlank()){
            it.capitalize()
        } else {
            "Buttered Ale"
        }
    }*/
    //bevarage = null
   // var bevarage = readLine()!!.capitalize()
   /* var bevarage = readLine()
    if (bevarage != null){
        bevarage = bevarage.capitalize()
    } else {
        println("I can't do that without crashing - beverage was null!")
    }
    println(bevarage)
    val bevarageServed: String  = bevarage ?: "Buttered Ale"
    println(bevarageServed)*/
    if(patronList.contains("Eli")){
        println("The tavern master says: Eli's in the back playing cards.")
    } else {
        println("The tavern master says: Eli isn't here.")
    }

    if(patronList.containsAll(listOf("Mordoc", "Sophie"))){
        println("The tavern master says: Yea, they're seated by the stew kettle.")
    } else {
        println("The tavern master says: Nay, they departed hours ago.")
    }

   /* println(com.bignerdranch.nyethack.getPatronList[0])
    com.bignerdranch.nyethack.getPatronList.remove("Eli")
    com.bignerdranch.nyethack.getPatronList.add("Alex")
    com.bignerdranch.nyethack.getPatronList.add(0, "Alex")
    com.bignerdranch.nyethack.getPatronList[0] = "Alexis"
    println(com.bignerdranch.nyethack.getPatronList)*/

    /*for (patron in com.bignerdranch.nyethack.getPatronList){
        println("Good evening, $patron")
    }*/
   /* com.bignerdranch.nyethack.getPatronList.forEachIndexed{index, patron -> println("Good evening, $patron - you're #${index + 1} in line.")
        com.bignerdranch.nyethack.placeOrder(patron, com.bignerdranch.nyethack.getMenuList.random())
    }*/

    menuList.forEachIndexed{ index, data ->
        println("$index : $data")
    }

    (0..9).forEach {
        val first = patronList.randomizer()
        val last = lastName.randomizer()
        val name = "$first $last"
        uniquePatrons += name
    }
    println(uniquePatrons)

    uniquePatrons.forEach {
        println("                     $it")
        patronGold[it] = 6.0
    }

    var orderCount = 0
    while(orderCount <= 9){
        placeOrder(
            uniquePatrons.randomizer(),
            menuList.randomizer()
        )
        orderCount++
    }
    println(patronGold)
    displayPatronBalances()
}

private fun displayPatronBalances() {
    patronGold.forEach { patron, balance ->
        println("$patron, balance: ${"%.2f".format(balance)}")
    }
}

fun performNewPurchase(price: Double, patronName: String):Boolean {
    val totalPurse = patronGold.getValue(patronName)
    patronGold[patronName] = totalPurse - price
    return true
}

/*
private fun toDragonSpeak(phrase: String ) = phrase.replace(Regex("[aeiouAEIOU]")){
    when (it.value.toLowerCase()){
        "a" -> "4"
        "e" -> "3"
        "i" -> "1"
        "o" -> "0"
        "u" -> "|_|"
        else -> it.value
    }
}
*/

private fun placeOrder(patronName: String, menuData: String){
    val indexOfApostrophe = TAVERN_NAME.indexOf('\'')
    val tavernMaster = TAVERN_NAME.substring(0 until indexOfApostrophe)
    println("$patronName speaks with $tavernMaster about their order.")

    val (type, name, price) = menuData.split(',')
    val acceptPurchase = performNewPurchase(price.toDouble(), patronName)
    if(acceptPurchase) {
        val message = "$patronName buy a $name ($type) for $price"
        println(message)

        val phrase = if (name == "Dragon's Breath") {
            "$patronName exclaims: ${"Ah, DELICIOS $name!".toDragonSpeak()}"
        } else {
            "$patronName says: Thanks for the $name."
        }
        println(phrase)
    } else {
        println("Not enought money")
    }
    remindPints()
}

fun performPurchase(price: Double): Boolean{
    displayBalance()
    val totalPurse = playerGold + (playerSilver / 100.0)
    println("Total purse in gold: $totalPurse")
    println("Purchasing item for $price")
    val remainingBalance = totalPurse - price

    // DragonCoins
    val remainingBalanceDragonCoin = playerDragonCoin - (price / GOLD_TO_DRAGONCOIN)
    println("Remaining DragonCoinBalance: ${"%.4f".format(remainingBalanceDragonCoin)}")

    if(remainingBalance >= 0) {
        println("Remaining balance: ${"%.2f".format(remainingBalance)}")

        val remainingGold = remainingBalance.toInt()
        val remainingSilver = (remainingBalance % 1 * 100).roundToInt()
        playerGold = remainingGold
        playerSilver = remainingSilver
        displayBalance()
        return true
    }
    return false
}

private fun displayBalance(){
    println("com.bignerdranch.nyethack.Player's purse balance: Gold: $playerGold , Silver: $playerSilver")
}

private fun remindPints(countGallon:Int = 5){
    println("Remaind pints: ${((countGallon / PINT_TO_GALLON) - 12).toInt()}")
}