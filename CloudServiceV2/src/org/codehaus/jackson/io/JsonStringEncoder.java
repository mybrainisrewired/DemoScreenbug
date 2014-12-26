package org.codehaus.jackson.io;

import java.lang.ref.SoftReference;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.CharTypes;
import org.codehaus.jackson.util.TextBuffer;

public final class JsonStringEncoder {
    private static final byte[] HEX_BYTES;
    private static final char[] HEX_CHARS;
    private static final int INT_0 = 48;
    private static final int INT_BACKSLASH = 92;
    private static final int INT_U = 117;
    private static final int SURR1_FIRST = 55296;
    private static final int SURR1_LAST = 56319;
    private static final int SURR2_FIRST = 56320;
    private static final int SURR2_LAST = 57343;
    protected static final ThreadLocal<SoftReference<JsonStringEncoder>> _threadEncoder;
    protected ByteArrayBuilder _byteBuilder;
    protected final char[] _quoteBuffer;
    protected TextBuffer _textBuffer;

    static {
        HEX_CHARS = CharTypes.copyHexChars();
        HEX_BYTES = CharTypes.copyHexBytes();
        _threadEncoder = new ThreadLocal();
    }

    public JsonStringEncoder() {
        this._quoteBuffer = new char[6];
        this._quoteBuffer[0] = '\\';
        this._quoteBuffer[2] = '0';
        this._quoteBuffer[3] = '0';
    }

    private int _appendByteEscape(int ch, int escCode, ByteArrayBuilder byteBuilder, int ptr) {
        byteBuilder.setCurrentSegmentLength(ptr);
        byteBuilder.append(INT_BACKSLASH);
        if (escCode < 0) {
            byteBuilder.append(INT_U);
            if (ch > 255) {
                int hi = ch >> 8;
                byteBuilder.append(HEX_BYTES[hi >> 4]);
                byteBuilder.append(HEX_BYTES[hi & 15]);
                ch &= 255;
            } else {
                byteBuilder.append(INT_0);
                byteBuilder.append(INT_0);
            }
            byteBuilder.append(HEX_BYTES[ch >> 4]);
            byteBuilder.append(HEX_BYTES[ch & 15]);
        } else {
            byteBuilder.append((byte) escCode);
        }
        return byteBuilder.getCurrentSegmentLength();
    }

    private int _appendSingleEscape(int escCode, char[] quoteBuffer) {
        if (escCode < 0) {
            int value = -(escCode + 1);
            quoteBuffer[1] = 'u';
            quoteBuffer[4] = HEX_CHARS[value >> 4];
            quoteBuffer[5] = HEX_CHARS[value & 15];
            return FragmentManagerImpl.ANIM_STYLE_FADE_EXIT;
        } else {
            quoteBuffer[1] = (char) escCode;
            return ClassWriter.COMPUTE_FRAMES;
        }
    }

    private int _convertSurrogate(int firstPart, int secondPart) {
        if (secondPart >= 56320 && secondPart <= 57343) {
            return 65536 + (firstPart - 55296) << 10 + secondPart - 56320;
        }
        throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(firstPart) + ", second 0x" + Integer.toHexString(secondPart) + "; illegal combination");
    }

    private void _throwIllegalSurrogate(int code) {
        if (code > 1114111) {
            throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(code) + ") to output; max is 0x10FFFF as per RFC 4627");
        } else if (code < 55296) {
            throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(code) + ") to output");
        } else if (code <= 56319) {
            throw new IllegalArgumentException("Unmatched first part of surrogate pair (0x" + Integer.toHexString(code) + ")");
        } else {
            throw new IllegalArgumentException("Unmatched second part of surrogate pair (0x" + Integer.toHexString(code) + ")");
        }
    }

    public static JsonStringEncoder getInstance() {
        SoftReference<JsonStringEncoder> ref = (SoftReference) _threadEncoder.get();
        JsonStringEncoder enc = ref == null ? null : (JsonStringEncoder) ref.get();
        if (enc != null) {
            return enc;
        }
        enc = new JsonStringEncoder();
        _threadEncoder.set(new SoftReference(enc));
        return enc;
    }

    public byte[] encodeAsUTF8(String text) {
        int inputPtr;
        ByteArrayBuilder byteBuilder = this._byteBuilder;
        if (byteBuilder == null) {
            byteBuilder = new ByteArrayBuilder(null);
            this._byteBuilder = byteBuilder;
        }
        int inputEnd = text.length();
        int outputPtr = 0;
        byte[] outputBuffer = byteBuilder.resetAndGetFirstSegment();
        int outputEnd = outputBuffer.length;
        int inputPtr2 = 0;
        while (inputPtr2 < inputEnd) {
            int outputPtr2;
            inputPtr = inputPtr2 + 1;
            int c = text.charAt(inputPtr2);
            inputPtr2 = inputPtr;
            while (c <= 127) {
                if (outputPtr >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr = 0;
                }
                outputPtr2 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) c;
                if (inputPtr2 >= inputEnd) {
                    outputPtr = outputPtr2;
                    break;
                } else {
                    inputPtr = inputPtr2 + 1;
                    c = text.charAt(inputPtr2);
                    outputPtr = outputPtr2;
                    inputPtr2 = inputPtr;
                }
            }
            if (outputPtr >= outputEnd) {
                outputBuffer = byteBuilder.finishCurrentSegment();
                outputEnd = outputBuffer.length;
                outputPtr2 = 0;
            } else {
                outputPtr2 = outputPtr;
            }
            if (c < 2048) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = (byte) ((c >> 6) | 192);
                inputPtr = inputPtr2;
            } else if (c < 55296 || c > 57343) {
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = (byte) ((c >> 12) | 224);
                if (outputPtr >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr = 0;
                }
                outputPtr2 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) (((c >> 6) & 63) | 128);
                outputPtr = outputPtr2;
                inputPtr = inputPtr2;
            } else {
                if (c > 56319) {
                    _throwIllegalSurrogate(c);
                }
                if (inputPtr2 >= inputEnd) {
                    _throwIllegalSurrogate(c);
                }
                inputPtr = inputPtr2 + 1;
                c = _convertSurrogate(c, text.charAt(inputPtr2));
                if (c > 1114111) {
                    _throwIllegalSurrogate(c);
                }
                outputPtr = outputPtr2 + 1;
                outputBuffer[outputPtr2] = (byte) ((c >> 18) | 240);
                if (outputPtr >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr = 0;
                }
                outputPtr2 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) (((c >> 12) & 63) | 128);
                if (outputPtr2 >= outputEnd) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputEnd = outputBuffer.length;
                    outputPtr = 0;
                } else {
                    outputPtr = outputPtr2;
                }
                outputPtr2 = outputPtr + 1;
                outputBuffer[outputPtr] = (byte) (((c >> 6) & 63) | 128);
                outputPtr = outputPtr2;
            }
            if (outputPtr >= outputEnd) {
                outputBuffer = byteBuilder.finishCurrentSegment();
                outputEnd = outputBuffer.length;
                outputPtr = 0;
            }
            outputPtr2 = outputPtr + 1;
            outputBuffer[outputPtr] = (byte) ((c & 63) | 128);
            outputPtr = outputPtr2;
            inputPtr2 = inputPtr;
        }
        return this._byteBuilder.completeAndCoalesce(outputPtr);
    }

    public char[] quoteAsString(String input) {
        TextBuffer textBuffer = this._textBuffer;
        if (textBuffer == null) {
            textBuffer = new TextBuffer(null);
            this._textBuffer = textBuffer;
        }
        char[] outputBuffer = textBuffer.emptyAndGetCurrentSegment();
        int[] escCodes = CharTypes.get7BitOutputEscapes();
        char escCodeCount = escCodes.length;
        int inPtr = 0;
        int inputLen = input.length();
        int outPtr = 0;
        while (inPtr < inputLen) {
            while (true) {
                char c = input.charAt(inPtr);
                if (c >= escCodeCount || escCodes[c] == 0) {
                    if (outPtr >= outputBuffer.length) {
                        outputBuffer = textBuffer.finishCurrentSegment();
                        outPtr = 0;
                    }
                    int outPtr2 = outPtr + 1;
                    outputBuffer[outPtr] = c;
                    inPtr++;
                    if (inPtr >= inputLen) {
                        outPtr = outPtr2;
                        break;
                    } else {
                        outPtr = outPtr2;
                    }
                } else {
                    int inPtr2 = inPtr + 1;
                    int length = _appendSingleEscape(escCodes[input.charAt(inPtr)], this._quoteBuffer);
                    if (outPtr + length > outputBuffer.length) {
                        int first = outputBuffer.length - outPtr;
                        if (first > 0) {
                            System.arraycopy(this._quoteBuffer, 0, outputBuffer, outPtr, first);
                        }
                        outputBuffer = textBuffer.finishCurrentSegment();
                        int second = length - first;
                        System.arraycopy(this._quoteBuffer, first, outputBuffer, outPtr, second);
                        outPtr += second;
                    } else {
                        System.arraycopy(this._quoteBuffer, 0, outputBuffer, outPtr, length);
                        outPtr += length;
                    }
                    inPtr = inPtr2;
                }
            }
        }
        textBuffer.setCurrentLength(outPtr);
        return textBuffer.contentsAsArray();
    }

    public byte[] quoteAsUTF8(String text) {
        ByteArrayBuilder byteBuilder = this._byteBuilder;
        if (byteBuilder == null) {
            byteBuilder = new ByteArrayBuilder(null);
            this._byteBuilder = byteBuilder;
        }
        int inputPtr = 0;
        int inputEnd = text.length();
        int outputPtr = 0;
        byte[] outputBuffer = byteBuilder.resetAndGetFirstSegment();
        while (inputPtr < inputEnd) {
            int[] escCodes = CharTypes.get7BitOutputEscapes();
            while (true) {
                int outputPtr2;
                int ch = text.charAt(inputPtr);
                if (ch <= 127 && escCodes[ch] == 0) {
                    if (outputPtr >= outputBuffer.length) {
                        outputBuffer = byteBuilder.finishCurrentSegment();
                        outputPtr = 0;
                    }
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) ch;
                    inputPtr++;
                    if (inputPtr >= inputEnd) {
                        outputPtr = outputPtr2;
                        break;
                    } else {
                        outputPtr = outputPtr2;
                    }
                }
                if (outputPtr >= outputBuffer.length) {
                    outputBuffer = byteBuilder.finishCurrentSegment();
                    outputPtr = 0;
                }
                int inputPtr2 = inputPtr + 1;
                ch = text.charAt(inputPtr);
                if (ch <= 127) {
                    outputPtr = _appendByteEscape(ch, escCodes[ch], byteBuilder, outputPtr);
                    outputBuffer = byteBuilder.getCurrentSegment();
                    inputPtr = inputPtr2;
                } else {
                    if (ch <= 2047) {
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) ((ch >> 6) | 192);
                        ch = (ch & 63) | 128;
                        outputPtr = outputPtr2;
                        inputPtr = inputPtr2;
                    } else if (ch < 55296 || ch > 57343) {
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) ((ch >> 12) | 224);
                        if (outputPtr2 >= outputBuffer.length) {
                            outputBuffer = byteBuilder.finishCurrentSegment();
                            outputPtr = 0;
                        } else {
                            outputPtr = outputPtr2;
                        }
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) (((ch >> 6) & 63) | 128);
                        ch = (ch & 63) | 128;
                        outputPtr = outputPtr2;
                        inputPtr = inputPtr2;
                    } else {
                        if (ch > 56319) {
                            _throwIllegalSurrogate(ch);
                        }
                        if (inputPtr2 >= inputEnd) {
                            _throwIllegalSurrogate(ch);
                        }
                        inputPtr = inputPtr2 + 1;
                        ch = _convertSurrogate(ch, text.charAt(inputPtr2));
                        if (ch > 1114111) {
                            _throwIllegalSurrogate(ch);
                        }
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) ((ch >> 18) | 240);
                        if (outputPtr2 >= outputBuffer.length) {
                            outputBuffer = byteBuilder.finishCurrentSegment();
                            outputPtr = 0;
                        } else {
                            outputPtr = outputPtr2;
                        }
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) (((ch >> 12) & 63) | 128);
                        if (outputPtr2 >= outputBuffer.length) {
                            outputBuffer = byteBuilder.finishCurrentSegment();
                            outputPtr = 0;
                        } else {
                            outputPtr = outputPtr2;
                        }
                        outputPtr2 = outputPtr + 1;
                        outputBuffer[outputPtr] = (byte) (((ch >> 6) & 63) | 128);
                        ch = (ch & 63) | 128;
                        outputPtr = outputPtr2;
                    }
                    if (outputPtr >= outputBuffer.length) {
                        outputBuffer = byteBuilder.finishCurrentSegment();
                        outputPtr = 0;
                    }
                    outputPtr2 = outputPtr + 1;
                    outputBuffer[outputPtr] = (byte) ch;
                    outputPtr = outputPtr2;
                }
            }
        }
        return this._byteBuilder.completeAndCoalesce(outputPtr);
    }
}