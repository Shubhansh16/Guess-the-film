package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel:ViewModel() {

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }

    private val timer:CountDownTimer

    private val _currentTime=MutableLiveData<Long>()

    val currentTime: LiveData<Long>
    get()=_currentTime

    val currentTimeString=Transformations.map(currentTime,{time ->
        DateUtils.formatElapsedTime(time)
    })

    // The current word
    private val _word = MutableLiveData<String>()
    val word : LiveData<String>
    get()=_word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
    get()=_score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val _eventGameFinished= MutableLiveData<Boolean>()
    val eventGameFinished:LiveData<Boolean>
    get()=_eventGameFinished


    init {
        resetList()
        nextWord()
        _score.value=0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                // TODO implement what should happen each tick of the timer
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {

                _currentTime.value = DONE
                this@GameViewModel._eventGameFinished.value = true
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    private fun resetList() {
        wordList = mutableListOf(
            "Albert Pinto Ko Gussa Kyoon Aata Hai",
            "Shin Shinaki Boobla Boo",
            "Bhediyon Ka Samooh",
            "Jal Bin Machhli Nritya Bin Bijli" ,
            "Ghulam-E-Mustafa" ,
            "Dhoti Lota Aur Chowpatty" ,
            "Kuku Mathur Ki Jhand Ho Gayi" ,
            "Dr. Kotnis Ki Amar Kahani" ,
            "Salim Langde Pe Mat Ro" ,
            "Matru Ki Bijlee Ka Mandola" ,
            "Balwinder Singh Famous Ho Gaya" ,
            "Chaarfutiya Chhokare" ,
            "Amar Akbar Anthony" ,
            "Luv Shuv Tey Chicken Khurana" ,
            "Dilruba Tangewali" ,
            "Jajantaram Mamantaram" ,
            "Mohan Joshi Hazir Ho!" ,
            "Murde Ki Jaan Khatre Mein" ,
            "Mehandi Ban Gai Khoon" ,
            "Lashtam Pashtam" ,
            "Anaarkali of Aarah"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
         resetList()
        }
        _word.value = wordList.removeAt(0)

    }

    fun onSkip() {
        _score.value=(score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value =(score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinished.value=false
    }
}