package com.example.snakegame.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.snakegame.MAX_SPEED
import com.example.snakegame.R
import com.example.snakegame.SnakeCore.gameSpeed
import com.example.snakegame.SnakeCore.isPlay
import com.example.snakegame.SnakeCore.nextMove
import com.example.snakegame.SnakeCore.startTheGame
import com.example.snakegame.databinding.FragmentGameBinding
import com.example.snakegame.model.Coordinate
import com.example.snakegame.model.Direction
import com.example.snakegame.model.SnakeTail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val BOARD_SIZE = 10
const val SNAKE_SIZE = 105

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    private lateinit var scope: CoroutineScope
    private val snakeTail = mutableListOf<SnakeTail>()
    private val snakeHead by lazy {
        ImageView(requireContext()).apply {
            this.setImageResource(R.drawable.snake)
            this.setPadding(10, 10, 10,10)
            this.layoutParams = FrameLayout.LayoutParams(SNAKE_SIZE, SNAKE_SIZE)
        }
    }
    private val food by lazy {
        ImageView(requireContext()).apply {
            this.setImageResource(R.drawable.snake)
            this.setPadding(10, 10, 10,10)
            this.layoutParams = FrameLayout.LayoutParams(SNAKE_SIZE, SNAKE_SIZE)
        }
    }
    private var currentDirection = Direction.RIGHT
    private var currentScore: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGameBinding.inflate(inflater, container, false)


        with(binding) {

            board.layoutParams =
                LinearLayout.LayoutParams(BOARD_SIZE * SNAKE_SIZE, BOARD_SIZE * SNAKE_SIZE)

            startTheGame()
            generateFood()
            nextMove = { move(currentDirection) }
            upButton.setOnClickListener {
                nextMove = {
                    checkIfCurrentDirectionIsNotOpposite(
                        Direction.UP, Direction.DOWN
                    )
                }
            }
            leftButton.setOnClickListener {
                nextMove = {
                    checkIfCurrentDirectionIsNotOpposite(
                        Direction.LEFT, Direction.RIGHT
                    )
                }
            }
            rightButton.setOnClickListener {
                nextMove = {
                    checkIfCurrentDirectionIsNotOpposite(
                        Direction.RIGHT, Direction.LEFT
                    )
                }
            }
            downButton.setOnClickListener {
                nextMove = {
                    checkIfCurrentDirectionIsNotOpposite(
                        Direction.DOWN, Direction.UP
                    )
                }
            }
            pause.setOnClickListener {
                if (!isPlay) return@setOnClickListener
                isPlay = false
                resume.visibility = View.VISIBLE
                newGame.visibility = View.VISIBLE
            }
            playAgain.setOnClickListener {
                activity?.recreate()
            }
            newGame.setOnClickListener {
                activity?.recreate()
            }
            resume.setOnClickListener {
                isPlay = true
                resume.visibility = View.GONE
                newGame.visibility = View.GONE
            }
        }

        return binding.root
    }

   private fun checkIfCurrentDirectionIsNotOpposite(
        nextDirection: Direction,
        oppositeDirection: Direction
    ) {
        if (currentDirection == oppositeDirection) {
            move(currentDirection)
        } else {
            move(nextDirection)
            currentDirection = nextDirection
        }
    }

    private fun checkIfSnakeEatsFood() {
        if (snakeHead.left == food.left && snakeHead.top == food.top) {
            binding.counter.text = "Score: ${currentScore++}"
            generateFood()
            addSnakeTail(snakeHead.top, snakeHead.left)
            increaseSpeed()
        }
    }

    private fun checkIfSnakeCrashed(): Boolean {
        for (tailPart in snakeTail) {
            if (tailPart.coordinate.top == snakeHead.top && tailPart.coordinate.left == snakeHead.left)
                return true
        }
        if (snakeHead.top < 0 || snakeHead.left < 0
            || snakeHead.top >= BOARD_SIZE * SNAKE_SIZE
            || snakeHead.left >= BOARD_SIZE * SNAKE_SIZE
        ) {
            return true
        }
        return false
    }

    private fun addSnakeTail(top: Int, left: Int) {
        val tailPart = drawSnakeTail(top, left)
        snakeTail.add(SnakeTail(Coordinate(top, left), tailPart))
    }

    private fun drawSnakeTail(top: Int, left: Int): ImageView {
        val tailImage = ImageView(requireContext())
        tailImage.setImageResource(R.drawable.snake)
        tailImage.setPadding(10, 10, 10,10)
        tailImage.layoutParams = FrameLayout.LayoutParams(SNAKE_SIZE, SNAKE_SIZE)
        (tailImage.layoutParams as FrameLayout.LayoutParams).topMargin = top
        (tailImage.layoutParams as FrameLayout.LayoutParams).leftMargin = left

        binding.board.addView(tailImage)
        return tailImage
    }

    private fun increaseSpeed() {
        if (gameSpeed <= MAX_SPEED) {
            return
        }
        if (snakeTail.size % 5 == 0) {
            gameSpeed -= 50
        }
    }
    private fun generateFood() {
        val coordinate = generateFoodCoordinate()
        (food.layoutParams as FrameLayout.LayoutParams).topMargin = coordinate.top
        (food.layoutParams as FrameLayout.LayoutParams).leftMargin = coordinate.left
        binding.board.removeView(food)
        binding.board.addView(food)
    }

    private fun generateFoodCoordinate(): Coordinate {
        val coordinate = Coordinate(
            top = (0 until BOARD_SIZE).random() * SNAKE_SIZE,
            left = (0 until BOARD_SIZE).random() * SNAKE_SIZE
        )
        for (tailPart in snakeTail) {
            if (tailPart.coordinate == coordinate) return generateFoodCoordinate()
        }
        return coordinate
    }

    private fun move(direction: Direction) {
        when (direction) {
            Direction.UP -> {
                (snakeHead.layoutParams as FrameLayout.LayoutParams).topMargin -= SNAKE_SIZE
            }

            Direction.DOWN -> {
                (snakeHead.layoutParams as FrameLayout.LayoutParams).topMargin += SNAKE_SIZE
            }

            Direction.RIGHT -> {
                (snakeHead.layoutParams as FrameLayout.LayoutParams).leftMargin += SNAKE_SIZE
            }

            Direction.LEFT -> {
                (snakeHead.layoutParams as FrameLayout.LayoutParams).leftMargin -= SNAKE_SIZE
            }
        }

        scope = lifecycleScope
        scope.launch {
            if (checkIfSnakeCrashed()) {
                isPlay = false
                showScore()
                return@launch
            }
            tailMove()
            checkIfSnakeEatsFood()
            binding.board.removeView(snakeHead)
            binding.board.addView(snakeHead)
        }

    }

    private fun showScore() {
        with(binding) {
            score.text = "Your score is: ${snakeTail.size}"
            score.visibility = View.VISIBLE
            playAgain.visibility = View.VISIBLE
        }
    }

    private fun tailMove() {
        var tempTailPart: SnakeTail? = null
        for (i in 0 until snakeTail.size) {
            val tailPart = snakeTail[i]
            binding.board.removeView(tailPart.image)
            if (i == 0) {
                tempTailPart = tailPart
                snakeTail[i] =
                    SnakeTail(
                        Coordinate(snakeHead.top, snakeHead.left),
                        drawSnakeTail(snakeHead.top, snakeHead.left)
                    )
            } else {
                val nextTailPart = snakeTail[i]
                tempTailPart?.let {
                    snakeTail[i] = SnakeTail(
                        Coordinate(it.coordinate.top, it.coordinate.left),
                        drawSnakeTail(it.coordinate.top, it.coordinate.left)
                    )
                }
                tempTailPart = nextTailPart
            }
        }
    }

}