package io.github.motomeri.tomoriradio.desktop.util

import javafx.beans.value.ObservableLongValue
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableLongValue
import javafx.beans.value.WritableValue
import kotlin.reflect.KProperty

operator fun <T> ObservableValue<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
    return this.value
}

operator fun ObservableLongValue.getValue(thisRef: Any?, property: KProperty<*>): Long {
    return this.value.toLong()
}

operator fun <T> WritableValue<T>.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    this.value = value
}

operator fun WritableLongValue.setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
    this.value = value
}
