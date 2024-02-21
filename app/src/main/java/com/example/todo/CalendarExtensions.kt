import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CalendarExtensions {
    fun Calendar.getDateOnly(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = this.time
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun Calendar.getTimeOnly(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = this.time
        calendar.set(Calendar.YEAR, 0)
        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.DATE, 0)
        return calendar.timeInMillis
    }

    fun Calendar.formatTime(): String {
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return formatter.format(time)
    }

    fun Calendar.formatDate(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(time)
    }
}
