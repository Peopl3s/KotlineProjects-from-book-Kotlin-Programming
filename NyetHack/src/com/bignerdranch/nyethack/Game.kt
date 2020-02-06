package com.bignerdranch.nyethack

import java.lang.Exception
import kotlin.system.exitProcess

fun main()
{
    Game.play()
}
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
