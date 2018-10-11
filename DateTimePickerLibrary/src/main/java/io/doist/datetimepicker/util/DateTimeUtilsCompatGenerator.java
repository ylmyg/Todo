package io.doist.datetimepicker.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.util.Log;

import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class DateTimeUtilsCompatGenerator {
    private static final String LOG_TAG = DateTimeUtilsCompatGenerator.class.getSimpleName();

    private static Pattern sCommonCharsPattern = Pattern.compile("[^a-z0-9 ,.:{}\"'\\[\\]]", Pattern.CASE_INSENSITIVE);
    private static HashMap<String, String> sUnicodeStrings = new HashMap<>();

    private static Field sFieldLocaleData;
    private static Field sFieldNarrowAm;
    private static Field sFieldNarrowPm;
    static {
        try {
            sFieldLocaleData = DateFormatSymbols.class.getDeclaredField("localeData");
            sFieldLocaleData.setAccessible(true);
            sFieldNarrowAm = Class.forName("libcore.icu.LocaleData").getDeclaredField("narrowAm");
            sFieldNarrowAm.setAccessible(true);
            sFieldNarrowPm = Class.forName("libcore.icu.LocaleData").getDeclaredField("narrowPm");
            sFieldNarrowPm.setAccessible(true);
        } catch (Exception e) {
            Log.w(LOG_TAG, e);
        }
    }

    /**
     * Generates pattern cases with duplicates. Must be pruned afterwards.
     */
    public static void generateBestDateTimePatternCases(String skeleton) {
        HashMap<String, String> patternCases = new HashMap<>();

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            final boolean hasScriptOrExtensions = !locale.getScript().isEmpty() || !locale.getExtensionKeys().isEmpty();
            if (hasScriptOrExtensions) {
                // Make sure to add case for locale without variants. This will generate duplicates.
                addCaseForPattern(patternCases, new Locale(locale.getLanguage(), locale.getCountry()), skeleton);
            }

            addCaseForPattern(patternCases, locale, skeleton);
        }

        for (String pattern : patternCases.keySet()) {
            String cases = patternCases.get(pattern);
            // Add the return value to each set of cases.
            cases += "    return \"" + pattern + "\";";

            logLargeString(cases);
        }
    }

    private static void addCaseForPattern(Map<String, String> patternCases, Locale locale, String skeleton) {
        String pattern = convertToUnicode(DateFormat.getBestDateTimePattern(locale, skeleton));

        String cases = patternCases.get(pattern);
        if (cases == null) {
            cases = "";
        }
        cases += "case \""+locale.toString()+"\":\n";

        patternCases.put(pattern, cases);
    }

    /**
     * Generates am pm strings cases with duplicates. Must be pruned afterwards.
     */
    public static void generateBestAmPmStringsCases() {
        HashMap<String, String> amPmsCases = new HashMap<>();

        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            boolean hasScriptOrExtensions = !locale.getScript().isEmpty() || !locale.getExtensionKeys().isEmpty();
            if (hasScriptOrExtensions) {
                // Make sure to add case for locale without variants. This will generate duplicates.
                addCaseForBestAmPmStrings(amPmsCases, new Locale(locale.getLanguage(), locale.getCountry()));
            }

            addCaseForBestAmPmStrings(amPmsCases, locale);
        }

        for (String amPms : amPmsCases.keySet()) {
            String cases = amPmsCases.get(amPms);
            // Add the return value to each set of cases.
            cases += "    return " + amPms + ";";

            logLargeString(cases);
        }
    }

    private static void addCaseForBestAmPmStrings(Map<String, String> amPmsCases, Locale locale) {
        String amPms = generateBestAmPmStringsForLocale(locale);

        String cases = amPmsCases.get(amPms);
        if (cases == null) {
            cases = "";
        }
        cases += "case \""+locale.toString()+"\":\n";

        amPmsCases.put(amPms, cases);
    }

    private static String generateBestAmPmStringsForLocale(Locale locale) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
        String[] amPms = dateFormatSymbols.getAmPmStrings();
        try {
            Object localeData = sFieldLocaleData.get(dateFormatSymbols);
            amPms[0] = convertToUnicode(amPms[0].length() > 2 ? (String) sFieldNarrowAm.get(localeData) : amPms[0]);
            amPms[1] = convertToUnicode(amPms[1].length() > 2 ? (String) sFieldNarrowPm.get(localeData) : amPms[1]);
        } catch (Exception e) {
            Log.w(LOG_TAG, e);
        }
        return "new String[]{\""+amPms[0]+"\", \""+amPms[1]+"\"}";
    }



    /**
     * Log large Strings by splitting them in multiple chunks.
     */
    private static void logLargeString(String largeString) {
        int chunkCount = (largeString.length() / 4000) + 1;
        for (int i = 0; i < chunkCount; i++) {
            int max = 4000 * (i + 1);
            if (max >= largeString.length()) {
                Log.v(LOG_TAG, (i + 1) + " of " + chunkCount + ":\n" + largeString.substring(4000 * i));
            } else {
                Log.v(LOG_TAG, (i + 1) + " of " + chunkCount + ":\n" + largeString.substring(4000 * i, max));
            }
        }
    }

    /**
     * Converts odd characters to unicode.
     */
    private static String convertToUnicode(String string) {
        if (sUnicodeStrings.get(string) == null) {
            String unicodeString = string;
            Matcher matcher = sCommonCharsPattern.matcher(string);
            while (matcher.find()) {
                Character oddChar = string.charAt(matcher.start());
                // Replaces odd character by its unicode code.
                unicodeString = unicodeString.replace(oddChar.toString(), "\\u" + UnicodeFormatter.charToHex(oddChar));
            }
            sUnicodeStrings.put(string, unicodeString);
        }
        return sUnicodeStrings.get(string);
    }

    public static class UnicodeFormatter  {
        static public String byteToHex(byte b) {
            // Returns hex String representation of byte b.
            char hexDigit[] = {
                    '0', '1', '2', '3', '4', '5', '6', '7',
                    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
            };
            char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
            return new String(array);
        }

        static public String charToHex(char c) {
            // Returns hex String representation of char c.
            byte hi = (byte) (c >>> 8);
            byte lo = (byte) (c & 0xff);
            return byteToHex(hi) + byteToHex(lo);
        }
    }
}
