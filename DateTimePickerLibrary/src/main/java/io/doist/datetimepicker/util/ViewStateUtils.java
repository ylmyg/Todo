package io.doist.datetimepicker.util;

import android.content.res.ColorStateList;
import android.util.SparseArray;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ViewStateUtils {
    public static int[] VIEW_STATE_EMPTY = new int[0];

    /**
     * The order matters.
     */
    public static SparseArray<int[]> VIEW_STATE_SETS_SINGLE = new SparseArray<int[]>(){{
        put(android.R.attr.state_window_focused, new int[]{android.R.attr.state_window_focused});
        put(-android.R.attr.state_window_focused, new int[]{-android.R.attr.state_window_focused});
        put(android.R.attr.state_focused, new int[]{android.R.attr.state_focused});
        put(-android.R.attr.state_focused, new int[]{-android.R.attr.state_focused});
        put(android.R.attr.state_enabled, new int[]{android.R.attr.state_enabled});
        put(-android.R.attr.state_enabled, new int[]{-android.R.attr.state_enabled});
        put(android.R.attr.state_activated, new int[]{android.R.attr.state_activated});
        put(-android.R.attr.state_activated, new int[]{-android.R.attr.state_activated});
        put(android.R.attr.state_selected, new int[]{android.R.attr.state_selected});
        put(-android.R.attr.state_selected, new int[]{-android.R.attr.state_selected});
        put(android.R.attr.state_checked, new int[]{android.R.attr.state_checked});
        put(-android.R.attr.state_checked, new int[]{-android.R.attr.state_checked});
        put(android.R.attr.state_pressed, new int[]{android.R.attr.state_pressed});
        put(-android.R.attr.state_pressed, new int[]{-android.R.attr.state_pressed});
    }};

    public static int[][] VIEW_STATE_SETS_DOUBLE;

    static {
        int size = (VIEW_STATE_SETS_SINGLE.size() * VIEW_STATE_SETS_SINGLE.size()) / 2 - VIEW_STATE_SETS_SINGLE.size();
        VIEW_STATE_SETS_DOUBLE = new int[size][2];
        int index = 0;
        for(int i = 0, count = VIEW_STATE_SETS_SINGLE.size(); i < count; i++) {
            int[] state1 = VIEW_STATE_SETS_SINGLE.valueAt(i);
            for(int j = i + 1; j < count; j++) {
                int[] state2 = VIEW_STATE_SETS_SINGLE.valueAt(j);
                if (state1[0] != -state2[0]) {
                    VIEW_STATE_SETS_DOUBLE[index++] = new int[]{state1[0], state2[0]};
                }
            }
        }
    }

    /**
     * This is a cheap replacement for {@link ColorStateList#addFirstIfMissing} (API level 22),
     * which is hidden, and will get replaced when it supports themed attributes.
     * Only supports color lists with items that have a maximum of two states.
     */
    @SuppressWarnings("JavadocReference")
    public static ColorStateList addStateIfMissing(ColorStateList colorList, int state, int color) {
        int defaultColor = colorList.getColorForState(ViewStateUtils.VIEW_STATE_EMPTY, colorList.getDefaultColor());

        int[] stateSet = VIEW_STATE_SETS_SINGLE.get(state);
        if (colorList.getColorForState(stateSet, defaultColor) != defaultColor) {
            return colorList;
        }

        Map<int[], Integer> states = new LinkedHashMap<>();
        states.put(ViewStateUtils.VIEW_STATE_EMPTY, defaultColor);

        for(int i = 0, count = VIEW_STATE_SETS_SINGLE.size(); i < count; i++) {
            int[] set = VIEW_STATE_SETS_SINGLE.valueAt(i);
            if (set == stateSet) {
                // We make sure the stateSet is placed in the right order.
                states.put(stateSet, color);
            } else {
                int setColor = colorList.getColorForState(set, defaultColor);
                if (setColor != defaultColor) {
                    states.put(set, setColor);
                }
            }
        }

        for (int[] set : ViewStateUtils.VIEW_STATE_SETS_DOUBLE) {
            int setColor = colorList.getColorForState(set, defaultColor);
            if (setColor != defaultColor) {
                for (int item : set) {
                    Integer singleSetColor = states.get(VIEW_STATE_SETS_SINGLE.get(item));
                    if (singleSetColor != null && singleSetColor == setColor) {
                        setColor = defaultColor;
                        break;
                    }
                }

                if (setColor != defaultColor) {
                    states.put(set, setColor);
                }
            }
        }

        int count = states.size();
        int[][] statesArray = new int[count][];
        int[] colorsArray = new int[count];
        Iterator<int[]> it = states.keySet().iterator();
        int i = count - 1;
        while (it.hasNext()) {
            int[] set = it.next();
            statesArray[i] = set;
            colorsArray[i] = states.get(set);
            i--;
        }

        return new ColorStateList(statesArray, colorsArray);
    }
}
