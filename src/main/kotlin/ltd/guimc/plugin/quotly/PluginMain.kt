package ltd.guimc.plugin.quotly

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.name
import net.mamoe.mirai.console.plugin.version
import java.io.IOException
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import kotlin.concurrent.thread
import kotlin.io.path.name

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        "ltd.guimc.plugin.quotly",
        "0.0.1",
        "QuotlyBot",
    ){
        author("BakaBotTeam")
        dependsOn("xyz.cssxsh.mirai.plugin.mirai-hibernate-plugin", false)
    }
) {
    override fun onEnable() {
        logger.info("$name v$version 正在加载喵")
        CommandManager.registerCommand(QuotLyCommand)
        logger.info("$name v$version 加载好了喵")
    }

    override fun onDisable() {
        logger.info("$name v$version 卸载好了喵")
    }

}