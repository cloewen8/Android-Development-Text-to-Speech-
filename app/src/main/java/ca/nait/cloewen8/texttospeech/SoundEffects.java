package ca.nait.cloewen8.texttospeech;

import android.content.Context;
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

    private Pair<Float, Float> getVolume() {
        // todo: Set the left and right volume based on button press position.
        return new Pair<Float, Float>(0.3f, 0.3f);
    }

    protected void playButtonSound() {
        Pair<Float, Float> volume = getVolume();
        mPool.play(mButtonSoundId, volume.first, volume.second, 0, 0, 1);
    }
}
