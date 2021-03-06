package jp.kaleidot725.emomemo.ui.common.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import jp.kaleidot725.emomemo.R
import jp.kaleidot725.emomemo.ui.extension.getSafetyString

class SpeechRecognizerController(
    private val context: Context?,
    private val onEvent: (event: RecognizeEvent, text: String) -> Unit
) : DefaultLifecycleObserver {
    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(createRecognitionListener(onEvent))
        }
    }

    fun start() {
        speechRecognizer.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH))
    }

    fun cancel() {
        speechRecognizer.stopListening()
    }

    fun retry() {
        speechRecognizer.cancel()
        speechRecognizer.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH))
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        speechRecognizer.destroy()
    }

    private fun createRecognitionListener(onEvent: (event: RecognizeEvent, text: String) -> Unit): RecognitionListener {
        return object : RecognitionListener {
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onEvent(eventType: Int, params: Bundle) {}
            override fun onBufferReceived(buffer: ByteArray) {}
            override fun onPartialResults(partialResults: Bundle) {}
            override fun onBeginningOfSpeech() {}

            override fun onReadyForSpeech(params: Bundle) {
                onEvent(RecognizeEvent.READY_FOR_SPEECH, context.getSafetyString(R.string.audio_recognizer_start))
            }

            override fun onEndOfSpeech() {
                onEvent(RecognizeEvent.END_OF_SPEECH, context.getSafetyString(R.string.audio_recognizer_end))
            }

            override fun onError(error: Int) {
                onEvent(RecognizeEvent.RECOGNITION_FAILED, context.getSafetyString(R.string.audio_recognizer_error))
            }

            override fun onResults(results: Bundle) {
                val stringArray = results.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
                onEvent(RecognizeEvent.RECOGNITION_SUCCESS, stringArray?.getOrNull(0).toString())
            }
        }
    }

    enum class RecognizeEvent {
        READY_FOR_SPEECH,
        END_OF_SPEECH,
        RECOGNITION_SUCCESS,
        RECOGNITION_FAILED
    }
}
