package mobi.vserv.org.ormma.controller;

import android.content.Context;
import android.os.StatFs;
import com.inmobi.commons.ads.cache.AdDatabaseHelper;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.jar.JarFile;
import mobi.vserv.org.ormma.view.OrmmaView;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class OrmmaAssetController extends OrmmaController {
    private static final char[] HEX_CHARS;

    static {
        HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    }

    public OrmmaAssetController(OrmmaView adView, Context c) {
        super(adView, c);
    }

    private String asHex(MessageDigest digest) {
        byte[] hash = digest.digest();
        char[] buf = new char[(hash.length * 2)];
        int i = 0;
        int x = 0;
        while (i < hash.length) {
            int x2 = x + 1;
            buf[x] = HEX_CHARS[(hash[i] >>> 4) & 15];
            x = x2 + 1;
            buf[x2] = HEX_CHARS[hash[i] & 15];
            i++;
        }
        return new String(buf);
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            int i = 0;
            while (i < files.length) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
                i++;
            }
        }
        return path.delete();
    }

    public static boolean deleteDirectory(String path) {
        return path != null ? deleteDirectory(new File(path)) : false;
    }

    private File getAssetDir(String path) {
        return new File(new StringBuilder(String.valueOf(this.mContext.getFilesDir().getPath())).append(File.separator).append(path).toString());
    }

    private String getAssetName(String asset) {
        return asset.lastIndexOf(File.separatorChar) >= 0 ? asset.substring(asset.lastIndexOf(File.separatorChar) + 1) : asset;
    }

    private String getAssetPath(String asset) {
        return asset.lastIndexOf(File.separatorChar) >= 0 ? asset.substring(0, asset.lastIndexOf(File.separatorChar)) : "/";
    }

    private String getFilesDir() {
        return this.mContext.getFilesDir().getPath();
    }

    private HttpEntity getHttpEntity(String url) {
        try {
            return new DefaultHttpClient().execute(new HttpGet(url)).getEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String moveToAdDirectory(String fn, String filesDir, String subDir) {
        File file = new File(new StringBuilder(String.valueOf(filesDir)).append(File.separator).append(fn).toString());
        new File(new StringBuilder(String.valueOf(filesDir)).append(File.separator).append(AdDatabaseHelper.TABLE_AD).toString()).mkdir();
        File dir = new File(new StringBuilder(String.valueOf(filesDir)).append(File.separator).append(AdDatabaseHelper.TABLE_AD).append(File.separator).append(subDir).toString());
        dir.mkdir();
        file.renameTo(new File(dir, file.getName()));
        return new StringBuilder(String.valueOf(dir.getPath())).append(File.separator).toString();
    }

    public void addAsset(String alias, String url) {
        HttpEntity entity = getHttpEntity(url);
        InputStream in = null;
        try {
            in = entity.getContent();
            writeToDisk(in, alias, false);
            this.mOrmmaView.injectJavaScript(new StringBuilder("OrmmaAdController.addedAsset('").append(alias).append("' )").toString());
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
            try {
                e2.printStackTrace();
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e3) {
                    }
                }
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e4) {
                    }
                }
            }
        }
        try {
            entity.consumeContent();
        } catch (Exception e5) {
            e5.printStackTrace();
        }
    }

    public int cacheRemaining() {
        StatFs stats = new StatFs(this.mContext.getFilesDir().getPath());
        return stats.getFreeBlocks() * stats.getBlockSize();
    }

    public String copyTextFromJarIntoAssetDir(String alias, String source) {
        InputStream in = null;
        try {
            String file = OrmmaAssetController.class.getClassLoader().getResource(source).getFile();
            if (file.startsWith("file:")) {
                file = file.substring(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
            }
            int pos = file.indexOf("!");
            if (pos > 0) {
                file = file.substring(0, pos);
            }
            JarFile jf = new JarFile(file);
            in = jf.getInputStream(jf.getJarEntry(source));
            String name = writeToDisk(in, alias, false);
            if (in == null) {
                return name;
            }
            try {
                in.close();
            } catch (Exception e) {
            }
            return name;
        } catch (Exception e2) {
            try {
                e2.printStackTrace();
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e3) {
                    }
                }
                return null;
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e4) {
                    }
                }
            }
        }
    }

    public void deleteOldAds() {
        deleteDirectory(new File(new StringBuilder(String.valueOf(getFilesDir())).append(File.separator).append(AdDatabaseHelper.TABLE_AD).toString()));
    }

    public FileOutputStream getAssetOutputString(String asset) throws FileNotFoundException {
        File dir = getAssetDir(getAssetPath(asset));
        dir.mkdirs();
        return new FileOutputStream(new File(dir, getAssetName(asset)));
    }

    public String getAssetPath() {
        return new StringBuilder("file://").append(this.mContext.getFilesDir()).append("/").toString();
    }

    public void removeAsset(String asset) {
        File dir = getAssetDir(getAssetPath(asset));
        dir.mkdirs();
        new File(dir, getAssetName(asset)).delete();
        this.mOrmmaView.injectJavaScript(new StringBuilder("OrmmaAdController.assetRemoved('").append(asset).append("' )").toString());
    }

    public void stopAllListeners() {
    }

    public String writeToDisk(InputStream in, String file, boolean storeInHashedDirectory) throws IllegalStateException, IOException {
        int i = 0;
        byte[] buff = new byte[1024];
        MessageDigest digest = null;
        if (storeInHashedDirectory) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        try {
            out = getAssetOutputString(file);
            while (true) {
                int numread = in.read(buff);
                if (numread <= 0) {
                    out.flush();
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e2) {
                        }
                    }
                    String filesDir = getFilesDir();
                    if (storeInHashedDirectory && digest != null) {
                        filesDir = moveToAdDirectory(file, filesDir, asHex(digest));
                    }
                    return new StringBuilder(String.valueOf(filesDir)).append(file).toString();
                } else {
                    if (storeInHashedDirectory && digest != null) {
                        digest.update(buff);
                    }
                    out.write(buff, 0, numread);
                    i++;
                }
            }
        } catch (Throwable th) {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e3) {
                }
            }
        }
    }

    public String writeToDiskWrap(InputStream in, String file, boolean storeInHashedDirectory, String injection, String bridgePath, String ormmaPath) throws IllegalStateException, IOException {
        byte[] buff = new byte[1024];
        MessageDigest digest = null;
        if (storeInHashedDirectory) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream fromFile = new ByteArrayOutputStream();
        FileOutputStream out = null;
        while (true) {
            try {
                int numread = in.read(buff);
                if (numread <= 0) {
                    String wholeHTML = fromFile.toString();
                    if (wholeHTML.indexOf("<html") >= 0) {
                    }
                    StringBuffer wholeHTMLBuffer = null;
                    if (0 != 0) {
                        wholeHTMLBuffer = new StringBuffer(wholeHTML);
                        int start = wholeHTMLBuffer.indexOf("/ormma_bridge.js");
                        wholeHTMLBuffer.replace(start, "/ormma_bridge.js".length() + start, new StringBuilder("file:/").append(bridgePath).toString());
                        start = wholeHTMLBuffer.indexOf("/ormma.js");
                        wholeHTMLBuffer.replace(start, "/ormma.js".length() + start, new StringBuilder("file:/").append(ormmaPath).toString());
                    }
                    out = getAssetOutputString(file);
                    if (0 == 0) {
                        out.write("<html>".getBytes());
                        out.write("<head>".getBytes());
                        out.write("<meta name='viewport' content='user-scalable=no initial-scale=1.0' />".getBytes());
                        out.write("<title>Advertisement</title> ".getBytes());
                        out.write(new StringBuilder("<script src=").append(bridgePath).append(" type=\"text/javascript\"></script>").toString().getBytes());
                        out.write(new StringBuilder("<script src=").append(ormmaPath).append(" type=\"text/javascript\"></script>").toString().getBytes());
                        if (injection != null) {
                            out.write("<script type=\"text/javascript\">".getBytes());
                            out.write(injection.getBytes());
                            out.write("</script>".getBytes());
                        }
                        out.write("</head>".getBytes());
                        out.write("<body style=\"margin:0; padding:0; overflow:hidden; background-color:transparent;\">".getBytes());
                        out.write("<div align=\"center\"> ".getBytes());
                    }
                    if (0 == 0) {
                        out.write(fromFile.toByteArray());
                    } else {
                        out.write(wholeHTMLBuffer.toString().getBytes());
                    }
                    if (0 == 0) {
                        out.write("</div> ".getBytes());
                        out.write("</body> ".getBytes());
                        out.write("</html> ".getBytes());
                    }
                    out.flush();
                    if (fromFile != null) {
                        try {
                            fromFile.close();
                        } catch (Exception e2) {
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e3) {
                        }
                    }
                    String filesDir = getFilesDir();
                    if (!storeInHashedDirectory || digest == null) {
                        return filesDir;
                    }
                    return moveToAdDirectory(file, filesDir, asHex(digest));
                } else {
                    if (storeInHashedDirectory && digest != null) {
                        digest.update(buff);
                    }
                    fromFile.write(buff, 0, numread);
                }
            } catch (Throwable th) {
                if (fromFile != null) {
                    try {
                        fromFile.close();
                    } catch (Exception e4) {
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e5) {
                    }
                }
            }
        }
    }
}