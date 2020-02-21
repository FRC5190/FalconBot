package services.configuration

import services.Configuration
import kotlin.reflect.KProperty

class StringMapValue(val id: String) {
    private var _value = Configuration.json[id] as Map<String, String>

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Map<String, String> = _value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Map<String, String>) {
        _value = value
        Configuration.json[id] = _value
        Configuration.save()
    }
}