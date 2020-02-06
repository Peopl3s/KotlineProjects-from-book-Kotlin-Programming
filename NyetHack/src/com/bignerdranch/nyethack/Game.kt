package com.bignerdranch.nyethack

import java.lang.Exception
import kotlin.system.exitProcess

fun main()
{
    Game.play()
    /*val name: String = "Madrigal"
    var healthPoints: Int = 89
    val isBlessed: Boolean = true
    val isImmortal:Boolean = false*/

  //  val player = Player("Madrigal")
  //  player.castFireball()

    // Аура
 //  player.auraColor(true)

   // var currentRoom: Room = TownSquare()
 //   println(currentRoom.discription())
  //  println(currentRoom.load())

   /* val race = "gnome"
    val faction = when(race){
        "dwarf" -> "Keepers of the Mines"
        "gnome" -> "Keepers of the Mines"
        "orc" -> "Free People of the Rolling Hills"
        "human" -> "Free People of the Rolling Hills"
        else -> "none"
    }
    val healthStatus = if (healthPoints == 100){
        " is in excelent condition!"
    } else if (healthPoints in 90..99){
        " has a few scraches"
    } else if (healthPoints in 75..89){
        if(isBlessed){
            " has some minor wounds but is healing quite quickly!"
        } else {
            " has some minor wounds"
        }
    } else if (healthPoints in 15..74){
        " looks pretty hurt"
    } else {
        " is in awful condition!"
    }*/
   // val healthStatus = formatHealthStatus(healthPoints, isBlessed)

   // val karma = (Math.pow(Math.random(),(100 - healthPoints) / 100.0) * 20).toInt()

  /*  val extendAura = when (karma){
        in 0..5 -> "RED"
        in 6..10 -> "ORANGE"
        in 11..15 -> "PURPLE"
        in 16..22 -> "GREEN"
        else -> "GREEN"
    }
*/
   // printPlayerStatus(player, player.auraColor(true))
    //com.bignerdranch.nyethack.playerFireballStatus(player.name,5)
}
/*
private fun printPlayerStatus(player: Player, auraColor: String) {
    println("(Aura: ${player.auraColor(true)})" + "(Blessed: ${if (player._isBlessed) "YES" else "NO"})")
    println("${player.name} ${player.formatHealthStatus(1, true)}")
   // println("Extend aura: $extendAura")
}*/

/*private fun auraColor(isBlessed: Boolean, healthPoints: Int, isImmortal: Boolean): String {
    val auraVisible = isBlessed && healthPoints > 50 || isImmortal
    val auraColor = if (auraVisible) "GREEN" else "NONE"
    return auraColor
}*/
/*
private fun castFireball(numFireballs: Int = 2): Int {
    println("A glass of Fireball springs into existence.(x$numFireballs)")
    return (1..50).random()
}
*/
/*private fun playerFireballStatus(playerName:String, numFireballs:Int = 2){
    val lvl = 5//castFireball(numFireballs)
    val statusPlayer = when(lvl){
        in 1..10 -> "Tipsy"
        in 11..20 -> "Sloshed"
        in 21..30 -> "Soused"
        in 31..40 -> "Stewed"
        in 41..50 -> "..t0aSt3d"
        else -> "Spell missed"
    }
    println(statusPlayer)
}*/
/*
private fun formatHealthStatus(healthPoints: Int, isBlessed: Boolean) =
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
*/

// класс-синглтон
object Game{
    private val player = Player("Madrigal")
    private var currentRoom: Room = TownSquare()

    private var worldMap = listOf(
        listOf(currentRoom, Room("Tavern"), Room("Back Room")),
        listOf(Room("Long Corridor"), Room("Generic Room")))

    init{
        println("Welcome, adventurer!")
        player.castFireball()
    }

    fun play(){
        while(true){
            println(currentRoom.discription())
            println(currentRoom.load())
            printPlayerStatus(player)

            println("> Enter your command: ")
            println(GameInput(readLine()).processCommand())
        }
    }

    private fun printPlayerStatus(player: Player) {
        println("(Aura: ${player.auraColor(true)})" + "(Blessed: ${if (player._isBlessed) "YES" else "NO"})")
        println("${player.name} ${player.formatHealthStatus(1, true)}")
    }

    private class GameInput(arg: String?){
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1, { "" })

        fun processCommand() = when(command.toLowerCase()){
            "move" -> move(argument)
            "map" -> map()
            "fight" -> fight()
            "quite" -> quite()
            else -> commandNotFound()
        }

        private fun commandNotFound() = "I'm not quite sure what you're trying to do!"

    }

    private fun map(){
        for(row in worldMap){
            for(place in row){
                if(worldMap[player.currentPosition.y][player.currentPosition.x] == place){
                    print(" X ")
                } else {
                    print(" O ")
                }
            }
            println()
        }
    }

    private fun quite() {
        println("End game")
        exitProcess(0)
    }

    private fun move(directionInput: String) =
        try{
            val direction = Direction.valueOf(directionInput.toUpperCase())
            val newPosition = direction.updateCoordinate(player.currentPosition)
            if(!newPosition.isBounds){
                throw IllegalStateException("$direction is out of bounds.")
            }

            val newRoom = worldMap[newPosition.y][newPosition.x]
            player.currentPosition = newPosition
            currentRoom = newRoom
            "OK, you move $direction to the ${newRoom.name}.\n${newRoom.load()}"
        } catch(e:Exception){
            "Invalid direction: $directionInput."
        }

    private fun fight() = currentRoom.monster?.let{
        while(player._healthPoints > 0 && it._healthPoints > 0){
            slay(it)
            Thread.sleep(1000)
        }
        "Combat complete."
    } ?: "There's nothing here to fight."

    private fun slay(monster: Monster){
        println("${monster.name} did ${monster.attack(player)} damage!")
        println("${player.name} did ${player.attack(monster)} damage!")

        if(player._healthPoints < 0){
            println(">>>> You have been defeated! Thanks for playing. <<<<")
            exitProcess(0)
        }

        if (monster._healthPoints <= 0) {
            println(">>>> ${monster.name} has been defeated! <<<<")
            currentRoom.monster = null
        }
    }
}