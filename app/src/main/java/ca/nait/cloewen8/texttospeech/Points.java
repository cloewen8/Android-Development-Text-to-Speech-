package ca.nait.cloewen8.texttospeech;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Points {
    private static final int MAX_WORDS = 5;

    private GameActivity mActivity;
    private TextView mPointsView;
    private int mPoints;
    private ArrayList<String[]> mSeq;
    private ArrayList<String[]> mFoundSeq;
    private ArrayList<String> mPicked;
    private ValueAnimator mPointsAnim;
    private Pattern mPointsRegex;
    private SharedPreferences mPrefs;

    protected void load(GameActivity activity) {
        mActivity = activity;
        mPointsView = activity.findViewById(R.id.game_points_text_view);
        mPoints = 0;
        mSeq = new ArrayList<String[]>();
        mFoundSeq = new ArrayList<String[]>();
        mPicked = new ArrayList<String>();
        mPointsAnim = null;
        mPointsRegex = Pattern.compile(activity.getString(R.string.points_regex));
        mPrefs = mActivity.getPreferences();

        for (String seq : mActivity.getResources().getStringArray(R.array.matches)) {
            mSeq.add(seq.split(" "));
        }
        setView(mPoints);
    }

    protected void addPicked(String word) {
        mPicked.add(0, word.toLowerCase());
        if (mPicked.size() == MAX_WORDS)
            mPicked.remove(mPicked.size() - 1);
        checkFound();
    }

    private void checkFound() {
        // Check if not already found and for a valid sequence.
        if (!hasMatch(mPicked, mFoundSeq)) {
            String[] seq = getMatch(mPicked, mSeq);
            if (seq != null) {
                mSeq.remove(seq);
                mFoundSeq.add(seq);
                mPicked.clear();
                addPoints(seq.length);
            }
        }
    }

    private boolean hasMatch(ArrayList<String> seq, ArrayList<String[]> toCheck) {
        return getMatch(seq, toCheck) != null;
    }

    private String[] getMatch(ArrayList<String> seq, ArrayList<String[]> toCheck) {
        String[] match;
        int wordReverseI;
        for (String[] checkSeq : toCheck) {
            if (seq.size() >= checkSeq.length) {
                match = checkSeq;
                // Do any of the words in the sequence not match.
                // Reverse the sequence to match the direction of the picked words.
                wordReverseI = match.length;
                for (int wordI = 0; match != null && wordI < match.length; wordI++) {
                    if (!match[--wordReverseI].equals(seq.get(wordI)))
                        match = null;
                }
                if (match != null)
                    return match;
            }
        }
        return null;
    }

    private void addPoints(int earned) {
        mPoints += earned*
            Integer.parseInt(mActivity.getPreferences().getString(mActivity.getString(R.string.settings_key_scoreMult),
                "100"));
        animatePoints(mPoints);
        // setView(mPoints);
    }

    private void setView(int points) {
        mPointsView.setText(mActivity.getString(R.string.points, points));
    }

    private void animatePoints(int newAmount) {
        // Cancel existing animations.
        if (mPointsAnim != null)
            mPointsAnim.cancel();

        // Start animating the points (increasing to the new amount).
        String text = mPointsView.getText().toString();
        Log.d("Points", text);
        Matcher match = mPointsRegex.matcher(text);
        if (mPrefs.getBoolean(mActivity.getString(R.string.settings_key_animate), true) &&
            match.matches()) {

            int oldAmount = Integer.parseInt(match.group(1));
            mPointsAnim = ValueAnimator.ofInt(oldAmount, newAmount);
            // Show 1 point every millisecond.
            mPointsAnim.setDuration(newAmount - oldAmount);
            // Stay linear to keep the points increase consistent.
            mPointsAnim.setInterpolator(new LinearInterpolator());
            mPointsAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setView(Integer.parseInt(animation.getAnimatedValue().toString()));
                }
            });
            mPointsAnim.start();
        } else {
            setView(mPoints);
        }
    }
}
