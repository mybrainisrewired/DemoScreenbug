package org.codehaus.jackson.format;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.format.InputAccessor.Std;

public class DataFormatDetector {
    public static final int DEFAULT_MAX_INPUT_LOOKAHEAD = 64;
    protected final JsonFactory[] _detectors;
    protected final int _maxInputLookahead;
    protected final MatchStrength _minimalMatch;
    protected final MatchStrength _optimalMatch;

    public DataFormatDetector(Collection<JsonFactory> detectors) {
        this((JsonFactory[]) detectors.toArray(new JsonFactory[detectors.size()]));
    }

    public DataFormatDetector(JsonFactory... detectors) {
        this(detectors, MatchStrength.SOLID_MATCH, MatchStrength.WEAK_MATCH, 64);
    }

    private DataFormatDetector(JsonFactory[] detectors, MatchStrength optMatch, MatchStrength minMatch, int maxInputLookahead) {
        this._detectors = detectors;
        this._optimalMatch = optMatch;
        this._minimalMatch = minMatch;
        this._maxInputLookahead = maxInputLookahead;
    }

    private DataFormatMatcher _findFormat(Std acc) throws IOException {
        JsonFactory bestMatch = null;
        MatchStrength bestMatchStrength = null;
        JsonFactory[] arr$ = this._detectors;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            JsonFactory f = arr$[i$];
            acc.reset();
            MatchStrength strength = f.hasFormat(acc);
            if (strength != null && strength.ordinal() >= this._minimalMatch.ordinal()) {
                if (bestMatch == null || bestMatchStrength.ordinal() < strength.ordinal()) {
                    bestMatch = f;
                    bestMatchStrength = strength;
                    if (strength.ordinal() >= this._optimalMatch.ordinal()) {
                        break;
                    }
                }
            }
            i$++;
        }
        return acc.createMatcher(bestMatch, bestMatchStrength);
    }

    public DataFormatMatcher findFormat(InputStream in) throws IOException {
        return _findFormat(new Std(in, new byte[this._maxInputLookahead]));
    }

    public DataFormatMatcher findFormat(byte[] fullInputData) throws IOException {
        return _findFormat(new Std(fullInputData));
    }

    public DataFormatDetector withMaxInputLookahead(int lookaheadBytes) {
        return lookaheadBytes == this._maxInputLookahead ? this : new DataFormatDetector(this._detectors, this._optimalMatch, this._minimalMatch, lookaheadBytes);
    }

    public DataFormatDetector withMinimalMatch(MatchStrength minMatch) {
        return minMatch == this._minimalMatch ? this : new DataFormatDetector(this._detectors, this._optimalMatch, minMatch, this._maxInputLookahead);
    }

    public DataFormatDetector withOptimalMatch(MatchStrength optMatch) {
        return optMatch == this._optimalMatch ? this : new DataFormatDetector(this._detectors, optMatch, this._minimalMatch, this._maxInputLookahead);
    }
}