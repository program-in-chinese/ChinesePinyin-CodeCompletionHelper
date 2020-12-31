package com.github.tuchg.nonasciicodecompletionhelper.extensions

import com.github.tuchg.nonasciicodecompletionhelper.config.Settings
import com.github.tuchg.nonasciicodecompletionhelper.utils.createNotification
import com.github.tuchg.nonasciicodecompletionhelper.utils.showFullNotification
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.ide.plugins.PluginManagerCore.isPluginInstalled
import com.intellij.ide.startup.StartupActionScriptManager
import com.intellij.ide.startup.StartupActionScriptManager.DeleteCommand
import com.intellij.notification.NotificationListener.UrlOpeningListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

/**
 * @author: github.com/izhangzhihao ，tuchg
 * @date: 2020/11/20 13:42
 * @description:
 */
class UpdateNotifyActivity : StartupActivity {

    override fun runActivity(project: Project) {
//        removeIfInstalled()
        val settings = Settings.instance
        if (getPlugin()?.version != settings.version) {
            settings.version = getPlugin()!!.version
            showUpdate(project)
        }
    }

    private fun removeIfInstalled() {
        val pluginId = PluginId.getId(pluginId)
        val isInstalled = isPluginInstalled(pluginId)
        if (isInstalled) {
            val pluginDescriptor = PluginManagerCore.getPlugin(pluginId)
            if (pluginDescriptor != null) {
                StartupActionScriptManager.addActionCommand(DeleteCommand(pluginDescriptor.path))
            }
        }
    }

    companion object {
        private const val pluginId = "com.github.tuchg.nonasciicodecompletionhelper"


        private val updateContent: String by lazy {
            //language=HTML
            """
    <br/>
    欢迎使用Intellij拼音补全助手，这是一款让IDE支持母语表达的扩展，一种值得考虑的折中解决方案
    <hr>
    一些特殊领域业务上不太适合用英语表达的东西，拒绝强上蹩脚英语甚至是模糊而歧义的拼音，一起来享受母语和英语环境相融合的自由编程！
    <br>
    <br>
    <p>如果你觉得本插件还算有那么点用或者有点有趣，请务必给我一颗<b><a href="https://github.com/tuchg/ChinesePinyin-CodeCompletionHelper">Star</a></b>
    <br>🎉 来自一个截止2020.11.20 15:34，<b>27k的下载</b>，<a href="https://github.com/tuchg/ChinesePinyin-CodeCompletionHelper">Github</a>加上<a href="https://gitee.com/tuchg/ChinesePinyin-CodeCompletionHelper">Gitee</a>的Star数都<b>还没破百</b>的小破插件作者</p>
    <hr>
    🐛 遇到bug或者你有什么好想法,请速来 <b><a href="https://github.com/tuchg/ChinesePinyin-CodeCompletionHelper/issues">issue,pr</a>.</b><br>
    🆕 查看本次<b><a href="${changelog()}">更新详情</a></b> 。
    <hr>
    <p>Development Powered By <a href="https://www.jetbrains.com/?from=ChinesePinyinCodeCompletionHelper">JetBrains</a>.</p>
    <br>
    """
        }

        private fun changelog(): String {
            val plugin = getPlugin()
            return if (plugin == null) {
                """https://github.com/tuchg/ChinesePinyin-CodeCompletionHelper/releases"""
            } else {
                """https://github.com/tuchg/ChinesePinyin-CodeCompletionHelper/releases/tag/v${plugin.version}"""
            }

        }

        fun getPlugin(): IdeaPluginDescriptor? = PluginManagerCore.getPlugin(PluginId.getId(pluginId))

        private fun updateMsg(): String {
            val plugin = getPlugin()
            return if (plugin == null) {
                "ChinesePinyin-CodeCompletionHelper 已安装。"
            } else {
                "ChinesePinyin-CodeCompletionHelper 已更新至 ${plugin.version}"
            }
        }

        private fun showUpdate(project: Project) {
            val notification = createNotification(
                updateMsg(),
                updateContent,
                pluginId,
                NotificationType.INFORMATION,
                UrlOpeningListener(false)
            )
            showFullNotification(project, notification)
        }
    }
}