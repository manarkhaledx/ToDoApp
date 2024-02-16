import android.icu.text.SimpleDateFormat
import java.util.Calendar

object CalendarExtensions {
    fun Calendar.getDateOnly(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = this.time
        calendar.set(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE), 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time.time
    }

    fun Calendar.getTimeOnly(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = this.time
        calendar.set(0, 0, 0, get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), get(Calendar.MILLISECOND))
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time.time
    }
    fun Calendar.formateTime(): String {
        val formatter = SimpleDateFormat("hh:mm a")
        return formatter.format(time)
    }

    fun Calendar.formateDate(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(time)
    }
}
