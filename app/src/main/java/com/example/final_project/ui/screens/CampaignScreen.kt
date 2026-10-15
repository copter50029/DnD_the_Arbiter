package com.example.final_project.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_project.R
import com.example.final_project.data.local.entity.AppLanguage
import com.example.final_project.data.local.entity.CampaignEntity
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
import com.example.final_project.ui.viewmodel.CampaignViewModel

@Composable
fun CampaignScreen(
    viewModel: CampaignViewModel,
    onNavigateToSession: (Long) -> Unit,
    onNavigateToDramatis: (Long) -> Unit
) {
    val campaigns by viewModel.campaigns.collectAsState()
    val stats by viewModel.campaignStats.collectAsState()
    val settings by viewModel.userSettings.collectAsState()

    var showNewCampaignDialog by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    var campaignToDelete by remember { mutableStateOf<CampaignEntity?>(null) }

    Scaffold(
        containerColor = DarkLeatherDeep,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewCampaignDialog = true },
                containerColor = ArcaneGold,
                contentColor = DarkLeatherDeep
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.btn_new_campaign))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header: Title & Settings Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.screen_campaigns_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = ArcaneGoldBright
                    )
                    Text(
                        text = stringResource(R.string.screen_campaigns_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = ArcaneGoldDark
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // API Key Config Button
                    IconButton(
                        onClick = { showApiKeyDialog = true },
                        modifier = Modifier
                            .background(DarkLeatherCard, CircleShape)
                            .size(40.dp)
                    ) {
                        val isKeySet = !settings?.apiKey.isNullOrBlank()
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "API Key",
                            tint = if (isKeySet) ArcaneGold else DeepCrimson,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Language Toggle Switch
                    IconButton(
                        onClick = { viewModel.toggleLanguage() },
                        modifier = Modifier
                            .background(DarkLeatherCard, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = ArcaneGoldBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Language & API Key Banner status
            val isKeyBound = !settings?.apiKey.isNullOrBlank()
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                color = if (isKeyBound) ArcaneGold.copy(alpha = 0.1f) else DeepCrimson.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isKeyBound) ArcaneGoldDark.copy(alpha = 0.4f) else DeepCrimson.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isKeyBound) {
                            stringResource(R.string.settings_api_key_status_bound)
                        } else {
                            stringResource(R.string.settings_api_key_status_missing)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isKeyBound) ArcaneGoldBright else Color(0xFFFF8A80)
                    )
                    Text(
                        text = if (settings?.preferredLanguage == AppLanguage.TH) "TH" else "EN",
                        style = MaterialTheme.typography.labelLarge,
                        color = ArcaneGold
                    )
                }
            }

            // Campaigns List or Empty Placeholder
            if (campaigns.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = ArcaneGoldDark.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.no_campaigns_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(campaigns, key = { it.id }) { campaign ->
                        val campStats = stats[campaign.id]
                        CampaignItemCard(
                            campaign = campaign,
                            sessionsCount = campStats?.sessionCount ?: 0,
                            bossCount = campStats?.bossCount ?: 0,
                            npcCount = campStats?.npcCount ?: 0,
                            onOpenSession = { onNavigateToSession(campaign.id) },
                            onOpenDramatis = { onNavigateToDramatis(campaign.id) },
                            onDelete = { campaignToDelete = campaign }
                        )
                    }
                }
            }
        }
    }

    // Dialog: Inscribe New Campaign
    if (showNewCampaignDialog) {
        NewCampaignDialog(
            onDismiss = { showNewCampaignDialog = false },
            onConfirm = { title, desc, lvl ->
                viewModel.createCampaign(title, desc, lvl)
                showNewCampaignDialog = false
            }
        )
    }

    // Dialog: Configure Gemini API Key & Model
    if (showApiKeyDialog) {
        ApiKeyDialog(
            currentKey = settings?.apiKey.orEmpty(),
            currentModel = settings?.selectedModel ?: "gemini-2.0-flash",
            onDismiss = { showApiKeyDialog = false },
            onSave = { key, model ->
                viewModel.saveSettings(key, model)
                showApiKeyDialog = false
            }
        )
    }

    // Dialog: Confirm Delete Campaign
    campaignToDelete?.let { campaign ->
        AlertDialog(
            onDismissRequest = { campaignToDelete = null },
            containerColor = DarkLeatherCard,
            title = {
                Text(
                    text = stringResource(R.string.confirm_delete_title),
                    fontFamily = FontFamily.Serif,
                    color = ArcaneGoldBright
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.confirm_delete_msg) + "\n\n«${campaign.title}»",
                    color = Color.White
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteCampaign(campaign.id)
                    campaignToDelete = null
                }) {
                    Text(
                        text = stringResource(R.string.action_delete),
                        color = DeepCrimson,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { campaignToDelete = null }) {
                    Text(
                        text = stringResource(R.string.action_cancel),
                        color = ArcaneGold
                    )
                }
            }
        )
    }
}

@Composable
private fun CampaignItemCard(
    campaign: CampaignEntity,
    sessionsCount: Int,
    bossCount: Int,
    npcCount: Int,
    onOpenSession: () -> Unit,
    onOpenDramatis: () -> Unit,
    onDelete: () -> Unit
) {
    GrimoireCard {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = campaign.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = ArcaneGoldBright,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (campaign.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = campaign.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = DeepCrimson.copy(alpha = 0.8f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Badges row: Party Level & Quick Statistics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RuneBadge(
                    label = "Lv",
                    value = campaign.partyLevel.toString(),
                    accentColor = ArcaneGold
                )
                RuneBadge(
                    label = stringResource(R.string.campaign_stat_sessions),
                    value = sessionsCount.toString(),
                    accentColor = Color(0xFF64B5F6)
                )
                RuneBadge(
                    label = stringResource(R.string.campaign_stat_bosses),
                    value = bossCount.toString(),
                    accentColor = DeepCrimson
                )
                RuneBadge(
                    label = stringResource(R.string.campaign_stat_npcs),
                    value = npcCount.toString(),
                    accentColor = Color(0xFF81C784)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Launchers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GrimoireButton(
                    text = stringResource(R.string.nav_session_architect),
                    onClick = onOpenSession,
                    modifier = Modifier.weight(1f)
                )
                GrimoireButton(
                    text = stringResource(R.string.nav_dramatis_personae),
                    onClick = onOpenDramatis,
                    modifier = Modifier.weight(1f),
                    isCrimson = true
                )
            }
        }
    }
}

@Composable
private fun NewCampaignDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var partyLevel by remember { mutableFloatStateOf(3f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkLeatherCard,
        title = {
            Text(
                text = stringResource(R.string.dialog_new_campaign_title),
                fontFamily = FontFamily.Serif,
                color = ArcaneGoldBright
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                GrimoireTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = stringResource(R.string.field_campaign_title)
                )
                GrimoireTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = stringResource(R.string.field_campaign_desc),
                    minLines = 3
                )
                Column {
                    Text(
                        text = "${stringResource(R.string.field_campaign_party_level)}: ${partyLevel.toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArcaneGoldBright
                    )
                    Slider(
                        value = partyLevel,
                        onValueChange = { partyLevel = it },
                        valueRange = 1f..20f,
                        steps = 18,
                        colors = SliderDefaults.colors(
                            thumbColor = ArcaneGold,
                            activeTrackColor = ArcaneGold
                        )
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(title, desc, partyLevel.toInt()) },
                enabled = title.isNotBlank()
            ) {
                Text(
                    text = stringResource(R.string.btn_new_campaign),
                    color = ArcaneGold,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel), color = Color.Gray)
            }
        }
    )
}

@Composable
private fun ApiKeyDialog(
    currentKey: String,
    currentModel: String,
    onDismiss: () -> Unit,
    onSave: (key: String, model: String) -> Unit
) {
    var keyText by remember { mutableStateOf(currentKey) }
    var selectedModel by remember { mutableStateOf(currentModel.ifBlank { "gemini-3.8-flash" }) }
    val availableModels = listOf(
        "gemini-3.8-flash",
        "gemini-3.5-flash-lite"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkLeatherCard,
        title = {
            Text(
                text = stringResource(R.string.settings_api_key_title),
                fontFamily = FontFamily.Serif,
                color = ArcaneGoldBright
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.settings_api_key_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
                GrimoireTextField(
                    value = keyText,
                    onValueChange = { keyText = it },
                    label = "Gemini API Key",
                    placeholder = stringResource(R.string.settings_api_key_hint)
                )

                HorizontalDivider(color = ArcaneGoldDark.copy(alpha = 0.3f))

                Text(
                    text = stringResource(R.string.settings_model_title),
                    style = MaterialTheme.typography.labelMedium,
                    color = ArcaneGoldBright
                )
                Text(
                    text = stringResource(R.string.settings_model_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableModels) { model ->
                        val isSelected = selectedModel == model
                        Surface(
                            modifier = Modifier.clickable { selectedModel = model },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) ArcaneGoldDark else Color(0xFF231A18),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) ArcaneGold else Color.Gray.copy(alpha = 0.3f)
                            )
                        ) {
                            Text(
                                text = model,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) ArcaneGoldBright else Color.Gray,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                GrimoireTextField(
                    value = selectedModel,
                    onValueChange = { selectedModel = it },
                    label = "Active Model Name",
                    placeholder = "gemini-3.8-flash"
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(keyText, selectedModel) }) {
                Text(
                    text = stringResource(R.string.settings_api_key_save),
                    color = ArcaneGold,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel), color = Color.Gray)
            }
        }
    )
}
