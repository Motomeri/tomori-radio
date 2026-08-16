package io.github.motomeri.tomoriradio.desktop.controller

import io.github.motomeri.tomoriradio.desktop.model.DetailedTrack
import io.github.motomeri.tomoriradio.desktop.viewmodel.TracksViewModel
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream

@Component
class TracksController(
    private val viewModel: TracksViewModel
) {

    @FXML lateinit var rootContainer: VBox

    private val itemMap = mutableMapOf<Long, HBox>()

    @FXML
    fun initialize() {
        viewModel.tracks.addListener { _: ListChangeListener.Change<out DetailedTrack> ->
            createItems()
        }
    }

    private fun createItems() {
        itemMap.clear()
        viewModel.tracks.forEach {
            val item = createItem(it)
            itemMap[it.id] = item
            rootContainer.children.add(item)
        }
    }

    private fun createItem(track: DetailedTrack): HBox {
        // 封面（圆角裁剪）
        val cover = ImageView().apply {
            fitWidth = 160.0
            fitHeight = 160.0
            isPreserveRatio = true
            image = track.cover?.let { Image(ByteArrayInputStream(it)) }
                ?: Image(javaClass.getResourceAsStream("/images/akari.png"))
            styleClass.add("song-cover")
            // 圆角裁剪
            clip = Rectangle(0.0, 0.0, 160.0, 160.0).apply {
                arcWidth = 15.0
                arcHeight = 15.0
            }
        }

        // 标题（粗体）
        val titleLabel = Label(track.title).apply {
            styleClass.add("song-title-item")
        }

        // 作者（正常）
        val artistLabel = Label(track.artist).apply {
            styleClass.add("song-artist-item")
        }

        // 排期标签（自适应宽度的圆角矩形）
        val scheduleLabel = Label("计划播放于：${track.schedule}").apply {
            styleClass.add("song-schedule")
            isWrapText = false
        }

        // 右侧信息 VBox
        val infoBox = VBox().apply {
            spacing = 8.0
            styleClass.add("song-info")
            children.addAll(titleLabel, artistLabel, scheduleLabel)
            // 垂直居中
            alignment = javafx.geometry.Pos.CENTER_LEFT
            // 占用剩余水平空间
            HBox.setHgrow(this, Priority.ALWAYS)
        }

        // 整体 HBox
        return HBox().apply {
            styleClass.add("song-item")
            spacing = 15.0
            children.addAll(cover, infoBox)
        }
    }

}