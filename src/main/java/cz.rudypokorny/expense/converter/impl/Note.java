package cz.rudypokorny.expense.converter.impl;

/**
 * Created by Rudolf on 01/03/2018.
 */

class Note {
    private String note;

    public Note(String note) {
        this.note = note;
    }

    public boolean is(final String... values) {
        return values != null && values.length > 0 && noteHasValue(values);
    }

    @Override
    public String toString() {
        return note;
    }

    private boolean noteHasValue(String... values) {
        for (String value : values) {
            if (value != null && value.trim().equals(this.note)) {
                return true;
            }
        }
        return false;
    }
}
