import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.event_viewer.R
import com.example.event_viewer.database.Event

class EventAdapter(private var events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = events[position]
        holder.bind(currentEvent)
    }

    override fun getItemCount() = events.size

    fun setEvents(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewEventTitle)
        private val locationTextView: TextView = itemView.findViewById(R.id.textViewEventLocation)
        private val startDateTimeTextView: TextView = itemView.findViewById(R.id.textViewEventStartDateTime)
        private val endDateTimeTextView: TextView = itemView.findViewById(R.id.textViewEventEndDateTime)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewEventDescription)

        fun bind(event: Event) {
            titleTextView.text = event.title
            locationTextView.text = event.location
            startDateTimeTextView.text = event.startDateTime
            endDateTimeTextView.text = event.endDateTime
            descriptionTextView.text = event.description
        }
    }
}
