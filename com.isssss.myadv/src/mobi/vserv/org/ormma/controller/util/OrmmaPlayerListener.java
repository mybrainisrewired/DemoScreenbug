package mobi.vserv.org.ormma.controller.util;

public interface OrmmaPlayerListener {
    void onBufferingStarted();

    void onComplete();

    void onError();

    void onPrepared();
}