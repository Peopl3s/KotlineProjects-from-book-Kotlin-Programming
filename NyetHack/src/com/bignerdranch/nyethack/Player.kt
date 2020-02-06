package com.bignerdranch.nyethack

import java.io.File
import com.bignerdranch.nyethack.extensions.random

class Player(_name:String,
             override var _healthPoints: Int = 100,
             val _isBlessed:Boolean,
             private val _isImmortal:Boolean): Fightable{

    override fun attack(opponent: Fightable): Int {
        val damageDealt = if (_isBlessed){
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent._healthPoints -= damageDealt
        return damageDealt
    }

    override val diceSides: Int = 6

    override val diceCount: Int = 3

    var name = _name
    get() = "${field.capitalize()} of $hometown"
    private set(value){
        field = value.trim()
    }

    val hometown: String by lazy{selectHometown()}
    var currentPosition = Coordinate(0,0)

    init{
        require(_healthPoints > 0, {"healthPoints must be greater than zero."})
        require(name.isNotBlank(), {"Player must have a name."})
    }
    constructor(name: String):this(name, _healthPoints = 100, _isBlessed = true, _isImmortal = false){
        if(name.toLowerCase() == "kar") _healthPoints = 40
    }

    fun castFireball(numFireballs: Int = 2) {
        println("A glass of Fireball springs into existence.(x$numFireballs)")
    }

     fun auraColor(isBlessed: Boolean): String {
        val auraVisible = isBlessed && _healthPoints > 50 || _isImmortal
        val auraColor = if (auraVisible) "GREEN" else "NONE"
        return auraColor
    }

     fun formatHealthStatus(healthPoints: Int, isBlessed: Boolean) =
        when (healthPoints) {
            100 -> " is in excelent condition!"
            in 90..99 -> " has a few scraches"
            in 75..89 -> if (isBlessed) {
                " has some minor wounds but is healing quite quickly!"
            } else {
                " has some minor wounds"
            }
            in 15..74 -> " looks pretty hurt"
            else -> " is in awful condition!"
        }

    private fun selectHometown() = File("data/towns.txt")
        .readText()
        .split("\n")
        .random()

}

