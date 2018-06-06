package com.example.studente.restapp.Database;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

public class UriConverter {

    @TypeConverter
    public Uri fromTimestamp(String value) {
        return value == null ? null : Uri.parse(value);
    }

    @TypeConverter
    public String dateToTimestamp(Uri uri) {
        if (uri == null) {
            return null;
        } else {
            return uri.toString();
        }
    }

}
