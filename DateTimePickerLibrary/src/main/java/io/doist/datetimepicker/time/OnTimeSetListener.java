package io.doist.datetimepicker.time;

/**
 * The callback interface used to indicate the user is done filling in
 * the time (they clicked on the 'Done' button).
 */
public interface OnTimeSetListener {

    /**
     * @param view      The view associated with this listener.
     * @param hourOfDay The hour that was set.
     * @param minute    The minute that was set.
     */
    void onTimeSet(TimePicker view, int hourOfDay, int minute);
}