package com.wmt.MusicPlayer;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMediaPlaybackService extends IInterface {

    public static abstract class Stub extends Binder implements IMediaPlaybackService {
        private static final String DESCRIPTOR = "com.wmt.MusicPlayer.IMediaPlaybackService";
        static final int TRANSACTION_duration = 9;
        static final int TRANSACTION_enqueue = 19;
        static final int TRANSACTION_getAlbumId = 14;
        static final int TRANSACTION_getAlbumName = 13;
        static final int TRANSACTION_getArtistId = 18;
        static final int TRANSACTION_getArtistName = 15;
        static final int TRANSACTION_getAudioId = 24;
        static final int TRANSACTION_getAudioSessionId = 35;
        static final int TRANSACTION_getDeviceId = 37;
        static final int TRANSACTION_getFilePath = 17;
        static final int TRANSACTION_getMediaMountedCount = 34;
        static final int TRANSACTION_getMusicPauseDelay = 43;
        static final int TRANSACTION_getNextAudioId = 25;
        static final int TRANSACTION_getNextPath = 26;
        static final int TRANSACTION_getPath = 23;
        static final int TRANSACTION_getQueue = 20;
        static final int TRANSACTION_getQueuePosition = 2;
        static final int TRANSACTION_getRepeatMode = 33;
        static final int TRANSACTION_getShuffleMode = 28;
        static final int TRANSACTION_getTitle = 16;
        static final int TRANSACTION_getTrackName = 12;
        static final int TRANSACTION_invalidateCursor = 39;
        static final int TRANSACTION_isInitialized = 38;
        static final int TRANSACTION_isPlaying = 3;
        static final int TRANSACTION_moveQueueItem = 21;
        static final int TRANSACTION_next = 8;
        static final int TRANSACTION_openAsync = 36;
        static final int TRANSACTION_openCurrent = 40;
        static final int TRANSACTION_openFile = 1;
        static final int TRANSACTION_pause = 5;
        static final int TRANSACTION_pauseMusicWithDelay = 42;
        static final int TRANSACTION_play = 6;
        static final int TRANSACTION_position = 10;
        static final int TRANSACTION_prev = 7;
        static final int TRANSACTION_removeNowPlayTracks = 30;
        static final int TRANSACTION_removeTrack = 31;
        static final int TRANSACTION_removeTracks = 29;
        static final int TRANSACTION_seek = 11;
        static final int TRANSACTION_setQueuePosition = 22;
        static final int TRANSACTION_setRepeatMode = 32;
        static final int TRANSACTION_setShuffleMode = 27;
        static final int TRANSACTION_stop = 4;
        static final int TRANSACTION_stopBeforePlayOnce = 41;

        private static class Proxy implements IMediaPlaybackService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public long duration() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_duration, _data, _reply, 0);
                _reply.readException();
                long _result = _reply.readLong();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public void enqueue(long[] list, int action) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeLongArray(list);
                _data.writeInt(action);
                this.mRemote.transact(TRANSACTION_enqueue, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public long getAlbumId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getAlbumId, _data, _reply, 0);
                _reply.readException();
                long _result = _reply.readLong();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public String getAlbumName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getAlbumName, _data, _reply, 0);
                _reply.readException();
                String _result = _reply.readString();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public long getArtistId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getArtistId, _data, _reply, 0);
                _reply.readException();
                long _result = _reply.readLong();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public String getArtistName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getArtistName, _data, _reply, 0);
                _reply.readException();
                String _result = _reply.readString();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public long getAudioId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getAudioId, _data, _reply, 0);
                _reply.readException();
                long _result = _reply.readLong();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public int getAudioSessionId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getAudioSessionId, _data, _reply, 0);
                _reply.readException();
                int _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public int getDeviceId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getDeviceId, _data, _reply, 0);
                _reply.readException();
                int _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public String getFilePath() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getFilePath, _data, _reply, 0);
                _reply.readException();
                String _result = _reply.readString();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            public int getMediaMountedCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getMediaMountedCount, _data, _reply, 0);
                _reply.readException();
                int _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public int getMusicPauseDelay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getMusicPauseDelay, _data, _reply, 0);
                _reply.readException();
                int _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public long getNextAudioId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getNextAudioId, _data, _reply, 0);
                _reply.readException();
                long _result = _reply.readLong();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public String getNextPath() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getNextPath, _data, _reply, 0);
                _reply.readException();
                String _result = _reply.readString();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public String getPath() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getPath, _data, _reply, 0);
                _reply.readException();
                String _result = _reply.readString();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public long[] getQueue() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getQueue, _data, _reply, 0);
                _reply.readException();
                long[] _result = _reply.createLongArray();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public int getQueuePosition() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getQueuePosition, _data, _reply, 0);
                _reply.readException();
                int _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public int getRepeatMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getRepeatMode, _data, _reply, 0);
                _reply.readException();
                int _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public int getShuffleMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getShuffleMode, _data, _reply, 0);
                _reply.readException();
                int _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public String getTitle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getTitle, _data, _reply, 0);
                _reply.readException();
                String _result = _reply.readString();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public String getTrackName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_getTrackName, _data, _reply, 0);
                _reply.readException();
                String _result = _reply.readString();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public void invalidateCursor() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_invalidateCursor, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public boolean isInitialized() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_isInitialized, _data, _reply, 0);
                _reply.readException();
                if (_reply.readInt() != 0) {
                    _result = true;
                }
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public boolean isPlaying() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_isPlaying, _data, _reply, 0);
                _reply.readException();
                if (_reply.readInt() != 0) {
                    _result = true;
                }
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public void moveQueueItem(int from, int to) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeInt(from);
                _data.writeInt(to);
                this.mRemote.transact(TRANSACTION_moveQueueItem, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void next() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_next, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void openAsync(long[] list, int position, int devID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeLongArray(list);
                _data.writeInt(position);
                _data.writeInt(devID);
                this.mRemote.transact(TRANSACTION_openAsync, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void openCurrent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_openCurrent, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void openFile(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeString(path);
                this.mRemote.transact(TRANSACTION_openFile, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void pause() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_pause, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void pauseMusicWithDelay(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeInt(level);
                this.mRemote.transact(TRANSACTION_pauseMusicWithDelay, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void play() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_play, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public long position() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_position, _data, _reply, 0);
                _reply.readException();
                long _result = _reply.readLong();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public void prev() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_prev, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public int removeNowPlayTracks(long[] removeItems) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeLongArray(removeItems);
                this.mRemote.transact(TRANSACTION_removeNowPlayTracks, _data, _reply, 0);
                _reply.readException();
                int _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public int removeTrack(long id, int devId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeLong(id);
                _data.writeInt(devId);
                this.mRemote.transact(TRANSACTION_removeTrack, _data, _reply, 0);
                _reply.readException();
                int _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public int removeTracks(int first, int last) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeInt(first);
                _data.writeInt(last);
                this.mRemote.transact(TRANSACTION_removeTracks, _data, _reply, 0);
                _reply.readException();
                int _result = _reply.readInt();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public long seek(long pos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeLong(pos);
                this.mRemote.transact(TRANSACTION_seek, _data, _reply, 0);
                _reply.readException();
                long _result = _reply.readLong();
                _reply.recycle();
                _data.recycle();
                return _result;
            }

            public void setQueuePosition(int index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeInt(index);
                this.mRemote.transact(TRANSACTION_setQueuePosition, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void setRepeatMode(int repeatmode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeInt(repeatmode);
                this.mRemote.transact(TRANSACTION_setRepeatMode, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void setShuffleMode(int shufflemode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeInt(shufflemode);
                this.mRemote.transact(TRANSACTION_setShuffleMode, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void stop() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_stop, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }

            public void stopBeforePlayOnce() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                _data.writeInterfaceToken(DESCRIPTOR);
                this.mRemote.transact(TRANSACTION_stopBeforePlayOnce, _data, _reply, 0);
                _reply.readException();
                _reply.recycle();
                _data.recycle();
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMediaPlaybackService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (iin == null || !iin instanceof IMediaPlaybackService) ? new Proxy(obj) : (IMediaPlaybackService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            int _result;
            boolean _result2;
            long _result3;
            String _result4;
            switch (code) {
                case TRANSACTION_openFile:
                    data.enforceInterface(DESCRIPTOR);
                    openFile(data.readString());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getQueuePosition:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getQueuePosition();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case TRANSACTION_isPlaying:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isPlaying();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_stop:
                    data.enforceInterface(DESCRIPTOR);
                    stop();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_pause:
                    data.enforceInterface(DESCRIPTOR);
                    pause();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_play:
                    data.enforceInterface(DESCRIPTOR);
                    play();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_prev:
                    data.enforceInterface(DESCRIPTOR);
                    prev();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_next:
                    data.enforceInterface(DESCRIPTOR);
                    next();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_duration:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = duration();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case TRANSACTION_position:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = position();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case TRANSACTION_seek:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = seek(data.readLong());
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case TRANSACTION_getTrackName:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getTrackName();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case TRANSACTION_getAlbumName:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getAlbumName();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case TRANSACTION_getAlbumId:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAlbumId();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case TRANSACTION_getArtistName:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getArtistName();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case TRANSACTION_getTitle:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getTitle();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case TRANSACTION_getFilePath:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getFilePath();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case TRANSACTION_getArtistId:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getArtistId();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case TRANSACTION_enqueue:
                    data.enforceInterface(DESCRIPTOR);
                    enqueue(data.createLongArray(), data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getQueue:
                    data.enforceInterface(DESCRIPTOR);
                    long[] _result5 = getQueue();
                    reply.writeNoException();
                    reply.writeLongArray(_result5);
                    return true;
                case TRANSACTION_moveQueueItem:
                    data.enforceInterface(DESCRIPTOR);
                    moveQueueItem(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_setQueuePosition:
                    data.enforceInterface(DESCRIPTOR);
                    setQueuePosition(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getPath:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getPath();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case TRANSACTION_getAudioId:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAudioId();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case TRANSACTION_getNextAudioId:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getNextAudioId();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case TRANSACTION_getNextPath:
                    data.enforceInterface(DESCRIPTOR);
                    _result4 = getNextPath();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case TRANSACTION_setShuffleMode:
                    data.enforceInterface(DESCRIPTOR);
                    setShuffleMode(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getShuffleMode:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getShuffleMode();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case TRANSACTION_removeTracks:
                    data.enforceInterface(DESCRIPTOR);
                    _result = removeTracks(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case TRANSACTION_removeNowPlayTracks:
                    data.enforceInterface(DESCRIPTOR);
                    _result = removeNowPlayTracks(data.createLongArray());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case TRANSACTION_removeTrack:
                    data.enforceInterface(DESCRIPTOR);
                    _result = removeTrack(data.readLong(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case TRANSACTION_setRepeatMode:
                    data.enforceInterface(DESCRIPTOR);
                    setRepeatMode(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getRepeatMode:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getRepeatMode();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case TRANSACTION_getMediaMountedCount:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getMediaMountedCount();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case TRANSACTION_getAudioSessionId:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getAudioSessionId();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case TRANSACTION_openAsync:
                    data.enforceInterface(DESCRIPTOR);
                    openAsync(data.createLongArray(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getDeviceId:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getDeviceId();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case TRANSACTION_isInitialized:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isInitialized();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case TRANSACTION_invalidateCursor:
                    data.enforceInterface(DESCRIPTOR);
                    invalidateCursor();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_openCurrent:
                    data.enforceInterface(DESCRIPTOR);
                    openCurrent();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_stopBeforePlayOnce:
                    data.enforceInterface(DESCRIPTOR);
                    stopBeforePlayOnce();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_pauseMusicWithDelay:
                    data.enforceInterface(DESCRIPTOR);
                    pauseMusicWithDelay(data.readInt());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getMusicPauseDelay:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getMusicPauseDelay();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    long duration() throws RemoteException;

    void enqueue(long[] jArr, int i) throws RemoteException;

    long getAlbumId() throws RemoteException;

    String getAlbumName() throws RemoteException;

    long getArtistId() throws RemoteException;

    String getArtistName() throws RemoteException;

    long getAudioId() throws RemoteException;

    int getAudioSessionId() throws RemoteException;

    int getDeviceId() throws RemoteException;

    String getFilePath() throws RemoteException;

    int getMediaMountedCount() throws RemoteException;

    int getMusicPauseDelay() throws RemoteException;

    long getNextAudioId() throws RemoteException;

    String getNextPath() throws RemoteException;

    String getPath() throws RemoteException;

    long[] getQueue() throws RemoteException;

    int getQueuePosition() throws RemoteException;

    int getRepeatMode() throws RemoteException;

    int getShuffleMode() throws RemoteException;

    String getTitle() throws RemoteException;

    String getTrackName() throws RemoteException;

    void invalidateCursor() throws RemoteException;

    boolean isInitialized() throws RemoteException;

    boolean isPlaying() throws RemoteException;

    void moveQueueItem(int i, int i2) throws RemoteException;

    void next() throws RemoteException;

    void openAsync(long[] jArr, int i, int i2) throws RemoteException;

    void openCurrent() throws RemoteException;

    void openFile(String str) throws RemoteException;

    void pause() throws RemoteException;

    void pauseMusicWithDelay(int i) throws RemoteException;

    void play() throws RemoteException;

    long position() throws RemoteException;

    void prev() throws RemoteException;

    int removeNowPlayTracks(long[] jArr) throws RemoteException;

    int removeTrack(long j, int i) throws RemoteException;

    int removeTracks(int i, int i2) throws RemoteException;

    long seek(long j) throws RemoteException;

    void setQueuePosition(int i) throws RemoteException;

    void setRepeatMode(int i) throws RemoteException;

    void setShuffleMode(int i) throws RemoteException;

    void stop() throws RemoteException;

    void stopBeforePlayOnce() throws RemoteException;
}