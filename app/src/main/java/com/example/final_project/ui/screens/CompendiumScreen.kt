package com.example.final_project.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_project.R
import com.example.final_project.data.remote.dto.MonsterDetailDto
import com.example.final_project.data.remote.dto.MonsterItemDto
import com.example.final_project.ui.components.GrimoireButton
import com.example.final_project.ui.components.GrimoireCard
import com.example.final_project.ui.components.GrimoireTextField
import com.example.final_project.ui.components.RuneBadge
import com.example.final_project.ui.theme.ArcaneGold
import com.example.final_project.ui.theme.ArcaneGoldBright
import com.example.final_project.ui.theme.ArcaneGoldDark
import com.example.final_project.ui.theme.DarkLeatherBorder
import com.example.final_project.ui.theme.DarkLeatherCard
import com.example.final_project.ui.theme.DarkLeatherDeep
import com.example.final_project.ui.theme.DeepCrimson
import com.example.final_project.ui.theme.ParchmentAged
import com.example.final_project.ui.theme.ParchmentLight
import com.example.final_project.ui.viewmodel.CompendiumViewModel

@Composable
fun CompendiumScreen(
    viewModel: CompendiumViewModel,
    onExportToDramatis: (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedMonsterDetail by viewModel.selectedMonsterDetail.collectAsState()
    val isLoadingDetail by viewModel.isLoadingDetail.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(containerColor = DarkLeatherDeep) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Screen Header
            Text(
                text = stringResource(R.string.screen_compendium_title),
                style = MaterialTheme.typography.headlineMedium,
                color = ArcaneGoldBright
            )
            Text(
                text = stringResource(R.string.compendium_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = ArcaneGoldDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar with Live Debounce
            GrimoireTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                label = "Search SRD Bestiary",
                placeholder = stringResource(R.string.compendium_search_hint),
                trailingIcon = {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = ArcaneGold,
                            strokeWidth = 2.dp
                        )
                    } else if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = ArcaneGold)
                        }
                    } else {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = ArcaneGold)
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Error display
            if (errorMessage != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    color = DeepCrimson.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DeepCrimson)
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFFF8A80),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Split View / Detail or Results
            if (selectedMonsterDetail != null) {
                MonsterDetailView(
                    detail = selectedMonsterDetail!!,
                    onClose = { viewModel.clearSelectedMonster() },
                    onExport = { onExportToDramatis(selectedMonsterDetail!!.index) }
                )
            } else if (isLoadingDetail) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = ArcaneGold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.compendium_loading),
                            style = MaterialTheme.typography.bodySmall,
                            color = ArcaneGoldBright
                        )
                    }
                }
            } else {
                // Results List
                Text(
                    text = "Monsters Index (${searchResults.size})",
                    style = MaterialTheme.typography.titleSmall,
                    color = ArcaneGold
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (searchResults.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.compendium_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(searchResults, key = { it.index }) { monster ->
                            MonsterListItem(
                                monster = monster,
                                onClick = { viewModel.selectMonster(monster.index) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonsterListItem(
    monster: MonsterItemDto,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = DarkLeatherCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkLeatherBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = monster.name,
                style = MaterialTheme.typography.bodyLarge,
                color = ArcaneGoldBright,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = ArcaneGoldDark,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun MonsterDetailView(
    detail: MonsterDetailDto,
    onClose: () -> Unit,
    onExport: () -> Unit
) {
    GrimoireCard(borderColor = ArcaneGold) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Title & Close Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = detail.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = ArcaneGoldBright
                        )
                        Text(
                            text = "${detail.size.orEmpty()} ${detail.type.orEmpty()} • ${detail.alignment.orEmpty()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = ArcaneGold)
                    }
                }
            }

            // Stat Block Runes: CR, AC, HP
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RuneBadge(
                        label = stringResource(R.string.compendium_stat_cr),
                        value = detail.displayChallengeRating,
                        accentColor = DeepCrimson
                    )
                    RuneBadge(
                        label = stringResource(R.string.compendium_ac_label),
                        value = "${detail.displayArmorClass}",
                        accentColor = ArcaneGold
                    )
                    RuneBadge(
                        label = stringResource(R.string.compendium_hp_label),
                        value = "${detail.hitPoints ?: 0} (${detail.hitDice ?: ""})",
                        accentColor = Color(0xFF4CAF50)
                    )
                }
            }

            // Export to Dramatis Personae action
            item {
                GrimoireButton(
                    text = stringResource(R.string.btn_export_to_dramatis),
                    onClick = onExport,
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    isCrimson = true
                )
            }

            // Special Traits
            val abilities = detail.specialAbilities
            if (!abilities.isNullOrEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.compendium_special_abilities_header),
                        style = MaterialTheme.typography.titleMedium,
                        color = ArcaneGoldBright
                    )
                }
                items(abilities) { ability ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        color = DarkLeatherDeep,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkLeatherBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = ability.name,
                                style = MaterialTheme.typography.titleSmall,
                                color = ArcaneGold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = ability.desc,
                                style = MaterialTheme.typography.bodySmall,
                                color = ParchmentAged
                            )
                        }
                    }
                }
            }

            // Actions & Legendary Abilities
            if (!detail.actions.isNullOrEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.compendium_actions_header),
                        style = MaterialTheme.typography.titleMedium,
                        color = ArcaneGoldBright
                    )
                }
                items(detail.actions) { action ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        color = DarkLeatherDeep,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkLeatherBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = action.name,
                                style = MaterialTheme.typography.titleSmall,
                                color = ArcaneGold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = action.desc,
                                style = MaterialTheme.typography.bodySmall,
                                color = ParchmentAged
                            )
                        }
                    }
                }
            }
        }
    }
}
