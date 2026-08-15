package io.github.motomeri.tomoriradio.core.schedule

import kotlinx.coroutines.*
import java.util.concurrent.PriorityBlockingQueue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

/**
 * 可动态修改计划时间点列表的定时计划调度器.
 *
 * @author RikkaKawaii0612
 */
class Scheduler(
    private val scope: CoroutineScope,
    private val onTick: suspend (Instant) -> Unit
) {

    private val queue = PriorityBlockingQueue<Instant>(11, Comparator.naturalOrder())

    private var schedulerJob: Job? = null

    /**
     * 启动调度器.
     */
    fun start() {
        schedulerJob?.cancel()
        schedulerJob = scope.launch {
            while (isActive) {
                // 窥视最近的时间点（不取出）
                val next = queue.peek() ?: run {
                    // 队列为空时挂起等待，避免空转
                    delay(1000.milliseconds)
                    continue
                }
                val now = Clock.System.now()
                when {
                    next <= now -> {
                        // 时间到达，取出并执行
                        val target = queue.poll()
                        if (target != null) {
                            onTick(target)
                        }
                    }
                    else -> {
                        // 等待至最近时间点
                        delay(next - now)
                    }
                }
            }
        }
    }

    /**
     * 停止调度器.
     */
    fun stop() {
        schedulerJob?.cancel()
        schedulerJob = null
    }

    /**
     * 动态添加新时间点.
     */
    fun addTimePoint(time: Instant) {
        queue.add(time)
    }

    /**
     * 动态移除时间点.
     */
    fun removeTimePoint(time: Instant): Boolean {
        return queue.remove(time)
    }

    /**
     * 获取当前计划时间点数量.
     */
    fun pendingCount(): Int = queue.size

}