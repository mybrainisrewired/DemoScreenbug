package org.codehaus.jackson.impl;

import com.wmt.data.LocalAudioAll;
import java.io.ByteArrayInputStream;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.format.InputAccessor;
import org.codehaus.jackson.format.MatchStrength;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.MergedStream;
import org.codehaus.jackson.io.UTF32Reader;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;

public final class ByteSourceBootstrapper {
    static final byte UTF8_BOM_1 = (byte) -17;
    static final byte UTF8_BOM_2 = (byte) -69;
    static final byte UTF8_BOM_3 = (byte) -65;
    protected boolean _bigEndian;
    private final boolean _bufferRecyclable;
    protected int _bytesPerChar;
    final IOContext _context;
    final InputStream _in;
    final byte[] _inputBuffer;
    private int _inputEnd;
    protected int _inputProcessed;
    private int _inputPtr;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonEncoding;

        static {
            $SwitchMap$org$codehaus$jackson$JsonEncoding = new int[JsonEncoding.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonEncoding[JsonEncoding.UTF32_BE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonEncoding[JsonEncoding.UTF32_LE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonEncoding[JsonEncoding.UTF16_BE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonEncoding[JsonEncoding.UTF16_LE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$org$codehaus$jackson$JsonEncoding[JsonEncoding.UTF8.ordinal()] = 5;
        }
    }

    public ByteSourceBootstrapper(IOContext ctxt, InputStream in) {
        this._bigEndian = true;
        this._bytesPerChar = 0;
        this._context = ctxt;
        this._in = in;
        this._inputBuffer = ctxt.allocReadIOBuffer();
        this._inputPtr = 0;
        this._inputEnd = 0;
        this._inputProcessed = 0;
        this._bufferRecyclable = true;
    }

    public ByteSourceBootstrapper(IOContext ctxt, byte[] inputBuffer, int inputStart, int inputLen) {
        this._bigEndian = true;
        this._bytesPerChar = 0;
        this._context = ctxt;
        this._in = null;
        this._inputBuffer = inputBuffer;
        this._inputPtr = inputStart;
        this._inputEnd = inputStart + inputLen;
        this._inputProcessed = -inputStart;
        this._bufferRecyclable = false;
    }

    private boolean checkUTF16(int i16) {
        if ((65280 & i16) == 0) {
            this._bigEndian = true;
        } else if ((i16 & 255) != 0) {
            return false;
        } else {
            this._bigEndian = false;
        }
        this._bytesPerChar = 2;
        return true;
    }

    private boolean checkUTF32(int quad) throws IOException {
        if ((quad >> 8) == 0) {
            this._bigEndian = true;
        } else if ((16777215 & quad) == 0) {
            this._bigEndian = false;
        } else if ((-16711681 & quad) == 0) {
            reportWeirdUCS4("3412");
        } else if ((-65281 & quad) != 0) {
            return false;
        } else {
            reportWeirdUCS4("2143");
        }
        this._bytesPerChar = 4;
        return true;
    }

    private boolean handleBOM(int quad) throws IOException {
        int msw;
        switch (quad) {
            case -16842752:
                reportWeirdUCS4("3412");
                msw = quad >>> 16;
                if (msw != 65279) {
                    this._inputPtr += 2;
                    this._bytesPerChar = 2;
                    this._bigEndian = true;
                    return true;
                } else if (msw != 65534) {
                    this._inputPtr += 2;
                    this._bytesPerChar = 2;
                    this._bigEndian = false;
                    return true;
                } else if ((quad >>> 8) == 15711167) {
                    return false;
                } else {
                    this._inputPtr += 3;
                    this._bytesPerChar = 1;
                    this._bigEndian = true;
                    return true;
                }
            case -131072:
                this._inputPtr += 4;
                this._bytesPerChar = 4;
                this._bigEndian = false;
                return true;
            case 65279:
                this._bigEndian = true;
                this._inputPtr += 4;
                this._bytesPerChar = 4;
                return true;
            case 65534:
                reportWeirdUCS4("2143");
                reportWeirdUCS4("3412");
                msw = quad >>> 16;
                if (msw != 65279) {
                    this._inputPtr += 2;
                    this._bytesPerChar = 2;
                    this._bigEndian = true;
                    return true;
                } else if (msw != 65534) {
                    this._inputPtr += 2;
                    this._bytesPerChar = 2;
                    this._bigEndian = false;
                    return true;
                } else if ((quad >>> 8) == 15711167) {
                    return false;
                } else {
                    this._inputPtr += 3;
                    this._bytesPerChar = 1;
                    this._bigEndian = true;
                    return true;
                }
            default:
                msw = quad >>> 16;
                if (msw != 65279) {
                    this._inputPtr += 2;
                    this._bytesPerChar = 2;
                    this._bigEndian = true;
                    return true;
                } else if (msw != 65534) {
                    this._inputPtr += 2;
                    this._bytesPerChar = 2;
                    this._bigEndian = false;
                    return true;
                } else if ((quad >>> 8) == 15711167) {
                    return false;
                } else {
                    this._inputPtr += 3;
                    this._bytesPerChar = 1;
                    this._bigEndian = true;
                    return true;
                }
        }
    }

    public static MatchStrength hasJSONFormat(InputAccessor acc) throws IOException {
        if (!acc.hasMoreBytes()) {
            return MatchStrength.INCONCLUSIVE;
        }
        byte b = acc.nextByte();
        if (b == (byte) -17 && !acc.hasMoreBytes()) {
            return MatchStrength.INCONCLUSIVE;
        }
        if (acc.nextByte() != (byte) -69) {
            return MatchStrength.NO_MATCH;
        }
        if (!acc.hasMoreBytes()) {
            return MatchStrength.INCONCLUSIVE;
        }
        if (acc.nextByte() != (byte) -65) {
            return MatchStrength.NO_MATCH;
        }
        if (!acc.hasMoreBytes()) {
            return MatchStrength.INCONCLUSIVE;
        }
        b = acc.nextByte();
        int ch = skipSpace(acc, b);
        if (ch < 0) {
            return MatchStrength.INCONCLUSIVE;
        }
        if (ch == 123) {
            ch = skipSpace(acc);
            if (ch < 0) {
                return MatchStrength.INCONCLUSIVE;
            }
            return (ch == 34 || ch == 125) ? MatchStrength.SOLID_MATCH : MatchStrength.NO_MATCH;
        } else if (ch == 91) {
            ch = skipSpace(acc);
            if (ch < 0) {
                return MatchStrength.INCONCLUSIVE;
            }
            return (ch == 93 || ch == 91) ? MatchStrength.SOLID_MATCH : MatchStrength.SOLID_MATCH;
        } else {
            MatchStrength strength = MatchStrength.WEAK_MATCH;
            if (ch == 34) {
                return strength;
            }
            if (ch <= 57 && ch >= 48) {
                return strength;
            }
            if (ch == 45) {
                ch = skipSpace(acc);
                if (ch < 0) {
                    return MatchStrength.INCONCLUSIVE;
                }
                return (ch > 57 || ch < 48) ? MatchStrength.NO_MATCH : strength;
            } else if (ch == 110) {
                return tryMatch(acc, "ull", strength);
            } else {
                if (ch == 116) {
                    return tryMatch(acc, "rue", strength);
                }
                return ch == 102 ? tryMatch(acc, "alse", strength) : MatchStrength.NO_MATCH;
            }
        }
    }

    private void reportWeirdUCS4(String type) throws IOException {
        throw new CharConversionException("Unsupported UCS-4 endianness (" + type + ") detected");
    }

    private static final int skipSpace(InputAccessor acc) throws IOException {
        return !acc.hasMoreBytes() ? -1 : skipSpace(acc, acc.nextByte());
    }

    private static final int skipSpace(InputAccessor acc, byte b) throws IOException {
        while (true) {
            int ch = b & 255;
            if (ch != 32 && ch != 13 && ch != 10 && ch != 9) {
                return ch;
            }
            if (!acc.hasMoreBytes()) {
                return -1;
            }
            int nextByte = acc.nextByte() & 255;
        }
    }

    private static final MatchStrength tryMatch(InputAccessor acc, String matchStr, MatchStrength fullMatchStrength) throws IOException {
        int i = 0;
        int len = matchStr.length();
        while (i < len) {
            if (!acc.hasMoreBytes()) {
                return MatchStrength.INCONCLUSIVE;
            }
            if (acc.nextByte() != matchStr.charAt(i)) {
                return MatchStrength.NO_MATCH;
            }
            i++;
        }
        return fullMatchStrength;
    }

    public JsonParser constructParser(int features, ObjectCodec codec, BytesToNameCanonicalizer rootByteSymbols, CharsToNameCanonicalizer rootCharSymbols) throws IOException, JsonParseException {
        JsonEncoding enc = detectEncoding();
        boolean canonicalize = Feature.CANONICALIZE_FIELD_NAMES.enabledIn(features);
        boolean intern = Feature.INTERN_FIELD_NAMES.enabledIn(features);
        if (enc == JsonEncoding.UTF8 && canonicalize) {
            return new Utf8StreamParser(this._context, features, this._in, codec, rootByteSymbols.makeChild(canonicalize, intern), this._inputBuffer, this._inputPtr, this._inputEnd, this._bufferRecyclable);
        } else {
            return new ReaderBasedParser(this._context, features, constructReader(), codec, rootCharSymbols.makeChild(canonicalize, intern));
        }
    }

    public Reader constructReader() throws IOException {
        JsonEncoding enc = this._context.getEncoding();
        switch (AnonymousClass_1.$SwitchMap$org$codehaus$jackson$JsonEncoding[enc.ordinal()]) {
            case LocalAudioAll.SORT_BY_DATE:
            case ClassWriter.COMPUTE_FRAMES:
                return new UTF32Reader(this._context, this._in, this._inputBuffer, this._inputPtr, this._inputEnd, this._context.getEncoding().isBigEndian());
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
            case JsonWriteContext.STATUS_EXPECT_VALUE:
            case JsonWriteContext.STATUS_EXPECT_NAME:
                InputStream in = this._in;
                InputStream in2 = in == null ? new ByteArrayInputStream(this._inputBuffer, this._inputPtr, this._inputEnd) : this._inputPtr < this._inputEnd ? new MergedStream(this._context, in, this._inputBuffer, this._inputPtr, this._inputEnd) : in;
                return new InputStreamReader(in2, enc.getJavaName());
            default:
                throw new RuntimeException("Internal error");
        }
    }

    public JsonEncoding detectEncoding() throws IOException, JsonParseException {
        JsonEncoding enc;
        boolean foundEncoding = false;
        if (ensureLoaded(JsonWriteContext.STATUS_EXPECT_VALUE)) {
            int quad = (((this._inputBuffer[this._inputPtr] << 24) | ((this._inputBuffer[this._inputPtr + 1] & 255) << 16)) | ((this._inputBuffer[this._inputPtr + 2] & 255) << 8)) | (this._inputBuffer[this._inputPtr + 3] & 255);
            if (handleBOM(quad)) {
                foundEncoding = true;
            } else if (checkUTF32(quad)) {
                foundEncoding = true;
            } else if (checkUTF16(quad >>> 16)) {
                foundEncoding = true;
            }
        } else if (ensureLoaded(ClassWriter.COMPUTE_FRAMES) && checkUTF16(((this._inputBuffer[this._inputPtr] & 255) << 8) | (this._inputBuffer[this._inputPtr + 1] & 255))) {
            foundEncoding = true;
        }
        if (foundEncoding) {
            switch (this._bytesPerChar) {
                case LocalAudioAll.SORT_BY_DATE:
                    enc = JsonEncoding.UTF8;
                    break;
                case ClassWriter.COMPUTE_FRAMES:
                    enc = this._bigEndian ? JsonEncoding.UTF16_BE : JsonEncoding.UTF16_LE;
                    break;
                case JsonWriteContext.STATUS_EXPECT_VALUE:
                    enc = this._bigEndian ? JsonEncoding.UTF32_BE : JsonEncoding.UTF32_LE;
                    break;
                default:
                    throw new RuntimeException("Internal error");
            }
        } else {
            enc = JsonEncoding.UTF8;
        }
        this._context.setEncoding(enc);
        return enc;
    }

    protected boolean ensureLoaded(int minimum) throws IOException {
        int gotten = this._inputEnd - this._inputPtr;
        while (gotten < minimum) {
            int count;
            if (this._in == null) {
                count = -1;
            } else {
                count = this._in.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
            }
            if (count < 1) {
                return false;
            }
            this._inputEnd += count;
            gotten += count;
        }
        return true;
    }
}