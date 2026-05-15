package org.guru.playlistmaker.ui.settings.fragment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.guru.playlistmaker.R

@Preview(showBackground = true)
@Composable
fun SettingComposeView() {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.Start

    ) {
        Text(
            style = MaterialTheme.typography.h6,
            text = stringResource(id = R.string.settings)
        )

        Row {
            Text(
                text = stringResource(id = R.string.dark_theme),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = false,
                onCheckedChange = {}
            )
        }

        addRow(R.string.share_app, R.drawable.ic_share)
        addRow(R.string.support, R.drawable.ic_support)
        addRow(R.string.user_agreement, R.drawable.ic_arrow_forward)

    }
}

@Composable
private fun addRow(stringID: Int, drawableID: Int) {
    Row {
        Text(
            text = stringResource(stringID),
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(drawableID),
            contentDescription = stringResource(stringID),
        )
    }
}