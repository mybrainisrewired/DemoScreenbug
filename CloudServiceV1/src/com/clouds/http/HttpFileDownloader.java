package com.clouds.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.clouds.constant.Constant;
import com.clouds.db.Dao;
import com.clouds.db.DownloadInfo;
import com.clouds.debug.SystemDebug;
import com.clouds.object.FileDownloadinfo;
import com.clouds.object.JSONFileInfo;
import com.clouds.util.FileManager;
import com.wmt.opengl.grid.ItemAnimation;
import com.wmt.util.MmInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLHandshakeException;

class HttpFileDownloader {
    private static final String TAG;
    public static final String screenshotsPath = "/mnt/local/.screenshots";
    private Context context;
    private List<FileDownloadinfo> fileDownloadinfos;
    private Handler handler;

    class FileDownloaderThread extends Thread {
        private List<JSONFileInfo> fileInfos;

        public FileDownloaderThread(List<JSONFileInfo> fileInfos) {
            this.fileInfos = null;
            this.fileInfos = fileInfos;
        }

        public void run() {
            super.run();
            HttpFileDownloader.this.fileDownloadinfos = HttpFileDownloader.this.startdownload(this.fileInfos);
            HttpFileDownloader.this.sendMessage(ItemAnimation.CUR_ARC);
        }
    }

    static {
        TAG = HttpFileDownloader.class.getSimpleName();
    }

    HttpFileDownloader(Context context, Handler handler) {
        this.fileDownloadinfos = null;
        this.context = context;
        this.handler = handler;
    }

    private boolean checkDownloadSucceed(String filePath, String fileHashCode) {
        Log.i(TAG, "Begin to compare sha256 hash code");
        boolean isSucceed = false;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(filePath);
            byte[] dataBytes = new byte[1024];
            while (true) {
                int nread = fis.read(dataBytes);
                if (nread == -1) {
                    break;
                }
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();
            StringBuffer hexString = new StringBuffer();
            int i = 0;
            while (i < mdbytes.length) {
                hexString.append(Integer.toString(mdbytes[i] & 255 + 256, MmInfo.MMINFO_MEM_SIZE).substring(1));
                i++;
            }
            Log.i(TAG, new StringBuilder("SHA-256 hash value : ").append(hexString.toString()).append(", mDownloadedFileSHA = ").append(fileHashCode).toString());
            if (fileHashCode == null || !fileHashCode.equalsIgnoreCase(hexString.toString())) {
                isSucceed = false;
            } else {
                isSucceed = true;
            }
            SystemDebug.e(TAG, new StringBuilder("isSucceed: ").append(isSucceed).toString());
        } catch (NoSuchAlgorithmException e) {
            NoSuchAlgorithmException e2 = e;
            Log.e(TAG, "There is no SHA-256 algorithm");
            Log.e(TAG, e2.getMessage());
        } catch (FileNotFoundException e3) {
            FileNotFoundException e4 = e3;
            Log.e(TAG, "Update package does not exists");
            Log.e(TAG, e4.getMessage());
        } catch (IOException e5) {
            IOException e6 = e5;
            Log.e(TAG, "Read file error. Maybe file is corrupted");
            Log.e(TAG, e6.getMessage());
        }
        return isSucceed;
    }

    private void createNewFile(String savePath, int fileSize) {
        Log.d(TAG, new StringBuilder("createNewFile:").append(savePath).toString());
        try {
            File file = new File(savePath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileDownloadPath(String deviceType, String downloadFileName) {
        if (deviceType == null || !deviceType.equals(Constant.DEVICE_TV)) {
            return new StringBuilder(String.valueOf(this.context.getFilesDir().getAbsolutePath())).append(File.separator).append(downloadFileName).toString();
        }
        FileManager.createFileDir(screenshotsPath);
        return new StringBuilder(screenshotsPath).append(File.separator).append(downloadFileName).toString();
    }

    private DownloadInfo init(int index, String url, String savePath, int fileSize) {
        createNewFile(savePath, fileSize);
        Dao dao = getDao();
        if (!isFirst(url)) {
            return dao.getInfos(url);
        }
        DownloadInfo info = new DownloadInfo(index, 0, fileSize - 1, 0, url);
        if (dao == null) {
            return info;
        }
        dao.saveInfos(info);
        return info;
    }

    private boolean isFirst(String urlstr) {
        return getDao() != null ? getDao().isHasInfors(urlstr) : true;
    }

    private boolean isHTTPSHead(String url) {
        return url != null ? url.startsWith("https") : false;
    }

    private FileDownloadinfo saveDownloadFile(int index, String savePath, String url, String appType, int fileSize, String hashCode) {
        SSLHandshakeException e;
        Exception e2;
        FileDownloadinfo fileDownloadinfo = null;
        try {
            DownloadInfo downloadInfo = init(index, url, savePath, fileSize);
            int startPos = downloadInfo.getStartPos();
            int compeleteSize = downloadInfo.getCompeleteSize();
            compeleteSize = 0;
            int endPos = downloadInfo.getEndPos();
            int threadId = downloadInfo.getThreadId();
            SystemDebug.e(TAG, new StringBuilder("downloadInfo: ").append(downloadInfo.toString()).toString());
            URL url2 = mUrl;
            String str = url;
            try {
                URLConnection localURLConnection = mUrl.openConnection();
                localURLConnection.setAllowUserInteraction(true);
                localURLConnection.setRequestProperty("Range", new StringBuilder("bytes=").append(0).append("-").append(endPos).toString());
                str = savePath;
                String str2 = "rwd";
                randomAccessFile.seek((long) 0);
                InputStream is = localURLConnection.getInputStream();
                byte[] buffer = new byte[4096];
                while (true) {
                    int length = is.read(buffer);
                    if (length == -1) {
                        break;
                    }
                    randomAccessFile.write(buffer, 0, length);
                    compeleteSize += length;
                    getDao().updataInfos(threadId, compeleteSize, url);
                }
                if (hashCode == null || !checkDownloadSucceed(savePath, hashCode)) {
                    Dao.getDaoInstanca(this.context).delete(url);
                } else {
                    Log.v(TAG, new StringBuilder("finish url: ").append(url).append(" index: ").append(index).toString());
                    fileDownloadinfo = new FileDownloadinfo(url, savePath, appType, fileSize);
                }
                is.close();
                randomAccessFile.close();
                return fileDownloadinfo;
            } catch (SSLHandshakeException e3) {
                e = e3;
                e.printStackTrace();
                if (isHTTPSHead(url)) {
                    url = url.replace("https:", "http:");
                }
                return saveDownloadFile(index, savePath, url, appType, fileSize, hashCode);
            } catch (Exception e4) {
                e2 = e4;
                e2.printStackTrace();
                return fileDownloadinfo;
            }
        } catch (SSLHandshakeException e5) {
            e = e5;
            e.printStackTrace();
            try {
                if (isHTTPSHead(url)) {
                    url = url.replace("https:", "http:");
                }
                return saveDownloadFile(index, savePath, url, appType, fileSize, hashCode);
            } catch (Exception e6) {
                e6.printStackTrace();
                return fileDownloadinfo;
            }
        } catch (Exception e7) {
            e2 = e7;
            e2.printStackTrace();
            return fileDownloadinfo;
        }
    }

    private void sendMessage(int what) {
        Log.v(TAG, new StringBuilder("sendMessage what: ").append(what).toString());
        if (this.handler != null) {
            Message msg = Message.obtain();
            msg.what = what;
            this.handler.sendMessage(msg);
        }
    }

    private List<FileDownloadinfo> startdownload(List<JSONFileInfo> fileInfos) {
        List<FileDownloadinfo> fileDownloadinfos = new ArrayList();
        if (fileInfos != null && fileInfos.size() > 0) {
            int listSize = fileInfos.size();
            int i = 0;
            while (i < listSize) {
                JSONFileInfo jsonFileInfo = (JSONFileInfo) fileInfos.get(i);
                int mDownloadFileSize = jsonFileInfo.getSize();
                String mDownloadURL = jsonFileInfo.getUrl();
                String hashCode = jsonFileInfo.getSha256();
                String appType = jsonFileInfo.getType();
                SystemDebug.e(TAG, new StringBuilder("[jsonListInfo]: ").append(jsonFileInfo.toString()).toString());
                String localfile = getFileDownloadPath(jsonFileInfo.getDeviceType(), mDownloadURL.substring(mDownloadURL.lastIndexOf("/") + 1));
                SystemDebug.e(TAG, new StringBuilder("localfile: ").append(localfile).toString());
                fileDownloadinfos.add(saveDownloadFile(i, localfile, mDownloadURL, appType, mDownloadFileSize, hashCode));
                i++;
            }
        }
        return fileDownloadinfos;
    }

    public Dao getDao() {
        return this.context != null ? Dao.getDaoInstanca(this.context) : null;
    }

    List<FileDownloadinfo> getDownloadFileInfo() {
        return this.fileDownloadinfos;
    }

    void startFileDownloadThread(List<JSONFileInfo> fileInfos) {
        new FileDownloaderThread(fileInfos).start();
    }
}