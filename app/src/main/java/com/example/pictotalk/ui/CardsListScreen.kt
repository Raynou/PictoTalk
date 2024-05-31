import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictotalk.R
import com.example.pictotalk.StateManager
import com.example.pictotalk.data_access.PictogramDAO
import com.example.pictotalk.game.Difficulty
import com.example.pictotalk.ui.theme.CristalLake

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsListScreen(
    topAppBar: @Composable () -> Unit = {},
    navigateUp: () -> Unit = {}
) {
    val stateManager = StateManager.getInstance()
    val pictogramDAO = PictogramDAO(LocalContext.current)
    val pictograms = pictogramDAO.getAllPictograms()
    val difficulties = listOf(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD)

    // Estado para los checkbox
    val checkboxStates = remember {
        mutableStateListOf(*Array(pictograms.size) {
            stateManager.newDeckPictograms.any { pictogram -> pictogram.id == pictograms[it].id }
        })
    }

    // Estado para los chips de filtro
    val chipStates = remember { mutableStateListOf(*Array(difficulties.size) { false }) }

    // Estado para la lista de pictogramas filtrados
    val filteredPictograms = remember { mutableStateListOf(*pictograms.toTypedArray()) }

    Scaffold(
        topBar = { topAppBar() },
        containerColor = Color.White
    ) { innerPadding ->
        // Añadir chips para filtrar por dificultad
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            ChipsFilter(
                difficulties,
                chipStates,
                onChipClicked = {
                    // Filtrar los pictogramas por las dificultades seleccionadas
                    val selectedDifficulties = difficulties.filterIndexed { index, _ -> chipStates[index] }
                    val newFilteredPictograms = if (selectedDifficulties.isEmpty()) {
                        pictograms
                    } else {
                        pictograms.filter { it.difficulty in selectedDifficulties }
                    }
                    filteredPictograms.clear()
                    filteredPictograms.addAll(newFilteredPictograms)

                    // Actualizar el estado de los checkbox basado en los pictogramas filtrados
                    checkboxStates.clear()
                    checkboxStates.addAll(Array(filteredPictograms.size) {
                        stateManager.newDeckPictograms.any { pictogram -> pictogram.id == filteredPictograms[it].id }
                    })
                }
            )
            LazyColumn {
                itemsIndexed(filteredPictograms) { index, pictogram ->
                    val interactionSource = remember { MutableInteractionSource() }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(71.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = rememberRipple(bounded = true),
                            ) {
                                checkboxStates[index] = !checkboxStates[index]
                                if (checkboxStates[index]) {
                                    stateManager.newDeckPictograms.add(pictogram)
                                } else {
                                    stateManager.newDeckPictograms.remove(pictogram)
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Spacer(modifier = Modifier.width(10.dp))

                        Image(
                            painter = painterResource(id = pictogram.image),
                            contentDescription = "Card",
                            modifier = Modifier
                                .size(56.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            val difficultyColor = when (pictogram.difficulty) {
                                Difficulty.EASY -> Color(0xFF0D940D)
                                Difficulty.MEDIUM -> Color(0xFFACAC1A)
                                Difficulty.HARD -> Color(0xFFB31B1B)
                            }
                            Text(
                                text = pictogram.phrase,
                                modifier = Modifier.width(188.dp),
                                fontSize = 18.sp
                            )
                            val textDifficulty = when (pictogram.difficulty) {
                                Difficulty.EASY -> "Fácil"
                                Difficulty.MEDIUM -> "Medio"
                                Difficulty.HARD -> "Dificil"
                            }
                            Text(
                                text = textDifficulty,
                                modifier = Modifier.width(188.dp),
                                fontSize = 10.sp,
                                color = difficultyColor
                            )
                        }

                        Spacer(modifier = Modifier.width(75.dp))

                        Checkbox(checked = checkboxStates[index], onCheckedChange = null)
                    }
                    if (index < filteredPictograms.size - 1) {
                        Divider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipsFilter(
    difficulties: List<Difficulty>,
    chipStates: MutableList<Boolean>,
    onChipClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier.padding(5.dp)
    ) {
        difficulties.forEachIndexed { index, difficulty ->
            val difficultyText = when (difficulty) {
                Difficulty.EASY -> "Fácil"
                Difficulty.MEDIUM -> "Medio"
                Difficulty.HARD -> "Difícil"
            }
            FilterChip(
                selected = chipStates[index],
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CristalLake,
                    selectedLabelColor = Color.White,
                    selectedLeadingIconColor = Color.White
                ),
                onClick = {
                    chipStates[index] = !chipStates[index]
                    onChipClicked()
                },
                label = {
                    Text(text = difficultyText)
                },
                leadingIcon = if (chipStates[index]) {
                    {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Checked"
                        )
                    }
                } else {
                    null
                }
            )

            if (index < difficulties.size - 1) {
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardsListDefaultPreview() {
    CardsListScreen()
}
