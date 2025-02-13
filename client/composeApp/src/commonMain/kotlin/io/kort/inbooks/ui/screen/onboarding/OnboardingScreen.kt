package io.kort.inbooks.ui.screen.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.app.di.getViewModel
import io.kort.inbooks.common.rangeFormat
import io.kort.inbooks.ui.component.Button
import io.kort.inbooks.ui.component.ButtonDefaults
import io.kort.inbooks.ui.component.PageScope
import io.kort.inbooks.ui.pattern.AppBar
import io.kort.inbooks.ui.pattern.Empty
import io.kort.inbooks.ui.pattern.LocalBottomAppBarWindowInsets
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.NavArrowRight
import io.kort.inbooks.ui.resource.Res
import io.kort.inbooks.ui.resource.illustration_onboarding
import io.kort.inbooks.ui.resource.onboarding_button_text
import io.kort.inbooks.ui.resource.onboarding_description
import io.kort.inbooks.ui.resource.onboarding_privacy_policy
import io.kort.inbooks.ui.resource.onboarding_privacy_policy_full_text
import io.kort.inbooks.ui.resource.onboarding_terms_and_conditions
import io.kort.inbooks.ui.resource.onboarding_title
import io.kort.inbooks.ui.resource.privacy_policy_url
import io.kort.inbooks.ui.resource.terms_and_conditions_url
import io.kort.inbooks.ui.token.system.System
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PageScope.OnboardingScreen(
    navigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel<OnboardingViewModel>()
    Column(modifier.windowInsetsPadding(windowInsets.exclude(LocalBottomAppBarWindowInsets.current))) {
        AppBar(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        Empty(
            modifier = Modifier.weight(1f),
            illustration = painterResource(Res.drawable.illustration_onboarding),
            title = stringResource(Res.string.onboarding_title),
            description = stringResource(Res.string.onboarding_description),
        )
        val coroutineScope = rememberCoroutineScope()
        Footer(
            onStartClick = {
                coroutineScope.launch {
                    viewModel.markIsOnboarded()
                    navigateToDashboard()
                }
            }
        )
    }
}

@Composable
private fun Footer(
    onStartClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            text = {
                Text(text = stringResource(Res.string.onboarding_button_text))
            },
            end = {
                Icon(
                    imageVector = Icons.NavArrowRight,
                    contentDescription = null,
                )
            },
            onClick = onStartClick,
            colors = ButtonDefaults.secondaryButtonColors(),
        )
        PrivacyPolicyText()
    }
}

@Composable
private fun PrivacyPolicyText() {
    val fullText = stringResource(Res.string.onboarding_privacy_policy_full_text)
    val privacyPolicyText = stringResource(Res.string.onboarding_privacy_policy)
    val termsAndConditionsText = stringResource(Res.string.onboarding_terms_and_conditions)
    val privacyPolicyUrl = stringResource(Res.string.privacy_policy_url)
    val termsAndConditionsUrl = stringResource(Res.string.terms_and_conditions_url)

    val formatedText = remember(fullText, privacyPolicyText) {
        fullText.rangeFormat(privacyPolicyText, termsAndConditionsText)
    }
    val uriHandler = LocalUriHandler.current

    val annotated = remember(formatedText, privacyPolicyUrl, termsAndConditionsUrl, uriHandler) {
        buildAnnotatedString {
            append(formatedText.string)
            listOf(
                Triple(formatedText.ranges[0]!!, "privacy_policy", privacyPolicyUrl),
                Triple(formatedText.ranges[1]!!, "terms_and_conditions", termsAndConditionsUrl),
            ).forEach { (range, tag, url) ->
                addLink(
                    clickable = LinkAnnotation.Clickable(
                        tag = tag,
                        styles = TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline)),
                        linkInteractionListener = {
                            uriHandler.openUri(url)
                        }
                    ),
                    start = range.first,
                    end = range.last
                )
            }
        }
    }

    Text(
        text = annotated,
        color = System.colors.onSurfaceVariant,
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
    )
}