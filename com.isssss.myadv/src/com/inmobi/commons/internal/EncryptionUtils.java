package com.inmobi.commons.internal;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {
    private static byte[] a;
    private static String b;
    private static String c;
    private static String d;
    private static String e;
    private static String f;
    private static String g;
    private static String h;
    private static String i;
    private static String j;

    private static class a extends SecretKeySpec {
        public a(byte[] bArr, int i, int i2, String str) {
            super(bArr, i, i2, str);
        }

        public a(byte[] bArr, String str) {
            super(bArr, str);
        }
    }

    private static class b extends RSAPublicKeySpec {
        public b(BigInteger bigInteger, BigInteger bigInteger2) {
            super(bigInteger, bigInteger2);
        }
    }

    private static class c extends IvParameterSpec {
        public c(byte[] bArr) {
            super(bArr);
        }
    }

    static {
        a = new byte[16];
        b = "SHA1PRNG";
        c = "Crypto";
        d = "HmacSHA1";
        e = "RSA";
        f = "RSA/ECB/nopadding";
        g = "aeskeygenerate";
        h = "last_key_generate";
        i = "AES/CBC/PKCS7Padding";
        j = "AES";
    }

    public static byte[] DeAe(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return b(bArr, bArr2, bArr3);
    }

    public static String SeMeGe(String str, byte[] bArr, byte[] bArr2, byte[] bArr3, String str2, String str3) {
        return a(str, bArr, bArr2, bArr3, str2, str3);
    }

    private static String a(String str, byte[] bArr, byte[] bArr2, byte[] bArr3, String str2, String str3) {
        try {
            byte[] a = a(str.getBytes("UTF-8"), bArr, bArr2);
            byte[] a2 = a(a, bArr3);
            a = a(a);
            a2 = a(a2);
            return new String(Base64.encode(b(a(a(b(b(a(bArr), a(bArr3)), a(bArr2)), str3, str2)), b(a, a2)), ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] a() {
        try {
            SecureRandom.getInstance(b, c).nextBytes(a);
        } catch (NoSuchAlgorithmException e) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "NoSuchAlgorithmException");
        } catch (NoSuchProviderException e2) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "NoSuchProviderException");
        }
        return a;
    }

    private static byte[] a(byte[] bArr) {
        long length = (long) bArr.length;
        ByteBuffer allocate = ByteBuffer.allocate(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        allocate.putLong(length);
        Object array = allocate.array();
        Object obj = new Object[(array.length + bArr.length)];
        System.arraycopy(array, 0, obj, 0, array.length);
        System.arraycopy(bArr, 0, obj, array.length, bArr.length);
        return obj;
    }

    private static byte[] a(byte[] bArr, String str, String str2) {
        byte[] bArr2 = null;
        try {
            RSAPublicKey rSAPublicKey = (RSAPublicKey) KeyFactory.getInstance(e).generatePublic(new b(new BigInteger(str2, 16), new BigInteger(str, 16)));
            Cipher instance = Cipher.getInstance(f);
            instance.init(1, rSAPublicKey);
            return instance.doFinal(bArr);
        } catch (NoSuchAlgorithmException e) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "NoSuchAlgorithmException");
            return bArr2;
        } catch (InvalidKeySpecException e2) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "InvalidKeySpecException");
            return bArr2;
        } catch (NoSuchPaddingException e3) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "NoSuchPaddingException");
            return bArr2;
        } catch (InvalidKeyException e4) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "InvalidKeyException");
            return bArr2;
        } catch (IllegalBlockSizeException e5) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "IllegalBlockSizeException");
            return bArr2;
        } catch (BadPaddingException e6) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "BadPaddingException");
            return bArr2;
        }
    }

    private static byte[] a(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = null;
        Key aVar = new a(bArr2, 0, bArr2.length, d);
        try {
            Mac instance = Mac.getInstance(d);
            instance.init(aVar);
            return instance.doFinal(bArr);
        } catch (NoSuchAlgorithmException e) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "NoSuchAlgorithmException");
            return bArr3;
        } catch (InvalidKeyException e2) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "InvalidKeyException");
            return bArr3;
        }
    }

    private static byte[] a(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        byte[] bArr4 = null;
        Key aVar = new a(bArr2, j);
        AlgorithmParameterSpec cVar = new c(bArr3);
        try {
            Cipher instance = Cipher.getInstance(i);
            instance.init(1, aVar, cVar);
            return instance.doFinal(bArr);
        } catch (NoSuchAlgorithmException e) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "NoSuchAlgorithmException");
            return bArr4;
        } catch (NoSuchPaddingException e2) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "NoSuchPaddingException");
            return bArr4;
        } catch (InvalidKeyException e3) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "InvalidKeyException");
            return bArr4;
        } catch (IllegalBlockSizeException e4) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "IllegalBlockSizeException");
            return bArr4;
        } catch (BadPaddingException e5) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "BadPaddingException");
            return bArr4;
        } catch (InvalidAlgorithmParameterException e6) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "InvalidAlgorithmParameterException");
            return bArr4;
        }
    }

    private static byte[] b() {
        SharedPreferences sharedPreferences = InternalSDKUtil.getContext().getSharedPreferences(g, 0);
        long j = sharedPreferences.getLong(h, 0);
        Editor edit;
        if (0 == j) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Generating for first time");
            edit = sharedPreferences.edit();
            edit.putLong(h, System.currentTimeMillis());
            edit.commit();
            return a();
        } else {
            if (j + 86400000 - System.currentTimeMillis() <= 0) {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "generated again");
                edit = sharedPreferences.edit();
                edit.putLong(h, System.currentTimeMillis());
                edit.commit();
                return a();
            } else {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "already generated");
                return a;
            }
        }
    }

    private static byte[] b(byte[] bArr, byte[] bArr2) {
        Object obj = new Object[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        System.arraycopy(bArr2, 0, obj, bArr.length, bArr2.length);
        return obj;
    }

    private static byte[] b(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        byte[] bArr4 = null;
        Key aVar = new a(bArr2, j);
        try {
            Cipher instance = Cipher.getInstance(i);
            instance.init(MMAdView.TRANSITION_UP, aVar, new c(bArr3));
            return instance.doFinal(bArr);
        } catch (NoSuchAlgorithmException e) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "NoSuchAlgorithmException");
            return bArr4;
        } catch (NoSuchPaddingException e2) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "NoSuchPaddingException");
            return bArr4;
        } catch (InvalidKeyException e3) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "InvalidKeyException");
            return bArr4;
        } catch (IllegalBlockSizeException e4) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "IllegalBlockSizeException");
            return bArr4;
        } catch (BadPaddingException e5) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "BadPaddingException");
            return bArr4;
        } catch (InvalidAlgorithmParameterException e6) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "InvalidAlgorithmParameterException");
            return bArr4;
        }
    }

    public static byte[] generateKey(int i) {
        try {
            byte[] bArr = new byte[i];
            new SecureRandom().nextBytes(bArr);
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] keag() {
        return b();
    }
}