package ltd.guimc.plugin.quotly

import cn.hutool.core.codec.Base64
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.isUploaded
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

object ImageUtils {
    suspend fun base642imageMessage(base64: String, bot: Bot, subject: Contact): Image {
        val inputstream = Base64.decode(base64)
        if (inputstream != null) {
            val image = subject.uploadImage(inputstream.toExternalResource())
            if (image.isUploaded(bot)) {
                return image
            }
        }
        throw Exception("图片上传失败")
    }
}
