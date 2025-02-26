package mx.itson.cheems2

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var gameOverCard = 0
    var flippedCards = 0
    var flippedSet = mutableSetOf<Int>()
    var gameWon = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        start()
        val btn_restart = findViewById<Button>(R.id.button_restart)
        btn_restart.setOnClickListener{
            restartGame()
        }
        val btn_flip = findViewById<Button>(R.id.button_flipCards)
        btn_flip.setOnClickListener{
            flipped()
        }

    }

    fun start(){
        for(i in 1 ..12 ){
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton
            btnCard.setOnClickListener(this)
            btnCard.setBackgroundResource(R.drawable.icon_pregunta)

        }
        gameOverCard = (1 ..12).random()
        Toast.makeText(this, getString(R.string.text_welcome), Toast.LENGTH_SHORT).show()

        Log.d("El valor de la carta es: ","La carta perdedora es: ${gameOverCard.toString()}")
    }
    fun flip(card: Int) {
        // Si el juego ya se ganó, no hacer nada si se toca la carta perdedora
        if (card == gameOverCard) {
            if (gameWon) {
                // Si ya ganaste y tocas la carta perdedora, solo la volteas, sin perder
                val btnCard = findViewById<View>(
                    resources.getIdentifier("card$card", "id", this.packageName)
                ) as ImageButton
                btnCard.setBackgroundResource(R.drawable.icon_chempe)
                return
            }
        }

        if (card == gameOverCard) {
            // Si el juego aún no se ha ganado y el usuario le da click, pierde
            if (!gameWon) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorAdmin = applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    val vibrator = vibratorAdmin.defaultVibrator
                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(300)
                }
                Toast.makeText(this, getString(R.string.text_game_over), Toast.LENGTH_LONG).show()

                for(i in 1..12) {
                    val btnCard = findViewById<View>(
                        resources.getIdentifier("card$i", "id", this.packageName)
                    ) as ImageButton
                    if (i == card) {
                        btnCard.setBackgroundResource(R.drawable.icon_chempe)
                    } else {
                        btnCard.setBackgroundResource(R.drawable.icon_cheems)
                    }
                }
            }
        } else {
            // Continúa en el juego
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$card", "id", this.packageName)
            ) as ImageButton
            if (card !in flippedSet) {
                btnCard.setBackgroundResource(R.drawable.icon_cheems)
                flippedCards++
                flippedSet.add(card)
            }

            if (flippedCards == 11) {
                Toast.makeText(this, R.string.text_win, Toast.LENGTH_SHORT).show()
                gameWon = true // Marcar que el usuario ganó

                // Voltear la carta perdedora
                val btnCard = findViewById<View>(
                    resources.getIdentifier("card$gameOverCard", "id", this.packageName)
                ) as ImageButton
                btnCard.setBackgroundResource(R.drawable.icon_chempe)
            }
        }
    }

    fun restartGame(){
        flippedCards = 0
        flippedSet.clear()
        gameWon = false
        gameOverCard = (1..12).random()

        for(i in 1..12){
            val btnCard = findViewById<View>(
                resources.getIdentifier("card$i", "id", this.packageName)
            ) as ImageButton
            btnCard.setBackgroundResource(R.drawable.icon_pregunta)
            btnCard.isClickable = true // Hacer todas las cartas clickeables nuevamente
        }
        Toast.makeText(this, "Nuevo juego iniciado", Toast.LENGTH_SHORT).show()
    }
    fun flipped(){
        for(i in 1..12){
            val btnCard = findViewById<View>(
            resources.getIdentifier("card$i", "id", this.packageName)
        ) as ImageButton
            if(gameOverCard ==i){
                btnCard.setBackgroundResource(R.drawable.icon_chempe)
            }else{
                btnCard.setBackgroundResource(R.drawable.icon_cheems)
            }
    }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.card1 -> {flip(1)}
            R.id.card2 -> {flip(2)}
            R.id.card3 -> {flip(3)}
            R.id.card4 -> {flip(4)}
            R.id.card5 -> {flip(5)}
            R.id.card6 -> {flip(6)}
            R.id.card7 -> {flip(7)}
            R.id.card8 -> {flip(8)}
            R.id.card9 -> {flip(9)}
            R.id.card10 -> {flip(10)}
            R.id.card11 -> {flip(11)}
            R.id.card12 -> {flip(12)}
        }
    }

}