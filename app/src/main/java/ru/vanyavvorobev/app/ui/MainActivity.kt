package ru.vanyavvorobev.app.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
		setContent {
			Box(modifier = Modifier.background(primary)) {
				MainActivityCompose(viewModel, this@MainActivity)
			}
		}
		viewModel.initState()
	}
}

@Composable
fun MainActivityCompose(viewModel: MainActivityViewModel, context: LifecycleOwner) {
	val handleToken: (token: MathToken) -> Unit = { token -> viewModel.handleToken(token) }
	var expression by remember { mutableStateOf("") }
	viewModel.state.observe(context) { state ->
		if(state is MainActivityState.Content)  {
			expression = state.expression
		}
	}

	Column(modifier = Modifier
		.fillMaxHeight()
		.fillMaxWidth()
		.padding(16.dp)) {
		Text(
			text = "Calculator",
			color = white,
			fontSize = 24.sp,
			modifier = Modifier.padding(4.dp)
		)
		Text(
			text = expression,
			color = white,
			fontSize = 36.sp,
			modifier = Modifier
				.padding(16.dp)
				.weight(1f)
		)
		Row {
			Text(
				text = "Result",
				color = white,
				fontSize = 32.sp,
				modifier = Modifier.weight(1f)
			)
			IconButton(onClick = { handleToken(MathToken.BACKSPACE) }) {
				Icon(painter = painterResource(id = R.drawable.backspace), contentDescription = "", tint = white)
			}
		}

		Divider(
			color = hint,
			thickness = 1.dp,
			modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 4.dp)
		)
		Row {
			CreateButton(onClick = { handleToken(MathToken.AC) }, text = "AC", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.PU_MINUS) }, text = "±", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.PERCENT) }, text = "%", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.DIVIDE) }, text = "/", color = secondary, weight = 1f)
		}
		Row {
			CreateButton(onClick = { handleToken(MathToken.SEVEN) }, text = "7", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.EIGHT) }, text = "8", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.NINE) }, text = "9", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.MULTIPLY) }, text = "*", color = secondary, weight = 1f)
		}
		Row {
			CreateButton(onClick = { handleToken(MathToken.FOUR) }, text = "4", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.FIVE) }, text = "5", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.SIX) }, text = "6", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.B_MINUS) }, text = "-", color = secondary, weight = 1f)
		}
		Row {
			CreateButton(onClick = { handleToken(MathToken.ONE) }, text = "1", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.TWO) }, text = "2", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.THREE) }, text = "3", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.PLUS) }, text = "+", color = secondary, weight = 1f)
		}
		Row(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)) {
			CreateButton(onClick = { handleToken(MathToken.ZERO) }, text = "0", color = accent, weight = 2f)
			CreateButton(onClick = { handleToken(MathToken.POINT) }, text = ",", color = accent, weight = 1f)
			CreateButton(onClick = { handleToken(MathToken.RESULT) }, text = "=", color = secondary, weight = 1f)
		}
	}
}

@Composable
fun RowScope.CreateButton(onClick: () -> Unit, text: String, color: Color, weight: Float){
	Button(
		onClick = onClick,
		colors = ButtonDefaults.buttonColors(backgroundColor = color),
		modifier = Modifier
			.padding(4.dp)
			.weight(weight)
			.defaultMinSize(32.dp, 64.dp)
			.clip(RoundedCornerShape(16.dp)),
		) {
		Text(text = text, fontSize = 24.sp, color = white)
	}
}