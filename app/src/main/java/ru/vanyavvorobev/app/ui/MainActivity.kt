package ru.vanyavvorobev.app.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import ru.vanyavvorobev.app.R
import ru.vanyavvorobev.app.domain.MathToken
import ru.vanyavvorobev.app.stateHolder.MainActivityState
import ru.vanyavvorobev.app.stateHolder.MainActivityViewModel
import ru.vanyavvorobev.app.ui.theme.accent
import ru.vanyavvorobev.app.ui.theme.hint
import ru.vanyavvorobev.app.ui.theme.primary
import ru.vanyavvorobev.app.ui.theme.secondary
import ru.vanyavvorobev.app.ui.theme.white

class MainActivity : ComponentActivity() {

	private val viewModel by lazy { ViewModelProvider(this) [MainActivityViewModel::class.java] }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel.initState()
		setContent {
			Box(modifier = Modifier.background(primary)) {
				DrawMainActivity(viewModel)
			}
		}
	}
}

@Composable
internal fun DrawMainActivity(viewModel: MainActivityViewModel) {
	when(val currentState = viewModel.state.collectAsState().value) {
		is MainActivityState.Initial,
		is MainActivityState.Content		-> { RenderMainActivityContent(viewModel = viewModel, state = currentState as MainActivityState.Content) }
		else -> {  }
	}
}