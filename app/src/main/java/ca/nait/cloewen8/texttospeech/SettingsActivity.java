package ca.nait.cloewen8.texttospeech;

import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;
import android.preference.PreferenceFragment;
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
        startActivity(new Intent(this, GameActivity.class));
        return true;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
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
