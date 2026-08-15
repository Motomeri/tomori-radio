package io.github.motomeri.tomoriradio.core.settings.service

import io.github.motomeri.tomoriradio.core.settings.AppSettings
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.flow.Flow
import kotlinx.io.files.Path
import org.springframework.stereotype.Service

/**
 * 管理动态设置的服务.
 *
 * @author RikkaKawaii0612
 */
@Service
class SettingService {

    private val configPath: Path = Path("configs", "settings.json")

    private val store: KStore<AppSettings> = storeOf(
        file = configPath,
        default = AppSettings()
    )

    @Volatile
    private var cachedSettings: AppSettings = AppSettings()

    /**
     * 获取当前缓存的设置. 如果要从磁盘刷新设置, 请使用 [loadSettingsFromDisk].
     */
    fun getSettings(): AppSettings = cachedSettings

    /**
     * 在协程中从磁盘读取设置数据.
     */
    suspend fun loadSettingsFromDisk() {
        store.get()?.let { cachedSettings = it }
    }

    /**
     * 在协程中更新并保存设置.
     */
    suspend fun updateSettings(newSettings: AppSettings) {
        store.set(newSettings)
    }

    /**
     * 观察设置变化.
     */
    fun observeSettings(): Flow<AppSettings?> = store.updates

}