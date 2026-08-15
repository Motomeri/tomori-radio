package io.github.motomeri.tomoriradio.core.settings.service

import io.github.vinceglb.autolaunch.AutoLaunch
import org.springframework.stereotype.Service


/**
 * 开机自启的服务.
 *
 * @author RikkaKawaii0612
 */
@Service
class AutoLaunchService(
    private val autoLaunch: AutoLaunch = AutoLaunch(appPackageName = "io.github.motomeri.tomoriradio")
) {

    /**
     * 启用开机自启.
     */
    suspend fun enableAutoLaunch() {
        autoLaunch.enable()
    }

    /**
     * 禁用开机自启. 如果已经启用开机自启, 则不会有任何反应.
     */
    suspend fun disableAutoLaunch() {
        autoLaunch.disable()
    }

    /**
     * 检查是否已启用开机自启.
     *
     * @return `true` 若已启用开机自启
     */
    suspend fun isAutoLaunchEnabled(): Boolean {
        return autoLaunch.isEnabled()
    }

}