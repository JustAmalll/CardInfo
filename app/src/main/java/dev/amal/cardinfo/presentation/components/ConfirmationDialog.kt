package dev.amal.cardinfo.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import dev.amal.cardinfo.R

@Composable
fun ConfirmationDialog(
    onDialogDismiss: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDialogDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.confirm_deletion),
                fontSize = 20.sp
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.confirm_deletion_subtitle),
                fontSize = 15.sp
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDeleteClicked()
                    onDialogDismiss()
                }
            ) {
                Text(text = stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDialogDismiss) {
                Text(text = stringResource(id = R.string.no))
            }
        }
    )
}