package com.bignerdranch.nyethack

import kotlin.random.Random

interface Fightable{
    var _healthPoints: Int
    val diceCount: Int
    val diceSides: Int
    val damageRoll: Int
        get() = (0 until diceCount).map {
            Random(0).nextInt(diceSides + 1)
        }.sum()

    fun attack(opponent: Fightable): Int
}

abstract class Monster(val name: String,
                       val description: String,
                       override var _healthPoints: Int): Fightable{

    override fun attack(opponent: Fightable): Int {
        val damageDealt = damageRoll
        opponent._healthPoints -= damageDealt
        return damageDealt
    }
}

class Goblin(name: String = "Goblin",
             description: String = "A nasty-looking goblin",
             _healthPoints: Int = 30): Monster(name, description, _healthPoints){
    override val diceCount = 2
    override val diceSides = 8

}