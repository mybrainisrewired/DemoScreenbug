package com.inmobi.commons.internal;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.mopub.common.Preconditions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class FileOperations {
    public static boolean getBooleanPreferences(Context context, String str, String str2) {
        if (context != null && str != null && str2 != null && !Preconditions.EMPTY_ARGUMENTS.equals(str.trim()) && !Preconditions.EMPTY_ARGUMENTS.equals(str2.trim())) {
            return context.getSharedPreferences(str, 0).getBoolean(str2, false);
        }
        Log.debug(InternalSDKUtil.LOGGING_TAG, "Failed to get preferences..App context NULL");
        return false;
    }

    public static int getIntPreferences(Context context, String str, String str2) {
        if (context != null && str != null && str2 != null && !Preconditions.EMPTY_ARGUMENTS.equals(str.trim()) && !Preconditions.EMPTY_ARGUMENTS.equals(str2.trim())) {
            return context.getSharedPreferences(str, 0).getInt(str2, 0);
        }
        Log.debug(InternalSDKUtil.LOGGING_TAG, "Failed to get preferences..App context NULL");
        return 0;
    }

    public static long getLongPreferences(Context context, String str, String str2) {
        if (context != null && str != null && str2 != null && !Preconditions.EMPTY_ARGUMENTS.equals(str.trim()) && !Preconditions.EMPTY_ARGUMENTS.equals(str2.trim())) {
            return context.getSharedPreferences(str, 0).getLong(str2, 0);
        }
        Log.debug(InternalSDKUtil.LOGGING_TAG, "Failed to get preferences..App context NULL");
        return 0;
    }

    public static String getPreferences(Context context, String str, String str2) {
        if (context != null && str != null && str2 != null && !Preconditions.EMPTY_ARGUMENTS.equals(str.trim()) && !Preconditions.EMPTY_ARGUMENTS.equals(str2.trim())) {
            return context.getSharedPreferences(str, 0).getString(str2, null);
        }
        Log.debug(InternalSDKUtil.LOGGING_TAG, "Failed to get preferences..App context NULL");
        return null;
    }

    public static boolean isFileExist(Context context, String str) {
        return new File(context.getDir(IMBrowserActivity.EXPANDDATA, 0), str).exists();
    }

    public static String readFileAsString(Context context, String str) throws IOException {
        File file = new File(context.getCacheDir().getAbsolutePath() + File.separator + str);
        file.createNewFile();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            stringBuffer.append("\n").append(readLine);
        }
        bufferedReader.close();
        return stringBuffer.length() >= 1 ? stringBuffer.substring(1).toString() : Preconditions.EMPTY_ARGUMENTS;
    }

    public static Object readFromFile(Context context, String str) {
        ObjectInputStream objectInputStream;
        Throwable th;
        Object obj = null;
        if (context == null || str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim())) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Cannot read map application context or Filename NULL");
            return null;
        } else {
            ObjectInputStream objectInputStream2;
            try {
                objectInputStream2 = new ObjectInputStream(new FileInputStream(new File(context.getDir(IMBrowserActivity.EXPANDDATA, 0), str)));
                try {
                    obj = objectInputStream2.readObject();
                    objectInputStream = objectInputStream2;
                } catch (EOFException e) {
                    Log.internal(InternalSDKUtil.LOGGING_TAG, "End of File reached");
                    objectInputStream = objectInputStream2;
                    if (objectInputStream != null) {
                        return obj;
                    }
                    objectInputStream.close();
                    return obj;
                } catch (FileNotFoundException e2) {
                    objectInputStream = objectInputStream2;
                    th = e2;
                    Log.internal(InternalSDKUtil.LOGGING_TAG, "Event log File doesnot exist", th);
                    if (objectInputStream != null) {
                        return obj;
                    }
                    objectInputStream.close();
                    return obj;
                } catch (StreamCorruptedException e3) {
                    objectInputStream = objectInputStream2;
                    th = e3;
                    Log.internal(InternalSDKUtil.LOGGING_TAG, "Event log File corrupted", th);
                    if (objectInputStream != null) {
                        return obj;
                    }
                    objectInputStream.close();
                    return obj;
                } catch (IOException e4) {
                    objectInputStream = objectInputStream2;
                    th = e4;
                    Log.internal(InternalSDKUtil.LOGGING_TAG, "Event log File IO Exception", th);
                    if (objectInputStream != null) {
                        return obj;
                    }
                    objectInputStream.close();
                    return obj;
                } catch (ClassNotFoundException e5) {
                    objectInputStream = objectInputStream2;
                    th = e5;
                    Log.internal(InternalSDKUtil.LOGGING_TAG, "Error: class not found", th);
                    if (objectInputStream != null) {
                        return obj;
                    }
                    objectInputStream.close();
                    return obj;
                }
            } catch (EOFException e6) {
                objectInputStream2 = null;
                Log.internal(InternalSDKUtil.LOGGING_TAG, "End of File reached");
                objectInputStream = objectInputStream2;
                if (objectInputStream != null) {
                    return obj;
                }
                objectInputStream.close();
                return obj;
            } catch (FileNotFoundException e7) {
                th = e7;
                objectInputStream = null;
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Event log File doesnot exist", th);
                if (objectInputStream != null) {
                    return obj;
                }
                objectInputStream.close();
                return obj;
            } catch (StreamCorruptedException e8) {
                th = e8;
                objectInputStream = null;
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Event log File corrupted", th);
                if (objectInputStream != null) {
                    return obj;
                }
                objectInputStream.close();
                return obj;
            } catch (IOException e9) {
                th = e9;
                objectInputStream = null;
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Event log File IO Exception", th);
                if (objectInputStream != null) {
                    return obj;
                }
                objectInputStream.close();
                return obj;
            } catch (ClassNotFoundException e10) {
                th = e10;
                objectInputStream = null;
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Error: class not found", th);
                if (objectInputStream != null) {
                    return obj;
                }
                objectInputStream.close();
                return obj;
            }
            if (objectInputStream != null) {
                return obj;
            }
            try {
                objectInputStream.close();
                return obj;
            } catch (IOException e11) {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Log File Close Exception");
                return Boolean.valueOf(false);
            }
        }
    }

    public static boolean saveToFile(Context context, String str, Object obj) {
        if (context == null || str == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim()) || obj == null) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Cannot read map application context of Filename NULL");
            return false;
        } else {
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(context.getDir(IMBrowserActivity.EXPANDDATA, 0), str), false));
                objectOutputStream.writeObject(obj);
                objectOutputStream.flush();
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e) {
                        Log.internal(InternalSDKUtil.LOGGING_TAG, "Log File Close Exception");
                        return false;
                    }
                }
                return true;
            } catch (FileNotFoundException e2) {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Log File Not found", e2);
                return false;
            } catch (IOException e3) {
                Log.internal(InternalSDKUtil.LOGGING_TAG, "Log File IO Exception", e3);
                return false;
            }
        }
    }

    public static void setPreferences(Context context, String str, String str2, float f) {
        if (context == null || str == null || str2 == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim()) || Preconditions.EMPTY_ARGUMENTS.equals(str2.trim())) {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Failed to set preferences..App context NULL");
        } else {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putFloat(str2, f);
            edit.commit();
        }
    }

    public static void setPreferences(Context context, String str, String str2, int i) {
        if (context == null || str == null || str2 == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim()) || Preconditions.EMPTY_ARGUMENTS.equals(str2.trim())) {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Failed to set preferences..App context NULL");
        } else {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putInt(str2, i);
            edit.commit();
        }
    }

    public static void setPreferences(Context context, String str, String str2, long j) {
        if (context == null || str == null || str2 == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim()) || Preconditions.EMPTY_ARGUMENTS.equals(str2.trim())) {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Failed to set preferences..App context NULL");
        } else {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putLong(str2, j);
            edit.commit();
        }
    }

    public static void setPreferences(Context context, String str, String str2, boolean z) {
        if (context == null || str == null || str2 == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim()) || Preconditions.EMPTY_ARGUMENTS.equals(str2.trim())) {
            Log.debug(InternalSDKUtil.LOGGING_TAG, "Failed to set preferences..App context NULL");
        } else {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putBoolean(str2, z);
            edit.commit();
        }
    }

    public static boolean setPreferences(Context context, String str, String str2, String str3) {
        if (context == null || str == null || str2 == null || Preconditions.EMPTY_ARGUMENTS.equals(str.trim()) || Preconditions.EMPTY_ARGUMENTS.equals(str2.trim())) {
            Log.internal(InternalSDKUtil.LOGGING_TAG, "Failed to set preferences..App context NULL");
            return false;
        } else {
            Editor edit = context.getSharedPreferences(str, 0).edit();
            edit.putString(str2, str3);
            edit.commit();
            return true;
        }
    }

    public static void writeStringToFile(Context context, String str, String str2, boolean z) throws IOException {
        File file = new File(context.getCacheDir().getAbsolutePath() + File.separator + str);
        file.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, z));
        bufferedWriter.write(str2);
        bufferedWriter.close();
    }
}