package ca.nait.cloewen8.texttospeech;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.TextView;

import java.util.ArrayList;

public class Points {
    private static final int MAX_WORDS = 5;

    private Resources mRes;
    private TextView mPointsView;
    private long mPoints;
    private ArrayList<String[]> mSeq;
    private ArrayList<String[]> mFoundSeq;
    private ArrayList<String> mPicked;

    protected void load(Activity activity) {
        mRes = activity.getResources();
        mPointsView = activity.findViewById(R.id.points_text_view);
        mPoints = 0;
        mSeq = new ArrayList<String[]>();
        mFoundSeq = new ArrayList<String[]>();
        mPicked = new ArrayList<String>();

        // todo: Sort by length (longest first).
        for (String seq : mRes.getStringArray(R.array.matches)) {
            mSeq.add(seq.split(" "));
        }
        addPoints(0);
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
        // todo: Apply the points multiplier.
        mPoints += earned;
        mPointsView.setText(mRes.getString(R.string.points, mPoints));
    }
}
