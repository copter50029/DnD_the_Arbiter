package com.example.final_project.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_project.R
import com.example.final_project.data.local.entity.CharacterEntity
import com.example.final_project.data.local.entity.CharacterType
import com.example.final_project.data.remote.dto.MonsterItemDto
import com.example.final_project.ui.components.GrimoireButton
import com.example.final_project.ui.components.GrimoireCard
import com.example.final_project.ui.components.GrimoireOutlinedButton
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
import com.example.final_project.ui.viewmodel.DramatisPersonaeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DramatisPersonaeScreen(
    viewModel: DramatisPersonaeViewModel,
    prefilledMonsterIndex: String? = null
) {
    val campaigns by viewModel.campaigns.collectAsState()
    val selectedCampId by viewModel.selectedCampaignId.collectAsState()
    val characters by viewModel.characters.collectAsState()
    val characterType by viewModel.characterType.collectAsState()
    val name by viewModel.name.collectAsState()
    val backstory by viewModel.backstory.collectAsState()
    val attachedMonsterDetail by viewModel.attachedMonsterDetail.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val gameTheoryFeedback by viewModel.gameTheoryFeedback.collectAsState()
    val psychologyFeedback by viewModel.psychologyFeedback.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val editingCharacterId by viewModel.editingCharacterId.collectAsState()

    var showMonsterDialog by remember { mutableStateOf(false) }
    var campaignDropdownExpanded by remember { mutableStateOf(false) }

    // If navigated from Compendium with prefilled monster index
    LaunchedEffect(prefilledMonsterIndex) {
        if (!prefilledMonsterIndex.isNullOrBlank()) {
            viewModel.attachMonster(prefilledMonsterIndex)
        }
    }

    val activeCampaign = campaigns.firstOrNull { it.id == selectedCampId }

    Scaffold(containerColor = DarkLeatherDeep) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header & Campaign selector
            item {
                Column {
                    Text(
                        text = stringResource(R.string.screen_dramatis_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = ArcaneGoldBright
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (campaigns.isNotEmpty()) {
                        ExposedDropdownMenuBox(
                            expanded = campaignDropdownExpanded,
                            onExpandedChange = { campaignDropdownExpanded = !campaignDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = activeCampaign?.title ?: stringResource(R.string.session_select_campaign_prompt),
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = campaignDropdownExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ArcaneGold,
                                    unfocusedBorderColor = DarkLeatherBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedContainerColor = DarkLeatherCard,
                                    unfocusedContainerColor = DarkLeatherCard
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = campaignDropdownExpanded,
                                onDismissRequest = { campaignDropdownExpanded = false },
                                modifier = Modifier.background(DarkLeatherCard)
                            ) {
                                campaigns.forEach { camp ->
                                    DropdownMenuItem(
                                        text = { Text(text = camp.title, color = ArcaneGoldBright) },
                                        onClick = {
                                            viewModel.selectCampaign(camp.id)
                                            campaignDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Status Banners
            if (errorMessage != null) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = DeepCrimson.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DeepCrimson)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = errorMessage!!,
                                color = Color(0xFFFF8A80),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = { viewModel.clearForm() }
                                ) {
                                    Text(
                                        text = stringResource(R.string.action_close),
                                        color = Color.LightGray,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (successMessage != null) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF2E7D32).copy(alpha = 0.25f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50))
                    ) {
                        Text(
                            text = successMessage!!,
                            color = Color(0xFFA5D6A7),
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Role Segmented Selector: [ Dread Boss ] vs [ Notable NPC ]
            item {
                PrimaryTabRow(
                    selectedTabIndex = if (characterType == CharacterType.BOSS) 0 else 1,
                    containerColor = DarkLeatherCard,
                    contentColor = ArcaneGoldBright
                ) {
                    Tab(
                        selected = characterType == CharacterType.BOSS,
                        onClick = { viewModel.setCharacterType(CharacterType.BOSS) },
                        text = {
                            Text(
                                text = stringResource(R.string.tab_boss),
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = if (characterType == CharacterType.BOSS) DeepCrimson else Color.Gray
                            )
                        }
                    )
                    Tab(
                        selected = characterType == CharacterType.NPC,
                        onClick = { viewModel.setCharacterType(CharacterType.NPC) },
                        text = {
                            Text(
                                text = stringResource(R.string.tab_npc),
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = if (characterType == CharacterType.NPC) ArcaneGoldBright else Color.Gray
                            )
                        }
                    )
                }
            }

            // Character Studio Form
            item {
                GrimoireCard {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GrimoireTextField(
                            value = name,
                            onValueChange = { viewModel.onNameChange(it) },
                            label = stringResource(R.string.character_name_label),
                            placeholder = stringResource(R.string.character_name_hint)
                        )

                        // Attach SRD Monster Statblock Section
                        if (attachedMonsterDetail != null) {
                            val monster = attachedMonsterDetail!!
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                color = DarkLeatherDeep,
                                border = androidx.compose.foundation.BorderStroke(1.dp, ArcaneGoldDark)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stringResource(R.string.character_attached_monster, monster.name),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = ArcaneGoldBright
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            RuneBadge(label = "CR", value = monster.displayChallengeRating)
                                            RuneBadge(label = "AC", value = monster.displayArmorClass.toString())
                                            RuneBadge(label = "HP", value = "${monster.hitPoints ?: 0}")
                                        }
                                    }
                                    IconButton(onClick = { viewModel.detachMonster() }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Detach",
                                            tint = DeepCrimson
                                        )
                                    }
                                }
                            }
                        } else {
                            GrimoireOutlinedButton(
                                text = stringResource(R.string.btn_attach_monster),
                                onClick = { showMonsterDialog = true },
                                icon = Icons.Default.Link,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Backstory drafting field
                        GrimoireTextField(
                            value = backstory,
                            onValueChange = { viewModel.onBackstoryChange(it) },
                            label = stringResource(R.string.character_backstory_label),
                            placeholder = stringResource(R.string.character_backstory_hint),
                            minLines = 5,
                            maxLines = 10
                        )

                        // Dedicated Role-Based AI Engines Action Button
                        if (characterType == CharacterType.BOSS) {
                            GrimoireButton(
                                text = stringResource(R.string.btn_analyze_boss),
                                onClick = { viewModel.consultAi() },
                                icon = Icons.Default.AutoAwesome,
                                isLoading = isAnalyzing,
                                enabled = name.isNotBlank() && backstory.isNotBlank(),
                                isCrimson = true
                            )
                        } else {
                            GrimoireButton(
                                text = stringResource(R.string.btn_analyze_npc),
                                onClick = { viewModel.consultAi() },
                                icon = Icons.Default.AutoAwesome,
                                isLoading = isAnalyzing,
                                enabled = name.isNotBlank() && backstory.isNotBlank()
                            )
                        }
                    }
                }
            }

            // AI Analysis Feedback Display (Dedicated Role Engines)
            if (gameTheoryFeedback.isNotBlank() || psychologyFeedback.isNotBlank()) {
                // Card 1: Game Theory Focus (Minimax vs Tit-for-Tat)
                item {
                    GrimoireCard(borderColor = if (characterType == CharacterType.BOSS) DeepCrimson else ArcaneGold) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (characterType == CharacterType.BOSS) {
                                    stringResource(R.string.feedback_boss_tactics_title)
                                } else {
                                    stringResource(R.string.feedback_npc_gametheory_title)
                                },
                                style = MaterialTheme.typography.titleMedium,
                                color = if (characterType == CharacterType.BOSS) Color(0xFFFF8A80) else ArcaneGoldBright
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                color = DarkLeatherDeep,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkLeatherBorder)
                            ) {
                                Text(
                                    text = gameTheoryFeedback,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ParchmentLight,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }

                // Card 2: Psychological Focus (Shadow-Self vs Emotional Leverage)
                item {
                    GrimoireCard(borderColor = ArcaneGoldDark) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (characterType == CharacterType.BOSS) {
                                    stringResource(R.string.feedback_boss_psychology_title)
                                } else {
                                    stringResource(R.string.feedback_npc_psychology_title)
                                },
                                style = MaterialTheme.typography.titleMedium,
                                color = ArcaneGoldBright
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                color = DarkLeatherDeep,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkLeatherBorder)
                            ) {
                                Text(
                                    text = psychologyFeedback,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ParchmentAged,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }

                // Save Character Button
                item {
                    GrimoireButton(
                        text = stringResource(R.string.btn_save_character),
                        onClick = { viewModel.saveCharacter() },
                        icon = Icons.Default.Save,
                        isCrimson = characterType == CharacterType.BOSS
                    )
                }
            }

            // Inscribed Personages List in Realm
            item {
                Text(
                    text = "${stringResource(R.string.characters_list_header)} (${characters.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = ArcaneGoldBright
                )
            }

            if (characters.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_characters_placeholder),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )
                }
            } else {
                items(characters, key = { it.id }) { char ->
                    PersonageItemCard(
                        character = char,
                        isEditing = editingCharacterId == char.id,
                        onEdit = { viewModel.loadCharacterForEdit(char) },
                        onDelete = { viewModel.deleteCharacter(char.id) }
                    )
                }
            }
        }
    }

    // Dialog to search and attach SRD Monster
    if (showMonsterDialog) {
        SelectMonsterDialog(
            onDismiss = { showMonsterDialog = false },
            onSelect = { monsterIndex ->
                viewModel.attachMonster(monsterIndex)
                showMonsterDialog = false
            }
        )
    }
}

@Composable
private fun PersonageItemCard(
    character: CharacterEntity,
    isEditing: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    GrimoireCard(
        borderColor = if (character.characterType == CharacterType.BOSS) DeepCrimson.copy(alpha = 0.5f) else ArcaneGoldDark.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RuneBadge(
                        label = "Type",
                        value = character.characterType.name,
                        accentColor = if (character.characterType == CharacterType.BOSS) DeepCrimson else ArcaneGold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = ArcaneGoldBright
                    )
                }
                if (character.rawBackstoryDraft.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = character.rawBackstoryDraft,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        maxLines = 2
                    )
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = ArcaneGold)
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = DeepCrimson)
                }
            }
        }
    }
}

@Composable
private fun SelectMonsterDialog(
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    // Standard SRD archetypes for quick selection dialog
    val quickMonsters = listOf(
        "lich" to "Lich (CR 21)",
        "adult-red-dragon" to "Adult Red Dragon (CR 17)",
        "aboleth" to "Aboleth (CR 10)",
        "mind-flayer" to "Mind Flayer (CR 7)",
        "medusa" to "Medusa (CR 6)",
        "gladiator" to "Gladiator (CR 5)",
        "mage" to "Mage (CR 6)",
        "assassin" to "Assassin (CR 8)",
        "vampire" to "Vampire (CR 13)",
        "goblin" to "Goblin (CR 1/4)"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkLeatherCard,
        title = {
            Text(
                text = stringResource(R.string.dialog_select_monster_title),
                fontFamily = FontFamily.Serif,
                color = ArcaneGoldBright
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(300.dp)
            ) {
                items(quickMonsters) { (index, name) ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(index) },
                        shape = RoundedCornerShape(6.dp),
                        color = DarkLeatherDeep,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkLeatherBorder)
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArcaneGoldBright,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel), color = Color.Gray)
            }
        }
    )
}
