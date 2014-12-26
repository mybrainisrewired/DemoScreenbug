package org.codehaus.jackson.smile;

import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;

public class SmileParserBootstrapper {
    private final boolean _bufferRecyclable;
    final IOContext _context;
    final InputStream _in;
    final byte[] _inputBuffer;
    private int _inputEnd;
    protected int _inputProcessed;
    private int _inputPtr;

    public SmileParserBootstrapper(IOContext ctxt, InputStream in) {
        this._context = ctxt;
        this._in = in;
        this._inputBuffer = ctxt.allocReadIOBuffer();
        this._inputPtr = 0;
        this._inputEnd = 0;
        this._inputProcessed = 0;
        this._bufferRecyclable = true;
    }

    public SmileParserBootstrapper(IOContext ctxt, byte[] inputBuffer, int inputStart, int inputLen) {
        this._context = ctxt;
        this._in = null;
        this._inputBuffer = inputBuffer;
        this._inputPtr = inputStart;
        this._inputEnd = inputStart + inputLen;
        this._inputProcessed = -inputStart;
        this._bufferRecyclable = false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.codehaus.jackson.format.MatchStrength hasSmileFormat(org.codehaus.jackson.format.InputAccessor r5_acc) throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.smile.SmileParserBootstrapper.hasSmileFormat(org.codehaus.jackson.format.InputAccessor):org.codehaus.jackson.format.MatchStrength");
        /*
        r3 = r5.hasMoreBytes();
        if (r3 != 0) goto L_0x0009;
    L_0x0006:
        r3 = org.codehaus.jackson.format.MatchStrength.INCONCLUSIVE;
    L_0x0008:
        return r3;
    L_0x0009:
        r0 = r5.nextByte();
        r3 = r5.hasMoreBytes();
        if (r3 != 0) goto L_0x0016;
    L_0x0013:
        r3 = org.codehaus.jackson.format.MatchStrength.INCONCLUSIVE;
        goto L_0x0008;
    L_0x0016:
        r1 = r5.nextByte();
        r3 = 58;
        if (r0 != r3) goto L_0x003c;
    L_0x001e:
        r3 = 41;
        if (r1 == r3) goto L_0x0025;
    L_0x0022:
        r3 = org.codehaus.jackson.format.MatchStrength.NO_MATCH;
        goto L_0x0008;
    L_0x0025:
        r3 = r5.hasMoreBytes();
        if (r3 != 0) goto L_0x002e;
    L_0x002b:
        r3 = org.codehaus.jackson.format.MatchStrength.INCONCLUSIVE;
        goto L_0x0008;
    L_0x002e:
        r3 = r5.nextByte();
        r4 = 10;
        if (r3 != r4) goto L_0x0039;
    L_0x0036:
        r3 = org.codehaus.jackson.format.MatchStrength.FULL_MATCH;
        goto L_0x0008;
    L_0x0039:
        r3 = org.codehaus.jackson.format.MatchStrength.NO_MATCH;
        goto L_0x0008;
    L_0x003c:
        r3 = -6;
        if (r0 != r3) goto L_0x0056;
    L_0x003f:
        r3 = 52;
        if (r1 != r3) goto L_0x0046;
    L_0x0043:
        r3 = org.codehaus.jackson.format.MatchStrength.SOLID_MATCH;
        goto L_0x0008;
    L_0x0046:
        r2 = r1 & 255;
        r3 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r2 < r3) goto L_0x0053;
    L_0x004c:
        r3 = 248; // 0xf8 float:3.48E-43 double:1.225E-321;
        if (r2 >= r3) goto L_0x0053;
    L_0x0050:
        r3 = org.codehaus.jackson.format.MatchStrength.SOLID_MATCH;
        goto L_0x0008;
    L_0x0053:
        r3 = org.codehaus.jackson.format.MatchStrength.NO_MATCH;
        goto L_0x0008;
    L_0x0056:
        r3 = -8;
        if (r0 != r3) goto L_0x0075;
    L_0x0059:
        r3 = r5.hasMoreBytes();
        if (r3 != 0) goto L_0x0062;
    L_0x005f:
        r3 = org.codehaus.jackson.format.MatchStrength.INCONCLUSIVE;
        goto L_0x0008;
    L_0x0062:
        r3 = likelySmileValue(r1);
        if (r3 != 0) goto L_0x006f;
    L_0x0068:
        r3 = 1;
        r3 = possibleSmileValue(r1, r3);
        if (r3 == 0) goto L_0x0072;
    L_0x006f:
        r3 = org.codehaus.jackson.format.MatchStrength.SOLID_MATCH;
        goto L_0x0008;
    L_0x0072:
        r3 = org.codehaus.jackson.format.MatchStrength.NO_MATCH;
        goto L_0x0008;
    L_0x0075:
        r3 = likelySmileValue(r0);
        if (r3 != 0) goto L_0x0082;
    L_0x007b:
        r3 = 0;
        r3 = possibleSmileValue(r1, r3);
        if (r3 == 0) goto L_0x0085;
    L_0x0082:
        r3 = org.codehaus.jackson.format.MatchStrength.SOLID_MATCH;
        goto L_0x0008;
    L_0x0085:
        r3 = org.codehaus.jackson.format.MatchStrength.NO_MATCH;
        goto L_0x0008;
        */
    }

    private static boolean likelySmileValue(byte b) {
        int ch = b & 255;
        if (ch < 224) {
            return ch >= 128 && ch <= 159;
        } else {
            switch (ch) {
                case -8:
                case -6:
                case SmileConstants.TOKEN_PREFIX_MISC_OTHER:
                case SmileConstants.TOKEN_MISC_LONG_TEXT_UNICODE:
                case SmileConstants.TOKEN_MISC_BINARY_7BIT:
                    return true;
                default:
                    return false;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean possibleSmileValue(byte r4_b, boolean r5_lenient) {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.smile.SmileParserBootstrapper.possibleSmileValue(byte, boolean):boolean");
        /*
        r1 = 1;
        r2 = 0;
        r0 = r4 & 255;
        r3 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r0 < r3) goto L_0x000f;
    L_0x0008:
        r3 = 224; // 0xe0 float:3.14E-43 double:1.107E-321;
        if (r0 > r3) goto L_0x000d;
    L_0x000c:
        return r1;
    L_0x000d:
        r1 = r2;
        goto L_0x000c;
    L_0x000f:
        if (r5 == 0) goto L_0x001f;
    L_0x0011:
        r3 = 64;
        if (r0 >= r3) goto L_0x000c;
    L_0x0015:
        r3 = -32;
        if (r0 <= r3) goto L_0x001f;
    L_0x0019:
        r3 = 44;
        if (r0 < r3) goto L_0x000c;
    L_0x001d:
        r1 = r2;
        goto L_0x000c;
    L_0x001f:
        r1 = r2;
        goto L_0x000c;
        */
    }

    public SmileParser constructParser(int generalParserFeatures, int smileFeatures, ObjectCodec codec, BytesToNameCanonicalizer rootByteSymbols) throws IOException, JsonParseException {
        BytesToNameCanonicalizer can = rootByteSymbols.makeChild(true, Feature.INTERN_FIELD_NAMES.enabledIn(generalParserFeatures));
        ensureLoaded(1);
        SmileParser p = new SmileParser(this._context, generalParserFeatures, smileFeatures, codec, can, this._in, this._inputBuffer, this._inputPtr, this._inputEnd, this._bufferRecyclable);
        boolean hadSig = false;
        if (this._inputPtr < this._inputEnd && this._inputBuffer[this._inputPtr] == (byte) 58) {
            hadSig = p.handleSignature(true, true);
        }
        if (hadSig || (SmileParser.Feature.REQUIRE_HEADER.getMask() & smileFeatures) == 0) {
            return p;
        }
        String msg;
        byte firstByte = this._inputPtr < this._inputEnd ? this._inputBuffer[this._inputPtr] : (byte) 0;
        if (firstByte == (byte) 123 || firstByte == (byte) 91) {
            msg = "Input does not start with Smile format header (first byte = 0x" + Integer.toHexString(firstByte & 255) + ") -- rather, it starts with '" + ((char) firstByte) + "' (plain JSON input?) -- can not parse";
        } else {
            msg = "Input does not start with Smile format header (first byte = 0x" + Integer.toHexString(firstByte & 255) + ") and parser has REQUIRE_HEADER enabled: can not parse";
        }
        throw new JsonParseException(msg, JsonLocation.NA);
    }

    protected boolean ensureLoaded(int minimum) throws IOException {
        if (this._in == null) {
            return false;
        }
        int gotten = this._inputEnd - this._inputPtr;
        while (gotten < minimum) {
            int count = this._in.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
            if (count < 1) {
                return false;
            }
            this._inputEnd += count;
            gotten += count;
        }
        return true;
    }
}