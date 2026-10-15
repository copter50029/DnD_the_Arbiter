package com.example.final_project.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.final_project.ui.theme.ArcaneGold
import com.example.final_project.ui.theme.ArcaneGoldBright
import com.example.final_project.ui.theme.ArcaneGoldDark
import com.example.final_project.ui.theme.DarkLeatherBorder
import com.example.final_project.ui.theme.DarkLeatherCard
import com.example.final_project.ui.theme.DarkLeatherDeep
import com.example.final_project.ui.theme.DeepCrimson

/**
 * Ancient Grimoire Card with medieval gold border and dark leather background.
 */
@Composable
fun GrimoireCard(
    modifier: Modifier = Modifier,
    borderColor: Color = ArcaneGoldDark.copy(alpha = 0.5f),
    containerColor: Color = DarkLeatherCard,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        content = content
    )
}

/**
 * Primary Grimoire Action Button with ancient gold styling.
 */
@Composable
fun GrimoireButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    isCrimson: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isCrimson) DeepCrimson else ArcaneGold,
            contentColor = if (isCrimson) ArcaneGoldBright else DarkLeatherDeep,
            disabledContainerColor = DarkLeatherBorder,
            disabledContentColor = Color.Gray
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = if (isCrimson) ArcaneGoldBright else DarkLeatherDeep,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "...",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
        } else {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

/**
 * Secondary outlined button with gold rune border.
 */
@Composable
fun GrimoireOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(46.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, ArcaneGold),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = ArcaneGoldBright
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = ArcaneGoldBright
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

/**
 * Stat Chip (e.g. CR, AC, HP, Party Level).
 */
@Composable
fun RuneBadge(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    accentColor: Color = ArcaneGold
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = accentColor.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.labelSmall,
                color = ArcaneGoldBright,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Stylized Outlined Text Field with ancient grimoire styling.
 */
@Composable
fun GrimoireTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    minLines: Int = 1,
    maxLines: Int = if (minLines > 1) 10 else 1,
    singleLine: Boolean = minLines == 1,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                fontFamily = FontFamily.Serif,
                fontSize = 13.sp
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                color = Color.Gray,
                fontSize = 13.sp
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        minLines = minLines,
        maxLines = maxLines,
        singleLine = singleLine,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ArcaneGold,
            unfocusedBorderColor = DarkLeatherBorder,
            focusedLabelColor = ArcaneGoldBright,
            unfocusedLabelColor = ArcaneGoldDark,
            cursorColor = ArcaneGold,
            focusedContainerColor = DarkLeatherDeep.copy(alpha = 0.6f),
            unfocusedContainerColor = DarkLeatherDeep.copy(alpha = 0.4f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}
