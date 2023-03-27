package app.revanced.patches.youtube.layout.etc.forcebackground.patch

import app.revanced.extensions.doRecursively
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.shared.annotation.YouTubeCompatibility
import app.revanced.patches.youtube.misc.settings.resource.patch.SettingsPatch
import org.w3c.dom.Element

@Patch(false)
@Name("force-player-button-background")
@Description("Force removes the background from the video player buttons.")
@DependsOn([SettingsPatch::class])
@YouTubeCompatibility
@Version("0.0.1")
class PlayerButtonBackgroundPatch : ResourcePatch {
    private companion object {
        const val RESOURCE_FILE_PATH = "res/drawable/player_button_circle_background.xml"

        val replacements = arrayOf(
            "color"
        )
    }
    
    override fun execute(context: ResourceContext): PatchResult {
        context.xmlEditor[RESOURCE_FILE_PATH].use { editor ->
            editor.file.doRecursively { node ->
                replacements.forEach replacement@{ replacement ->
                    if (node !is Element) return@replacement

                    node.getAttributeNode("android:$replacement")?.let { attribute ->
                        attribute.textContent = "@android:color/transparent"
                    }
                }
            }
        }

        val prefs = context["res/xml/revanced_prefs.xml"]
        prefs.writeText(
            prefs.readText()
                .replace(
                    "HIDE_NEXT_BUTTON",
                    "FORCE_BUTTON_BACKGROUND"
                ).replace(
                    "HIDE_PREV_BUTTON",
                    "FORCE_BUTTON_BACKGROUND"
                ).replace(
                    "HIDE_PLAYER_BUTTON_BACKGROUND",
                    "FORCE_BUTTON_BACKGROUND"
                )
        )

        return PatchResultSuccess()
    }
}