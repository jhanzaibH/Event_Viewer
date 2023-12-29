package com.example.event_viewer

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.event_viewer.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEventActivity : AppCompatActivity() {

    private lateinit var eventNameEditText: EditText
    private lateinit var eventDescriptionEditText: EditText
    private lateinit var eventLocationSpinner: Spinner
    private lateinit var eventStartDateTextView: TextView
    private lateinit var eventEndDateTextView: TextView
    private lateinit var ageRangeContainer: LinearLayout

    private lateinit var eventDao: EventDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_event)

        findInputViews()

        val locations = arrayOf("Göteborg", "Malmö", "Kamlar", "Luleå", "Stockholm")
        setupLocationSpinner(locations)

        val ageRanges = arrayOf("Khuddam", "Aftal", "Lajna", "Ansar")
        addAgeRangeCheckboxes(ageRanges)

        val startDateButton = findViewById<Button>(R.id.btn_start_date)
        val endDateButton = findViewById<Button>(R.id.btn_end_date)

        val startDateTextView = findViewById<TextView>(R.id.start_date)
        val endDateTextView = findViewById<TextView>(R.id.end_date)

        chooseDateTime(startDateButton, startDateTextView)
        chooseDateTime(endDateButton, endDateTextView)

        // Find the button by its ID
        val addButton = findViewById<Button>(R.id.btn_add_event)
        initEventDao()
        // Set an OnClickListener
        addButton.setOnClickListener {
            // Define what happens when the button is clicked
            addEvent()
        }
    }

    private fun addAgeRangeCheckboxes(ageRanges: Array<String>) {
        ageRanges.forEach { ageRange ->
            val checkBox = CheckBox(this)
            checkBox.text = ageRange
            checkBox.id = View.generateViewId()
            ageRangeContainer.addView(checkBox)
        }
    }

    private fun initEventDao(){
        val database = DatabaseBuilder.getDatabase(applicationContext)
        eventDao = database.eventDao()
    }

    private fun chooseDateTime(button: Button, textView: TextView){
        button.setOnClickListener {
            showDatePicker { year, month, day ->
                // Handle the chosen date
                // Then show the TimePicker
                showTimePicker { hour, minute ->
                    // Handle the chosen time
                    val formattedDateTime = formatDateTime(year, month, day, hour, minute)
                    // Set the formatted date and time to the TextView
                    textView.text = formattedDateTime
                }
            }
        }
    }

    private fun formatDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return format.format(calendar.time)
    }

    private fun setupLocationSpinner(locations:Array<String>){
        val locationSpinner = findViewById<Spinner>(R.id.location_spinner)

        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, locations)
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationSpinner.adapter = locationAdapter
    }

    /* fun setupAgeRangeSpinner(ageRanges:Array<String>){
        val ageRangeSpinner = findViewById<Spinner>(R.id.ageRangespinner)

        val ageRangeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ageRanges)
        ageRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ageRangeSpinner.adapter = ageRangeAdapter
    }*/

    private fun showDatePicker(onDateSet: (year: Int, month: Int, day: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            onDateSet(year, month, dayOfMonth)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePicker(onTimeSet: (hour: Int, minute: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(this, { _, hourOfDay, minute ->
            onTimeSet(hourOfDay, minute)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    private fun findInputViews(){
        eventNameEditText = findViewById(R.id.title_input)
        eventDescriptionEditText = findViewById(R.id.description_input)
        eventLocationSpinner = findViewById(R.id.location_spinner)
        eventStartDateTextView= findViewById(R.id.start_date)
        eventEndDateTextView= findViewById(R.id.end_date)
        ageRangeContainer = findViewById(R.id.ageRangeContainer)
//        eventAgeRangeSpinner= findViewById(R.id.ageRangespinner)
    }

    private fun addEvent(){
        val title = eventNameEditText.text.toString()
        val description = eventDescriptionEditText.text.toString()
        val location = eventLocationSpinner.selectedItem.toString()
        val startDate = eventStartDateTextView.text.toString()
        val endDate = eventEndDateTextView.text.toString()
        //val ageRange = eventAgeRangeSpinner.selectedItem.toString()


        val selectedAgeRanges = mutableListOf<String>()
        for (i in 0 until ageRangeContainer.childCount) {
            val checkBox = ageRangeContainer.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
                selectedAgeRanges.add(checkBox.text.toString())
            }
        }

        val ageRangeString = selectedAgeRanges.joinToString(", ")

        val newEvent = Event(title = title, description = description, location = location,
            startDateTime = startDate, endDateTime = endDate, ageRange = ageRangeString)

        CoroutineScope(Dispatchers.IO).launch {
            eventDao.insertEvent(newEvent)

            // Fetch and log all events
            val allEvents = eventDao.getAllEvents()
            allEvents.forEach {
                Log.d("AddEventActivity", "Event: $it")
            }

            // Finish the activity
            finish()
        }
    }
}
