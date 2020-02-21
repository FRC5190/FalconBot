package services.configuration

import services.Configuration
import kotlin.reflect.KProperty

class StringValue(val id: String) {
    private var _value = Configuration.json[id] as String

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = _value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        _value = value
        Configuration.json[id] = _value
        Configuration.save()
    }
}