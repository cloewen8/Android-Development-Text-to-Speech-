package ca.nait.cloewen8.texttospeech;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.Locale;

public class GameActivity extends AppCompatActivity
    implements TextToSpeech.OnInitListener,
    View.OnClickListener {

    private static final String TAG = "GameActivity";
    private static final Locale LANG = Locale.CANADA;
    private static final int CHECK_TTS_REQUEST = 0;

    private GridView mWordsView;
    private TextToSpeech mTTS;
    private boolean mTTSLoaded;

    private void bindViews() {
        mWordsView = findViewById(R.id.game_words_grid_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mTTSLoaded = false;
        bindViews();

        // todo: Set the points.

        Intent checkTTS = new Intent();
        checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTS, CHECK_TTS_REQUEST);

        // todo: Build sound pool for sound effects.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTTS != null) {
            mTTS.shutdown();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHECK_TTS_REQUEST) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Log.d(TAG, "Text to Speech is available.");

                mTTS = new TextToSpeech(this, this);
            } else if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL) {
                Log.d(TAG, "Text to Speech is not available.");

                Intent installTTS = new Intent();
                installTTS.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTS);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language of the activity.
            mTTS.setLanguage(LANG);

            mTTSLoaded = true;
            // todo: Check if sound effects loaded.
            if (true) {
                finishLoading();
            }
        } else if (status == TextToSpeech.ERROR) {
            Log.e(TAG, "Unable to start Text to Speech, retrying.");

            mTTS = new TextToSpeech(this, this);
        }
    }

    @Override
    public void onClick(View v) {
        String word = ((Button) v).getText().toString();
        Log.i(TAG, "Word requested: " + word);

        // todo: Award points.

        // todo: Play button sound.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTTS.speak(word, TextToSpeech.QUEUE_FLUSH, null, word);
        } else {
            mTTS.speak(word, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    protected boolean hasLoaded() {
        return mTTSLoaded;
    }

    protected void finishLoading() {
        mWordsView.setAdapter(new ArrayAdapter<String>(this,
            R.layout.item_word,
            getResources().getStringArray(R.array.words)) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflator.inflate(R.layout.item_word, parent, false);
                Button button = view.findViewById(R.id.word_button);
                button.setText(getItem(position));
                button.setOnClickListener(GameActivity.this);
                return view;
            }
        });
    }
}
