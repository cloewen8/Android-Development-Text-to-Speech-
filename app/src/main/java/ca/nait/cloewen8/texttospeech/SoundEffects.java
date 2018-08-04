package ca.nait.cloewen8.texttospeech;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Pair;

public class SoundEffects implements SoundPool.OnLoadCompleteListener {
    private GameActivity mActivity;
    private SoundPool mPool;
    private int mButtonSoundId;
    private boolean mPoolLoaded = false;

    protected void loadSounds(GameActivity activity) {
        mActivity = activity;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        } else {
            mPool = new SoundPool.Builder().build();
        }
        mButtonSoundId = mPool.load(mActivity, R.raw.button_pressed, 1);
        mPool.setOnLoadCompleteListener(this);
    }

    protected void unloadSounds() {
        mPool.release();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        mPoolLoaded = true;
        if (mActivity.hasLoaded()) {
            mActivity.finishLoading();
        }
    }

    protected boolean hasLoaded() {
        return mPoolLoaded;
    }

    private Pair<Float, Float> getVolume(float direction) {
        Pair<Float, Float> volume;
        SharedPreferences prefs = mActivity.getPreferences();
        if (prefs.getBoolean(mActivity.getString(R.string.settings_key_stereoMono), true)) {
            // Get the amount on either side of the direction ([0..1]).
            // 0 being left, 1 being right.
            float preferred = prefs.getFloat(
                mActivity.getString(R.string.settings_key_direction),
                0.5f);
            volume = new Pair<Float, Float>((1f - preferred)*(1f - direction)*0.3f, preferred*direction*0.3f);
        } else {
            volume = new Pair<Float, Float>(0.3f, 0.3f);
        }
        return volume;
    }

    protected void playButtonSound(float direction) {
        if (mActivity.getPreferences().getBoolean(
            mActivity.getString(R.string.settings_key_effectsEnabled),
            true)) {

            Pair<Float, Float> volume = getVolume(direction);
            mPool.play(mButtonSoundId, volume.first, volume.second, 0, 0, 1);
        }
    }
}
