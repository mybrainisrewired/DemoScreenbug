package com.millennialmedia.google.gson;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.google.gson.internal.LazilyParsedNumber;
import com.millennialmedia.google.gson.internal._$Gson.Preconditions;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class JsonPrimitive extends JsonElement {
    private static final Class<?>[] PRIMITIVE_TYPES;
    private Object value;

    static {
        PRIMITIVE_TYPES = new Class[]{Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class};
    }

    public JsonPrimitive(Boolean bool) {
        setValue(bool);
    }

    public JsonPrimitive(Character c) {
        setValue(c);
    }

    public JsonPrimitive(Number number) {
        setValue(number);
    }

    JsonPrimitive(Object primitive) {
        setValue(primitive);
    }

    public JsonPrimitive(String string) {
        setValue(string);
    }

    private static boolean isIntegral(JsonPrimitive primitive) {
        if (!(primitive.value instanceof Number)) {
            return false;
        }
        Number number = (Number) primitive.value;
        return number instanceof BigInteger || number instanceof Long || number instanceof Integer || number instanceof Short || number instanceof Byte;
    }

    private static boolean isPrimitiveOrString(Object target) {
        if (target instanceof String) {
            return true;
        }
        Class<?> classOfPrimitive = target.getClass();
        Class[] arr$ = PRIMITIVE_TYPES;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (arr$[i$].isAssignableFrom(classOfPrimitive)) {
                return true;
            }
            i$++;
        }
        return false;
    }

    JsonPrimitive deepCopy() {
        return this;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(com.millennialmedia.google.gson.JsonPrimitive r12_obj) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.google.gson.JsonPrimitive.equals(java.lang.Object):boolean");
        /*
        r11 = this;
        r5 = 1;
        r6 = 0;
        if (r11 != r12) goto L_0x0005;
    L_0x0004:
        return r5;
    L_0x0005:
        if (r12 == 0) goto L_0x0011;
    L_0x0007:
        r7 = r11.getClass();
        r8 = r12.getClass();
        if (r7 == r8) goto L_0x0013;
    L_0x0011:
        r5 = r6;
        goto L_0x0004;
    L_0x0013:
        r4 = r12;
        r4 = (com.millennialmedia.google.gson.JsonPrimitive) r4;
        r7 = r11.value;
        if (r7 != 0) goto L_0x0020;
    L_0x001a:
        r7 = r4.value;
        if (r7 == 0) goto L_0x0004;
    L_0x001e:
        r5 = r6;
        goto L_0x0004;
    L_0x0020:
        r7 = isIntegral(r11);
        if (r7 == 0) goto L_0x0042;
    L_0x0026:
        r7 = isIntegral(r4);
        if (r7 == 0) goto L_0x0042;
    L_0x002c:
        r7 = r11.getAsNumber();
        r7 = r7.longValue();
        r9 = r4.getAsNumber();
        r9 = r9.longValue();
        r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r7 == 0) goto L_0x0004;
    L_0x0040:
        r5 = r6;
        goto L_0x0004;
    L_0x0042:
        r7 = r11.value;
        r7 = r7 instanceof java.lang.Number;
        if (r7 == 0) goto L_0x0071;
    L_0x0048:
        r7 = r4.value;
        r7 = r7 instanceof java.lang.Number;
        if (r7 == 0) goto L_0x0071;
    L_0x004e:
        r7 = r11.getAsNumber();
        r0 = r7.doubleValue();
        r7 = r4.getAsNumber();
        r2 = r7.doubleValue();
        r7 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r7 == 0) goto L_0x006e;
    L_0x0062:
        r7 = java.lang.Double.isNaN(r0);
        if (r7 == 0) goto L_0x006f;
    L_0x0068:
        r7 = java.lang.Double.isNaN(r2);
        if (r7 == 0) goto L_0x006f;
    L_0x006e:
        r6 = r5;
    L_0x006f:
        r5 = r6;
        goto L_0x0004;
    L_0x0071:
        r5 = r11.value;
        r6 = r4.value;
        r5 = r5.equals(r6);
        goto L_0x0004;
        */
    }

    public BigDecimal getAsBigDecimal() {
        return this.value instanceof BigDecimal ? (BigDecimal) this.value : new BigDecimal(this.value.toString());
    }

    public BigInteger getAsBigInteger() {
        return this.value instanceof BigInteger ? (BigInteger) this.value : new BigInteger(this.value.toString());
    }

    public boolean getAsBoolean() {
        return isBoolean() ? getAsBooleanWrapper().booleanValue() : Boolean.parseBoolean(getAsString());
    }

    Boolean getAsBooleanWrapper() {
        return (Boolean) this.value;
    }

    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    public char getAsCharacter() {
        return getAsString().charAt(0);
    }

    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    public Number getAsNumber() {
        return this.value instanceof String ? new LazilyParsedNumber((String) this.value) : (Number) this.value;
    }

    public short getAsShort() {
        return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
    }

    public String getAsString() {
        if (isNumber()) {
            return getAsNumber().toString();
        }
        return isBoolean() ? getAsBooleanWrapper().toString() : (String) this.value;
    }

    public int hashCode() {
        if (this.value == null) {
            return ApiEventType.API_MRAID_ASYNC_PING;
        }
        long value;
        if (isIntegral(this)) {
            value = getAsNumber().longValue();
            return (int) ((value >>> 32) ^ value);
        } else if (!(this.value instanceof Number)) {
            return this.value.hashCode();
        } else {
            value = Double.doubleToLongBits(getAsNumber().doubleValue());
            return (int) ((value >>> 32) ^ value);
        }
    }

    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }

    public boolean isNumber() {
        return this.value instanceof Number;
    }

    public boolean isString() {
        return this.value instanceof String;
    }

    void setValue(Object primitive) {
        if (primitive instanceof Character) {
            this.value = String.valueOf(((Character) primitive).charValue());
        } else {
            boolean z = primitive instanceof Number || isPrimitiveOrString(primitive);
            Preconditions.checkArgument(z);
            this.value = primitive;
        }
    }
}