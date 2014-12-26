package org.codehaus.jackson.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.NumberInput;

public abstract class JsonNumericParserBase extends JsonParserBase {
    static final BigDecimal BD_MAX_INT;
    static final BigDecimal BD_MAX_LONG;
    static final BigDecimal BD_MIN_INT;
    static final BigDecimal BD_MIN_LONG;
    protected static final char CHAR_NULL = '\u0000';
    protected static final int INT_0 = 48;
    protected static final int INT_1 = 49;
    protected static final int INT_2 = 50;
    protected static final int INT_3 = 51;
    protected static final int INT_4 = 52;
    protected static final int INT_5 = 53;
    protected static final int INT_6 = 54;
    protected static final int INT_7 = 55;
    protected static final int INT_8 = 56;
    protected static final int INT_9 = 57;
    protected static final int INT_DECIMAL_POINT = 46;
    protected static final int INT_E = 69;
    protected static final int INT_MINUS = 45;
    protected static final int INT_PLUS = 43;
    protected static final int INT_e = 101;
    static final double MAX_INT_D = 2.147483647E9d;
    static final long MAX_INT_L = 2147483647L;
    static final double MAX_LONG_D = 9.223372036854776E18d;
    static final double MIN_INT_D = -2.147483648E9d;
    static final long MIN_INT_L = -2147483648L;
    static final double MIN_LONG_D = -9.223372036854776E18d;
    protected static final int NR_BIGDECIMAL = 16;
    protected static final int NR_BIGINT = 4;
    protected static final int NR_DOUBLE = 8;
    protected static final int NR_INT = 1;
    protected static final int NR_LONG = 2;
    protected static final int NR_UNKNOWN = 0;
    protected int _expLength;
    protected int _fractLength;
    protected int _intLength;
    protected int _numTypesValid;
    protected BigDecimal _numberBigDecimal;
    protected BigInteger _numberBigInt;
    protected double _numberDouble;
    protected int _numberInt;
    protected long _numberLong;
    protected boolean _numberNegative;

    static {
        BD_MIN_LONG = new BigDecimal(Long.MIN_VALUE);
        BD_MAX_LONG = new BigDecimal(Long.MAX_VALUE);
        BD_MIN_INT = new BigDecimal(Long.MIN_VALUE);
        BD_MAX_INT = new BigDecimal(Long.MAX_VALUE);
    }

    protected JsonNumericParserBase(IOContext ctxt, int features) {
        super(ctxt, features);
        this._numTypesValid = 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void _parseSlowFloatValue(int r4_expType) throws java.io.IOException, org.codehaus.jackson.JsonParseException {
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.impl.JsonNumericParserBase._parseSlowFloatValue(int):void");
        /*
        r3 = this;
        r1 = 16;
        if (r4 != r1) goto L_0x0011;
    L_0x0004:
        r1 = r3._textBuffer;	 Catch:{ NumberFormatException -> 0x001e }
        r1 = r1.contentsAsDecimal();	 Catch:{ NumberFormatException -> 0x001e }
        r3._numberBigDecimal = r1;	 Catch:{ NumberFormatException -> 0x001e }
        r1 = 16;
        r3._numTypesValid = r1;	 Catch:{ NumberFormatException -> 0x001e }
    L_0x0010:
        return;
    L_0x0011:
        r1 = r3._textBuffer;	 Catch:{ NumberFormatException -> 0x001e }
        r1 = r1.contentsAsDouble();	 Catch:{ NumberFormatException -> 0x001e }
        r3._numberDouble = r1;	 Catch:{ NumberFormatException -> 0x001e }
        r1 = 8;
        r3._numTypesValid = r1;	 Catch:{ NumberFormatException -> 0x001e }
        goto L_0x0010;
    L_0x001e:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Malformed numeric value '";
        r1 = r1.append(r2);
        r2 = r3._textBuffer;
        r2 = r2.contentsAsString();
        r1 = r1.append(r2);
        r2 = "'";
        r1 = r1.append(r2);
        r1 = r1.toString();
        r3._wrapError(r1, r0);
        goto L_0x0010;
        */
    }

    private final void _parseSlowIntValue(int expType, char[] buf, int offset, int len) throws IOException, JsonParseException {
        String numStr = this._textBuffer.contentsAsString();
        try {
            if (NumberInput.inLongRange(buf, offset, len, this._numberNegative)) {
                this._numberLong = Long.parseLong(numStr);
                this._numTypesValid = 2;
            } else {
                this._numberBigInt = new BigInteger(numStr);
                this._numTypesValid = 4;
            }
        } catch (NumberFormatException e) {
            _wrapError("Malformed numeric value '" + numStr + "'", e);
        }
    }

    protected void _parseNumericValue(int expType) throws IOException, JsonParseException {
        if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
            char[] buf = this._textBuffer.getTextBuffer();
            int offset = this._textBuffer.getTextOffset();
            int len = this._intLength;
            if (this._numberNegative) {
                offset++;
            }
            if (len <= 9) {
                int i = NumberInput.parseInt(buf, offset, len);
                if (this._numberNegative) {
                    i = -i;
                }
                this._numberInt = i;
                this._numTypesValid = 1;
            } else if (len <= 18) {
                long l = NumberInput.parseLong(buf, offset, len);
                if (this._numberNegative) {
                    l = -l;
                }
                if (len == 10) {
                    if (this._numberNegative) {
                        if (l >= -2147483648L) {
                            this._numberInt = (int) l;
                            this._numTypesValid = 1;
                            return;
                        }
                    } else if (l <= 2147483647L) {
                        this._numberInt = (int) l;
                        this._numTypesValid = 1;
                        return;
                    }
                }
                this._numberLong = l;
                this._numTypesValid = 2;
            } else {
                _parseSlowIntValue(expType, buf, offset, len);
            }
        } else if (this._currToken == JsonToken.VALUE_NUMBER_FLOAT) {
            _parseSlowFloatValue(expType);
        } else {
            _reportError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
        }
    }

    protected void convertNumberToBigDecimal() throws IOException, JsonParseException {
        if ((this._numTypesValid & 8) != 0) {
            this._numberBigDecimal = new BigDecimal(getText());
        } else if ((this._numTypesValid & 4) != 0) {
            this._numberBigDecimal = new BigDecimal(this._numberBigInt);
        } else if ((this._numTypesValid & 2) != 0) {
            this._numberBigDecimal = BigDecimal.valueOf(this._numberLong);
        } else if ((this._numTypesValid & 1) != 0) {
            this._numberBigDecimal = BigDecimal.valueOf((long) this._numberInt);
        } else {
            _throwInternal();
        }
        this._numTypesValid |= 16;
    }

    protected void convertNumberToBigInteger() throws IOException, JsonParseException {
        if ((this._numTypesValid & 16) != 0) {
            this._numberBigInt = this._numberBigDecimal.toBigInteger();
        } else if ((this._numTypesValid & 2) != 0) {
            this._numberBigInt = BigInteger.valueOf(this._numberLong);
        } else if ((this._numTypesValid & 1) != 0) {
            this._numberBigInt = BigInteger.valueOf((long) this._numberInt);
        } else if ((this._numTypesValid & 8) != 0) {
            this._numberBigInt = BigDecimal.valueOf(this._numberDouble).toBigInteger();
        } else {
            _throwInternal();
        }
        this._numTypesValid |= 4;
    }

    protected void convertNumberToDouble() throws IOException, JsonParseException {
        if ((this._numTypesValid & 16) != 0) {
            this._numberDouble = this._numberBigDecimal.doubleValue();
        } else if ((this._numTypesValid & 4) != 0) {
            this._numberDouble = this._numberBigInt.doubleValue();
        } else if ((this._numTypesValid & 2) != 0) {
            this._numberDouble = (double) this._numberLong;
        } else if ((this._numTypesValid & 1) != 0) {
            this._numberDouble = (double) this._numberInt;
        } else {
            _throwInternal();
        }
        this._numTypesValid |= 8;
    }

    protected void convertNumberToInt() throws IOException, JsonParseException {
        if ((this._numTypesValid & 2) != 0) {
            int result = (int) this._numberLong;
            if (((long) result) != this._numberLong) {
                _reportError("Numeric value (" + getText() + ") out of range of int");
            }
            this._numberInt = result;
        } else if ((this._numTypesValid & 4) != 0) {
            this._numberInt = this._numberBigInt.intValue();
        } else if ((this._numTypesValid & 8) != 0) {
            reportOverflowInt();
            this._numberInt = (int) this._numberDouble;
        } else if ((this._numTypesValid & 16) != 0) {
            if (BD_MIN_INT.compareTo(this._numberBigDecimal) > 0 || BD_MAX_INT.compareTo(this._numberBigDecimal) < 0) {
                reportOverflowInt();
            }
            this._numberInt = this._numberBigDecimal.intValue();
        } else {
            _throwInternal();
        }
        this._numTypesValid |= 1;
    }

    protected void convertNumberToLong() throws IOException, JsonParseException {
        if ((this._numTypesValid & 1) != 0) {
            this._numberLong = (long) this._numberInt;
        } else if ((this._numTypesValid & 4) != 0) {
            this._numberLong = this._numberBigInt.longValue();
        } else if ((this._numTypesValid & 8) != 0) {
            reportOverflowLong();
            this._numberLong = (long) this._numberDouble;
        } else if ((this._numTypesValid & 16) != 0) {
            if (BD_MIN_LONG.compareTo(this._numberBigDecimal) > 0 || BD_MAX_LONG.compareTo(this._numberBigDecimal) < 0) {
                reportOverflowLong();
            }
            this._numberLong = this._numberBigDecimal.longValue();
        } else {
            _throwInternal();
        }
        this._numTypesValid |= 2;
    }

    public BigInteger getBigIntegerValue() throws IOException, JsonParseException {
        if ((this._numTypesValid & 4) == 0) {
            if (this._numTypesValid == 0) {
                _parseNumericValue(NR_BIGINT);
            }
            if ((this._numTypesValid & 4) == 0) {
                convertNumberToBigInteger();
            }
        }
        return this._numberBigInt;
    }

    public BigDecimal getDecimalValue() throws IOException, JsonParseException {
        if ((this._numTypesValid & 16) == 0) {
            if (this._numTypesValid == 0) {
                _parseNumericValue(NR_BIGDECIMAL);
            }
            if ((this._numTypesValid & 16) == 0) {
                convertNumberToBigDecimal();
            }
        }
        return this._numberBigDecimal;
    }

    public double getDoubleValue() throws IOException, JsonParseException {
        if ((this._numTypesValid & 8) == 0) {
            if (this._numTypesValid == 0) {
                _parseNumericValue(NR_DOUBLE);
            }
            if ((this._numTypesValid & 8) == 0) {
                convertNumberToDouble();
            }
        }
        return this._numberDouble;
    }

    public float getFloatValue() throws IOException, JsonParseException {
        return (float) getDoubleValue();
    }

    public int getIntValue() throws IOException, JsonParseException {
        if ((this._numTypesValid & 1) == 0) {
            if (this._numTypesValid == 0) {
                _parseNumericValue(NR_INT);
            }
            if ((this._numTypesValid & 1) == 0) {
                convertNumberToInt();
            }
        }
        return this._numberInt;
    }

    public long getLongValue() throws IOException, JsonParseException {
        if ((this._numTypesValid & 2) == 0) {
            if (this._numTypesValid == 0) {
                _parseNumericValue(NR_LONG);
            }
            if ((this._numTypesValid & 2) == 0) {
                convertNumberToLong();
            }
        }
        return this._numberLong;
    }

    public NumberType getNumberType() throws IOException, JsonParseException {
        if (this._numTypesValid == 0) {
            _parseNumericValue(0);
        }
        if (this._currToken != JsonToken.VALUE_NUMBER_INT) {
            return (this._numTypesValid & 16) != 0 ? NumberType.BIG_DECIMAL : NumberType.DOUBLE;
        } else {
            if ((this._numTypesValid & 1) != 0) {
                return NumberType.INT;
            }
            return (this._numTypesValid & 2) != 0 ? NumberType.LONG : NumberType.BIG_INTEGER;
        }
    }

    public Number getNumberValue() throws IOException, JsonParseException {
        if (this._numTypesValid == 0) {
            _parseNumericValue(0);
        }
        if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
            if ((this._numTypesValid & 1) != 0) {
                return Integer.valueOf(this._numberInt);
            }
            if ((this._numTypesValid & 2) != 0) {
                return Long.valueOf(this._numberLong);
            }
            return (this._numTypesValid & 4) != 0 ? this._numberBigInt : this._numberBigDecimal;
        } else if ((this._numTypesValid & 16) != 0) {
            return this._numberBigDecimal;
        } else {
            if ((this._numTypesValid & 8) == 0) {
                _throwInternal();
            }
            return Double.valueOf(this._numberDouble);
        }
    }

    protected void reportInvalidNumber(String msg) throws JsonParseException {
        _reportError("Invalid numeric value: " + msg);
    }

    protected void reportOverflowInt() throws IOException, JsonParseException {
        _reportError("Numeric value (" + getText() + ") out of range of int (" + Integer.MIN_VALUE + " - " + Integer.MAX_VALUE + ")");
    }

    protected void reportOverflowLong() throws IOException, JsonParseException {
        _reportError("Numeric value (" + getText() + ") out of range of long (" + Long.MIN_VALUE + " - " + Long.MAX_VALUE + ")");
    }

    protected void reportUnexpectedNumberChar(int ch, String comment) throws JsonParseException {
        String msg = "Unexpected character (" + _getCharDesc(ch) + ") in numeric value";
        if (comment != null) {
            msg = msg + ": " + comment;
        }
        _reportError(msg);
    }

    protected final JsonToken reset(boolean negative, int intLen, int fractLen, int expLen) {
        return (fractLen >= 1 || expLen >= 1) ? resetFloat(negative, intLen, fractLen, expLen) : resetInt(negative, intLen);
    }

    protected final JsonToken resetAsNaN(String valueStr, double value) {
        this._textBuffer.resetWithString(valueStr);
        this._numberDouble = value;
        this._numTypesValid = 8;
        return JsonToken.VALUE_NUMBER_FLOAT;
    }

    protected final JsonToken resetFloat(boolean negative, int intLen, int fractLen, int expLen) {
        this._numberNegative = negative;
        this._intLength = intLen;
        this._fractLength = fractLen;
        this._expLength = expLen;
        this._numTypesValid = 0;
        return JsonToken.VALUE_NUMBER_FLOAT;
    }

    protected final JsonToken resetInt(boolean negative, int intLen) {
        this._numberNegative = negative;
        this._intLength = intLen;
        this._fractLength = 0;
        this._expLength = 0;
        this._numTypesValid = 0;
        return JsonToken.VALUE_NUMBER_INT;
    }
}