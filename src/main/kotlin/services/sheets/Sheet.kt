package services.sheets

import com.google.api.services.sheets.v4.model.ValueRange
import services.Configuration
import services.GoogleSheets
import kotlin.reflect.KProperty

class Sheet(sheetName: String, private val range: String) {
    private val id = Configuration.sheets[sheetName] ?: error("Sheet name $sheetName not found!")
    private var frozen = false
    private var _value = GoogleSheets.service.spreadsheets().values()
        .get(id, range).execute().getValues() as List<List<String>>

    var values: List<List<String>>
    get() { return _value }
    set( value: List<List<String>> )
    {
        _value = value
        GoogleSheets.service.spreadsheets().values()
            .update(id, range, ValueRange().setValues(value))
            .setValueInputOption("RAW")
            .execute()
    }

    fun update() {
        _value = GoogleSheets.service.spreadsheets().values()
            .get(id, range).execute().getValues() as List<List<String>>
    }

    fun setFrozen(state: Boolean) {
        frozen = state
    }
}