package io.kort.inbooks.ui.screen.dashboard.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.kort.inbooks.ui.component.Button
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.foundation.calculateEndPadding
import io.kort.inbooks.ui.foundation.calculateStartPadding
import io.kort.inbooks.ui.pattern.Empty
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.dashboard_onboarding_sign_up_description
import io.kort.inbooks.ui.resource.dashboard_onboarding_sign_up_title
import io.kort.inbooks.ui.resource.illustration_dashboard_onboarding_sign_up
import io.kort.inbooks.ui.resource.sign_up_a_new_account
import io.kort.inbooks.ui.token.system.System
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpOnboardingSheet(
    onDismissRequest: () -> Unit,
    navigateToSignUp: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = System.colors.background,
        contentColor = System.colors.onBackground,
        dragHandle = { BottomSheetDefaults.DragHandle(color = System.colors.onBackgroundVariant) },
    ) {
        Column(
            Modifier.padding(
                start = System.spacing.pagePadding.calculateStartPadding(),
                end = System.spacing.pagePadding.calculateEndPadding(),
                bottom = System.spacing.pagePadding.calculateBottomPadding(),
            )
        ) {
            Empty(
                title = stringResource(Res.string.dashboard_onboarding_sign_up_title),
                description = stringResource(Res.string.dashboard_onboarding_sign_up_description),
                illustration = painterResource(Res.drawable.illustration_dashboard_onboarding_sign_up),
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = navigateToSignUp,
                colors = ButtonDefaults.secondaryButtonColors(),
            ) {
                Text(stringResource(Res.string.sign_up_a_new_account))
            }
        }
    }
}