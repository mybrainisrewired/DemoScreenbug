package com.google.android.gms.internal;

import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class p {
    private final n jP;
    private final SecureRandom ki;

    public class a extends Exception {
        public a(Throwable th) {
            super(th);
        }
    }

    public p(n nVar, SecureRandom secureRandom) {
        this.jP = nVar;
        this.ki = secureRandom;
    }

    static void c(byte[] bArr) {
        int i = 0;
        while (i < bArr.length) {
            bArr[i] = (byte) (bArr[i] ^ 68);
            i++;
        }
    }

    public byte[] b(String str) throws a {
        try {
            byte[] a = this.jP.a(str, false);
            if (a.length != 32) {
                throw new a();
            }
            byte[] bArr = new byte[16];
            ByteBuffer.wrap(a, MMAdView.TRANSITION_RANDOM, ApiEventType.API_MRAID_GET_ORIENTATION).get(bArr);
            c(bArr);
            return bArr;
        } catch (IllegalArgumentException e) {
            throw new a(e);
        }
    }

    public byte[] c(byte[] bArr, String str) throws a {
        if (bArr.length != 16) {
            throw new a();
        }
        try {
            byte[] a = this.jP.a(str, false);
            if (a.length <= 16) {
                throw new a();
            }
            ByteBuffer allocate = ByteBuffer.allocate(a.length);
            allocate.put(a);
            allocate.flip();
            byte[] bArr2 = new byte[16];
            a = new byte[(a.length - 16)];
            allocate.get(bArr2);
            allocate.get(a);
            Key secretKeySpec = new SecretKeySpec(bArr, "AES");
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(MMAdView.TRANSITION_UP, secretKeySpec, new IvParameterSpec(bArr2));
            return instance.doFinal(a);
        } catch (NoSuchAlgorithmException e) {
            throw new a(e);
        } catch (InvalidKeyException e2) {
            throw new a(e2);
        } catch (IllegalBlockSizeException e3) {
            throw new a(e3);
        } catch (NoSuchPaddingException e4) {
            throw new a(e4);
        } catch (BadPaddingException e5) {
            throw new a(e5);
        } catch (InvalidAlgorithmParameterException e6) {
            throw new a(e6);
        } catch (IllegalArgumentException e7) {
            throw new a(e7);
        }
    }
}