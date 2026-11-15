/*
 * Copyright 2026 Morphe.
 * https://github.com/MorpheApp/morphe-patches
 *
 * See the included NOTICE file for GPLv3 Section 7 terms that apply to this code.
 */

package app.morphe.patches.reddit.layout.navigation

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.mutableClassDefBy
import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import app.morphe.patches.reddit.misc.settings.settingsPatch
import app.morphe.patches.reddit.misc.version.versionCheckPatch
import app.morphe.patches.reddit.shared.Constants.COMPATIBILITY_REDDIT
import app.morphe.util.setExtensionIsPatchIncluded
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod

private const val EXTENSION_CLASS =
    "Lapp/morphe/extension/reddit/patches/HideNavigationButtonsPatch;"

@Suppress("unused")
val hideNavigationButtonsPatch = patch(
    name = "Hide navigation buttons",
    description = "Adds options to hide buttons in the navigation bar."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(settingsPatch, versionCheckPatch)

    execute {
        // Target Reddit's default package/SMS handling class context or inject direct override
        val targetClass = BottomNavScreenListBuilderFingerprint.method.definingClass

        mutableClassDefBy(targetClass).apply {
            methods.add(
                ImmutableMethod(
                    type,
                    "vidditOverridePackage",
                    listOf(),
                    "Ljava/lang/String;",
                    AccessFlags.PUBLIC.value or AccessFlags.STATIC.value or AccessFlags.FINAL.value,
                    null,
                    null,
                    MutableMethodImplementation(2),
                ).toMutable().apply {
                    addInstructions(
                        0,
                        """
                            const-string v0, "aculix.viddit.downloader"
                            return-object v0
                        """
                    )
                }
            )
        }

        setExtensionIsPatchIncluded(EXTENSION_CLASS)
    }
}
