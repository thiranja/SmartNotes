package com.example.thiranja.smartnotes;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loading the preferences from the xml file
        addPreferencesFromResource(R.xml.preferences);

    }
}
