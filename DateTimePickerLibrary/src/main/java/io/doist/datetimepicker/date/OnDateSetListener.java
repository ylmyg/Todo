package io.doist.datetimepicker.date;

/**
 * The callback used to indicate the user is done filling in the date.
 */
public interface OnDateSetListener {

    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility with {@link java.util.Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
}