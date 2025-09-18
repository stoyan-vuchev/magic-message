package com.stoyanvuchev.magicmessage.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun CheckboxField(
    modifier: Modifier = Modifier,
    tickColor: Color,
    label: AnnotatedString,
    checked: Boolean,
    onCheckedChange: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                onClick = onCheckedChange,
                role = Role.Checkbox
            )
            .padding(8.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = checked,
            onCheckedChange = remember { { onCheckedChange() } },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                checkmarkColor = tickColor,
                uncheckedColor = Color.White
            )
        )

        Spacer(modifier = Modifier.width(24.dp))

        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

    }

}