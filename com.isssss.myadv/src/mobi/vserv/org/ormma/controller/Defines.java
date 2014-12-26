package mobi.vserv.org.ormma.controller;

import android.content.Context;
import com.mopub.common.Preconditions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Defines {
    public static final boolean ENABLE_lOGGING;
    public static boolean temp;

    public class Events {
        public static final String HEADING_CHANGE = "headingChange";
        public static final String KEYBOARD_CHANGE = "keyboardChange";
        public static final String LOCATION_CHANGE = "locationChange";
        public static final String NETWORK_CHANGE = "networkChange";
        public static final String ORIENTATION_CHANGE = "orientationChange";
        public static final String SCREEN_CHANGE = "screenChange";
        public static final String SHAKE = "shake";
        public static final String SIZE_CHANGE = "sizeChange";
        public static final String STATE_CHANGE = "stateChange";
        public static final String TILT_CHANGE = "tiltChange";
        public static final String VSERADFETCH = "vservADFetch";
    }

    static {
        temp = false;
        ENABLE_lOGGING = temp;
    }

    public static String readFileFromInternalStorage(Context context, String fileName) {
        String eol = System.getProperty("line.separator");
        BufferedReader input = null;
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader input2 = new BufferedReader(new InputStreamReader(context.openFileInput(fileName)));
            while (true) {
                try {
                    String line = input2.readLine();
                    if (line == null) {
                        String toString = buffer.toString();
                        if (input2 != null) {
                            try {
                                input2.close();
                            } catch (IOException e) {
                            }
                        }
                        return toString;
                    } else {
                        buffer.append(new StringBuilder(String.valueOf(line)).append(eol).toString());
                    }
                } catch (Exception e2) {
                    input = input2;
                } catch (Throwable th) {
                    th = th;
                    input = input2;
                }
            }
        } catch (Exception e3) {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e4) {
                }
            }
            return Preconditions.EMPTY_ARGUMENTS;
        } catch (Throwable th2) {
            Throwable th3;
            th3 = th2;
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e5) {
                }
            }
            throw th3;
        }
    }

    public static void writeFileToInternalStorage(Context context, String fileName, String contents) {
        BufferedWriter writer = null;
        try {
            BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(fileName, 0)));
            try {
                writer2.write(contents);
                if (writer2 != null) {
                    try {
                        writer2.close();
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
                writer = writer2;
                if (writer != null) {
                    writer.close();
                }
            } catch (Throwable th) {
                th = th;
                writer = writer2;
                if (writer != null) {
                    writer.close();
                }
                throw th;
            }
        } catch (Exception e3) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e4) {
                }
            }
        } catch (Throwable th2) {
            Throwable th3 = th2;
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e5) {
                }
            }
            throw th3;
        }
    }
}