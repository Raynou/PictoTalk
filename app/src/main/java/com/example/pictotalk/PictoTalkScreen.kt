package com.example.pictotalk

import CardsListScreen
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pictotalk.game.CrudAction
import com.example.pictotalk.game.SettingsManager
import com.example.pictotalk.ui.GameScreen
import com.example.pictotalk.ui.MainMenuScreen
import com.example.pictotalk.ui.NewDeckScreen
import com.example.pictotalk.ui.components.PictoTalkTopAppBar
import kotlinx.coroutines.delay

enum class PictoTalkScreen(@StringRes val title: Int? = null) {
    Start(title = R.string.app_name),
    Game,
    NewDeck(title = R.string.new_deck),
    CardsList(title = R.string.card_list),
    EditDeck(title = R.string.edit_deck)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PictoTalkApp(
    navController: NavHostController = rememberNavController()
) {
    val backStateEntry by navController.currentBackStackEntryAsState()
    val currentScreen = PictoTalkScreen.valueOf(
        backStateEntry?.destination?.route ?: PictoTalkScreen.Start.name
    )
    val stateManager = StateManager.getInstance()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold {  innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PictoTalkScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(PictoTalkScreen.Start.name) {
                MainMenuScreen(
                    topAppBar = {
                        PictoTalkTopAppBar(
                            SettingsManager(LocalContext.current),
                            canManageSettings = true,
                            canGoBack = false,
                            title = "PictoTalk"
                        )
                    },
                    onDeckClicked = {
                        navController.navigate(PictoTalkScreen.Game.name)
                    },
                    onFABClicked = {
                        navController.navigate(
                            PictoTalkScreen.NewDeck.name,
                        )
                    },
                    onLongDeckClicked = {
                        navController.navigate(PictoTalkScreen.EditDeck.name)
                    }
                )
            }
            composable(PictoTalkScreen.Game.name) {
                GameScreen(
                    navigateUp = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                PictoTalkScreen.NewDeck.name
            ) {
                NewDeckScreen(
                    topAppBar = {
                        PictoTalkTopAppBar(
                            SettingsManager(LocalContext.current),
                            canManageSettings = false,
                            canGoBack = true,
                            title = "Nuevo Mazo",
                            onGoBack = {
                                stateManager.newDeckName = ""
                                stateManager.newDeckPictograms = mutableListOf()

                                // Hide the keyboard
                                keyboardController?.hide()

                                navController.popBackStack()
                            }
                        )
                    },
                    onDeckClicked = {
                        navController.navigate(PictoTalkScreen.CardsList.name)
                    },
                    navigateUp = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                PictoTalkScreen.EditDeck.name
            ) {
                NewDeckScreen(
                    topAppBar = {
                        PictoTalkTopAppBar(
                            SettingsManager(LocalContext.current),
                            canManageSettings = false,
                            canGoBack = true,
                            title = "Editar Mazo",
                            onGoBack = {
                                stateManager.newDeckName = ""
                                stateManager.newDeckPictograms = mutableListOf()

                                // Hide the keyboard
                                keyboardController?.hide()

                                navController.popBackStack()
                            }
                        )
                    },
                    onDeckClicked = {
                        navController.navigate(PictoTalkScreen.CardsList.name)
                    },
                    navigateUp = {
                        navController.popBackStack()
                    },
                    crudAction = CrudAction.EDIT
                )
            }
            composable(
                PictoTalkScreen.CardsList.name
            ) {
                CardsListScreen(
                    topAppBar = {
                        PictoTalkTopAppBar(
                            SettingsManager(LocalContext.current),
                            canManageSettings = false,
                            canGoBack = true,
                            title = "Selección de cartas",
                            onGoBack = {
                                navController.popBackStack()
                            }
                        )
                    },
                    navigateUp = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}




