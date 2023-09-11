package com.example.snakegame

import com.example.snakegame.screen.SettingFragment

const val START_SPEED = 500L
const val MAX_SPEED = 200L

object SnakeCore {

    var nextMove: () -> Unit = {}
    var isPlay = true
    var gameSpeed = SettingFragment().currentSpeed ?: START_SPEED
    var thread: Thread = Thread(Runnable {
        while (true) {
            Thread.sleep(gameSpeed)
            if (isPlay) {
                nextMove()
            }
        }
    })

    init {
        thread.start()
    }

    fun startTheGame() {
        gameSpeed = START_SPEED
        isPlay = true
    }
}