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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.final_project.data.local.entity.SessionLogEntity
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
import com.example.final_project.ui.viewmodel.SessionArchitectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionArchitectScreen(
    viewModel: SessionArchitectViewModel
) {
    val campaigns by viewModel.campaigns.collectAsState()
    val selectedCampId by viewModel.selectedCampaignId.collectAsState()
    val pastEchoes by viewModel.pastEchoes.collectAsState()
    val allSessions by viewModel.allSessions.collectAsState()
    val currentDraft by viewModel.currentDraft.collectAsState()
    val pastEventsSummary by viewModel.pastEventsSummary.collectAsState()
    val sessionNumber by viewModel.sessionNumber.collectAsState()
    val isConsultingOracle by viewModel.isConsultingOracle.collectAsState()
    val aiPacingFeedback by viewModel.aiPacingFeedback.collectAsState()
    val aiDilemmaFeedback by viewModel.aiDilemmaFeedback.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val editingSessionId by viewModel.editingSessionId.collectAsState()

    var showEchoesExpanded by remember { mutableStateOf(false) }
    var showHistoryExpanded by remember { mutableStateOf(false) }
    var campaignDropdownExpanded by remember { mutableStateOf(false) }

    val activeCampaign = campaigns.firstOrNull { it.id == selectedCampId }

    Scaffold(containerColor = DarkLeatherDeep) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Title & Campaign Selector Dropdown
            item {
                Column {
                    Text(
                        text = stringResource(R.string.screen_session_title),
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

            // Status feedback banners (Error / Success)
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
                                    onClick = { viewModel.clearFeedback() }
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

            // SECTION 1: Context Header — Collapsible Past Session Echoes (last 2-3 sessions)
            item {
                GrimoireCard(borderColor = ArcaneGoldDark.copy(alpha = 0.4f)) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showEchoesExpanded = !showEchoesExpanded },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = ArcaneGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${stringResource(R.string.session_recap_header)} (${pastEchoes.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = ArcaneGoldBright
                                )
                            }
                            Icon(
                                imageVector = if (showEchoesExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = ArcaneGold
                            )
                        }

                        AnimatedVisibility(visible = showEchoesExpanded) {
                            Column(
                                modifier = Modifier.padding(top = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (pastEchoes.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.session_recap_empty),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.LightGray
                                    )
                                } else {
                                    pastEchoes.forEach { echo ->
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(6.dp),
                                            color = DarkLeatherDeep,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkLeatherBorder)
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp)) {
                                                Text(
                                                    text = stringResource(R.string.session_number_format, echo.sessionNumber),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = ArcaneGold,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = echo.currentDraft.ifBlank { echo.pastEventsSummary },
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.White,
                                                    maxLines = 3
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 2: Drafting Area
            item {
                GrimoireCard {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.session_draft_header),
                                style = MaterialTheme.typography.titleMedium,
                                color = ArcaneGoldBright
                            )

                            RuneBadge(
                                label = "Session",
                                value = "#$sessionNumber",
                                accentColor = ArcaneGold
                            )
                        }

                        GrimoireTextField(
                            value = pastEventsSummary,
                            onValueChange = { viewModel.onPastEventsChange(it) },
                            label = stringResource(R.string.session_past_events_hint),
                            minLines = 2,
                            maxLines = 4
                        )

                        GrimoireTextField(
                            value = currentDraft,
                            onValueChange = { viewModel.onDraftChange(it) },
                            label = stringResource(R.string.session_draft_hint),
                            minLines = 6,
                            maxLines = 14
                        )

                        // Action 1: Consult the Oracle (Gemini 3.8 Flash)
                        GrimoireButton(
                            text = stringResource(R.string.btn_consult_oracle),
                            onClick = { viewModel.consultOracle() },
                            icon = Icons.Default.AutoAwesome,
                            isLoading = isConsultingOracle,
                            enabled = currentDraft.isNotBlank()
                        )
                    }
                }
            }

            // SECTION 3: AI Analysis Cards Display
            if (aiPacingFeedback.isNotBlank() || aiDilemmaFeedback.isNotBlank()) {
                // Card A: Psychological Pacing & Player Agency
                item {
                    GrimoireCard(borderColor = ArcaneGold.copy(alpha = 0.6f)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = ArcaneGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = stringResource(R.string.session_ai_card_psychology_title),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = ArcaneGoldBright
                                    )
                                    Text(
                                        text = stringResource(R.string.session_ai_card_psychology_subtitle),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ArcaneGoldDark
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                color = DarkLeatherDeep.copy(alpha = 0.7f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkLeatherBorder)
                            ) {
                                Text(
                                    text = aiPacingFeedback,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ParchmentAged,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }
                }

                // Card B: Game Theory & Choice Architecture
                item {
                    GrimoireCard(borderColor = DeepCrimson.copy(alpha = 0.8f)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = DeepCrimson,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = stringResource(R.string.session_ai_card_game_theory_title),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFFFF8A80)
                                    )
                                    Text(
                                        text = stringResource(R.string.session_ai_card_game_theory_subtitle),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.LightGray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                color = DarkLeatherDeep.copy(alpha = 0.7f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DeepCrimson.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = aiDilemmaFeedback,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ParchmentLight,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }
                }

                // Commit action to Room DB
                item {
                    GrimoireButton(
                        text = stringResource(R.string.btn_save_session),
                        onClick = { viewModel.saveSession() },
                        icon = Icons.Default.Save,
                        isCrimson = true
                    )
                }
            }

            // SECTION 4: Past Recorded Sessions Drawer
            if (allSessions.isNotEmpty()) {
                item {
                    GrimoireCard {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showHistoryExpanded = !showHistoryExpanded },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${stringResource(R.string.session_history_header)} (${allSessions.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = ArcaneGoldBright
                                )
                                Icon(
                                    imageVector = if (showHistoryExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = ArcaneGold
                                )
                            }

                            AnimatedVisibility(visible = showHistoryExpanded) {
                                Column(
                                    modifier = Modifier.padding(top = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    allSessions.forEach { sess ->
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { viewModel.loadSessionForEdit(sess) },
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (editingSessionId == sess.id) ArcaneGold.copy(alpha = 0.2f) else DarkLeatherDeep,
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                if (editingSessionId == sess.id) ArcaneGold else DarkLeatherBorder
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = stringResource(R.string.session_number_format, sess.sessionNumber),
                                                        style = MaterialTheme.typography.titleSmall,
                                                        color = ArcaneGoldBright
                                                    )
                                                    Text(
                                                        text = sess.currentDraft.take(80) + if (sess.currentDraft.length > 80) "..." else "",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = Color.LightGray
                                                    )
                                                }
                                                Text(
                                                    text = stringResource(R.string.action_edit),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = ArcaneGold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
