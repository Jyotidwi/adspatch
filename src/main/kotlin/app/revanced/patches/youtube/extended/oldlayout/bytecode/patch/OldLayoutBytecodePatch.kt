package app.revanced.patches.youtube.extended.oldlayout.bytecode.patch

import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patches.youtube.extended.oldlayout.bytecode.fingerprints.OldLayoutFingerprint
import app.revanced.shared.annotation.YouTubeCompatibility
import app.revanced.shared.util.integrations.Constants.EXTENDED_PATH
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction

@Name("enable-old-layout-bytecode-patch")
@YouTubeCompatibility
@Version("0.0.1")
class OldLayoutBytecodePatch : BytecodePatch(
    listOf(
        OldLayoutFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {

        val result = OldLayoutFingerprint.result!!
        val method = result.mutableMethod
        val index = result.scanResult.patternScanResult!!.startIndex
        val register = (method.implementation!!.instructions[index] as OneRegisterInstruction).registerA

        method.addInstructions(
            index + 1, """
            invoke-static {v$register}, $EXTENDED_PATH/VersionOverridePatch;->getVersionOverride(Ljava/lang/String;)Ljava/lang/String;
            move-result-object v$register
        """
        )

        return PatchResultSuccess()
    }
}