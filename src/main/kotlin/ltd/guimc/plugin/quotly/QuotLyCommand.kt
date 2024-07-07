package ltd.guimc.plugin.quotly

import cn.hutool.http.HttpUtil
import kotlinx.coroutines.launch
import ltd.guimc.lgzbot.utils.CooldownUtils
import ltd.guimc.lgzbot.utils.OverflowUtils
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.util.cast
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import org.json.JSONObject
import top.mrxiaom.overflow.OverflowAPI
import xyz.cssxsh.mirai.hibernate.MiraiHibernateRecorder


object QuotLyCommand: SimpleCommand(
    owner = PluginMain,
    primaryName = "quotly",
    description = "QuotLy"
) {
    /* example post value
        {
          "type": "quote",
          "format": "png",
          "backgroundColor": "#1b1429",
          "width": 512,
          "height": 768,
          "scale": 2,
          "messages": [
            {
              "entities": [],
              "media": {
                "url": "https://via.placeholder.com/1000"
              },
              "avatar": true,
              "from": {
                "id": 1,
                "name": "Mike",
                "photo": {
                  "url": "https://via.placeholder.com/100"
                }
              },
              "text": "Hey",
              "replyMessage": {}
            }
          ]
        }
     */
    val cooldown = CooldownUtils(10000)

    @Handler
    fun CommandSenderOnMessage<GroupMessageEvent>.onHandler() = launch {
        if (!cooldown.isTimePassed(user!!)) {
            if (cooldown.shouldSendCooldownNotice(user!!)) sendMessage("你可以在 ${cooldown.getLeftTime(user!!) / 1000} 秒后继续使用该指令")
            return@launch
        }

        val quote = fromEvent.message.findIsInstance<QuoteReply>() ?: return@launch
        var textMessage = quote.source.originalMessage.contentToString()

        var member = subject.cast<Group>().getOrFail(quote.source.fromId)

        val postValue = JSONObject(
            "        {\n" +
            "          \"type\": \"quote\",\n" +
            "          \"format\": \"png\",\n" +
            "          \"backgroundColor\": \"#1b1429\",\n" +
            "          \"width\": 512,\n" +
            "          \"height\": 768,\n" +
            "          \"scale\": 2,\n" +
            "          \"messages\": [\n" +
            "            {\n" +
            "              \"avatar\": true,\n" +
            "              \"from\": {\n" +
            "                \"id\": 1,\n" +
            "                \"name\": \"${member.nameCardOrNick}\",\n" +
            "                \"photo\": {\n" +
            "                  \"url\": \"${member.avatarUrl}\"\n" +
            "                }\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }")

        cooldown.flag(user!!)
        postValue.getJSONArray("messages").getJSONObject(0).put("text", textMessage)
        try {
            val rawResp = JSONObject(HttpUtil.post("http://127.0.0.1:3000/generate", postValue.toString()))
            val resp = rawResp.getJSONObject("result").getString("image")
            if (!OverflowUtils.checkOverflowCore()) {
                sendMessage(ImageUtils.base642imageMessage(resp, bot!!, subject!!))
            } else {
                sendMessage(OverflowAPI.get().imageFromFile("base64://"+resp))
            }
        } catch (e: Throwable) {
            sendMessage("Oops, something went wrong.")
            cooldown.addLeftTime(user!!, -10000L)
            e.printStackTrace()
        }
    }
}
