package services.configuration

import services.Configuration
import kotlin.reflect.KProperty

class StringListValue(val id: String) {
    private var _value = Configuration.json[id] as List<String>

    operator fun getValue(thisRef: Any?, property: KProperty<*>): List<String> = _value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: List<String>) {
        _value = value
        Configuration.json[id] = _value
        Configuration.save()
    }
}