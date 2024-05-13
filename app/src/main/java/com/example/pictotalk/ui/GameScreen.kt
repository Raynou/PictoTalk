package com.example.pictotalk.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.pictotalk.StateManager
import com.example.pictotalk.data_access.PictogramDAO
import com.example.pictotalk.entities.Pictogram
import com.example.pictotalk.entities.Deck
import com.example.pictotalk.game.GameManager
import com.example.pictotalk.game.SettingsManager

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.pictotalk.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import android.Manifest
import android.speech.tts.Voice
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import com.example.pictotalk.game.VoiceOutputManager
import com.example.pictotalk.ui.components.ConfirmationDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    navigateUp: () -> Unit = {}
) {
    val context = LocalContext.current
    val stateManager = StateManager.getInstance()
    val deck = stateManager.deck
    val cards = getCards(deck, context)
    val settingsManager = SettingsManager(context)
    val difficulty = settingsManager.getDifficulty()
    val gameManager = stateManager.gameManager ?: GameManager(difficulty, cards)
    val voiceOutputManager = VoiceOutputManager()
    voiceOutputManager.init(context)
    if(stateManager.gameManager == null) {
        stateManager.gameManager = gameManager
    }

    var showFeedback by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isAnswerCorrect by remember { mutableStateOf(false) }
    var showEndGameScreen by remember { mutableStateOf(false) }

    var text by remember {
        mutableStateOf("")
    }

    var isRecording by remember { mutableStateOf(false) }
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val res =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
                        ?: ""
                val oldScore = gameManager.score
                text = res
                gameManager.evaluateAnswer(res)
                isAnswerCorrect = oldScore < gameManager.score
                showFeedback = true
            }
            isRecording = false
            if(gameManager.isEndGame()) {
                showEndGameScreen = true
            } else {
                gameManager.nextCard()
            }

        }
    )

    val speechPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    // Status variables
    val progressStatus by gameManager.getProgress()
    val currentCard by gameManager.getCurrentCard()

    Scaffold { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = {
                    showDialog = true
                }) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }


                // Game progress
                LinearProgressIndicator(
                    progress = progressStatus,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            Text(currentCard.phrase, fontSize = 24.sp)

            Column (
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(400.dp)
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    onClick = {
                        voiceOutputManager.speak(currentCard.phrase)
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(painter = painterResource(id = R.drawable.cards), contentDescription = "Pictogram Image")
                    }
                }

            }

            FloatingActionButton(
                onClick = {
                    if (!isRecording) {
                        if (speechPermissionState.status.isGranted) {
                            speechRecognition(activityResultLauncher)
                            isRecording = true
                        } else {
                            speechPermissionState.launchPermissionRequest()
                        }
                    }
                },
                modifier = Modifier
                            .size(72.dp)
            ) {
                Icon(Icons.Filled.Mic, contentDescription = "Speak")
            }
        }
    }

    if(showFeedback) {
        FullScreenFeedback(isAnswerCorrect) {
            showFeedback = false
        }
    }

    if(showDialog) {
        ConfirmationDialog(
            title = "¿Estás seguro que deseas salir?",
            onConfirm = {
                showDialog = false
                navigateUp()
            },
            onDismiss = { showDialog = false },
            content = {
                Text("Si sales perderás tu progreso")
            }
        )
    }

    if(showEndGameScreen) {
        EndGameScreen(
            totalCards = gameManager.getTotalCards(),
            score = gameManager.score,
            onBackToMenu = {
                navigateUp()
            }
        )
    }

    BackHandler {
        showDialog = true
    }

    LaunchedEffect(true) {
        stateManager.gameManager = null
    }
}

fun getCards(deck: Deck, context: Context): List<Pictogram> {
    val cardDAO = PictogramDAO(context)
    return cardDAO.getCardsByDeckId(deck.id)
}

fun speechRecognition(
    activityResultLauncher: ActivityResultLauncher<Intent>
) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Di algo")
    }
    activityResultLauncher.launch(intent)
}

@Preview(showBackground = true)
@Composable
fun GameDefaultPreview() {
    GameScreen()
}

