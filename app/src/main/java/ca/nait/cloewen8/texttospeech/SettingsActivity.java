package ca.nait.cloewen8.texttospeech;

import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupActionBar();
        getFragmentManager().beginTransaction()
            .add(R.id.settings_container, new GeneralPreferenceFragment(), GeneralPreferenceFragment.TAG)
            .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean performed;
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                performed = true;
                break;
            default:
                performed = super.onOptionsItemSelected(item);
        }
        return performed;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {
        public static final String TAG = "GeneralPreferenceFragment";

        public GeneralPreferenceFragment() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
        }
    }
}
