package com.ct7ct7ct7.androidvimeoplayer.utils;

import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerErrorListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerTextTrackListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerTimeListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerVolumeListener;
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState;
import com.ct7ct7ct7.androidvimeoplayer.model.TextTrack;
import com.google.gson.Gson;

import java.util.ArrayList;


public class JsBridge {
    private final Handler mainThreadHandler;
    private final ArrayList<VimeoPlayerReadyListener> readyListeners;
    private final ArrayList<VimeoPlayerStateListener> stateListeners;
    private final ArrayList<VimeoPlayerTextTrackListener> textTrackListeners;
    private final ArrayList<VimeoPlayerTimeListener> timeListeners;
    private final ArrayList<VimeoPlayerVolumeListener> volumeListeners;
    private final ArrayList<VimeoPlayerErrorListener> errorListeners;

    public float currentTimeSeconds = 0;
    public PlayerState playerState = PlayerState.UNKNOWN;
    public float volume = 1;

    public JsBridge() {
        readyListeners = new ArrayList<>();
        stateListeners = new ArrayList<>();
        textTrackListeners = new ArrayList<>();
        timeListeners = new ArrayList<>();
        volumeListeners = new ArrayList<>();
        errorListeners = new ArrayList<>();
        this.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public void removeLastReadyListener(VimeoPlayerReadyListener readyListener) {
        this.readyListeners.remove(readyListener);
    }

    public void addReadyListener(VimeoPlayerReadyListener readyListener) {
        this.readyListeners.add(readyListener);
    }

    public void addStateListener(VimeoPlayerStateListener stateListener) {
        this.stateListeners.add(stateListener);
    }

    public void addTextTrackListener(VimeoPlayerTextTrackListener textTrackListener) {
        this.textTrackListeners.add(textTrackListener);
    }

    public void addTimeListener(VimeoPlayerTimeListener timeListener) {
        this.timeListeners.add(timeListener);
    }

    public void addVolumeListener(VimeoPlayerVolumeListener volumeListener) {
        this.volumeListeners.add(volumeListener);
    }

    public void addErrorListener(VimeoPlayerErrorListener errorListener) {
        this.errorListeners.add(errorListener);
    }


    @JavascriptInterface
    public void sendVideoCurrentTime(float seconds) {
        currentTimeSeconds = seconds;
        for (final VimeoPlayerTimeListener timeListener : timeListeners) {
            mainThreadHandler.post(() -> timeListener.onCurrentSecond(currentTimeSeconds));
        }
    }

    @JavascriptInterface
    public void sendError(final String message, final String method, final String name) {
        for (final VimeoPlayerErrorListener errorListener : errorListeners) {
            mainThreadHandler.post(() -> errorListener.onError(message, method, name));
        }
    }


    @JavascriptInterface
    public void sendReady(final String title, final float duration, final String tracksJson) {
        this.playerState = PlayerState.READY;
        final TextTrack[] textTrackArray = new Gson().fromJson(tracksJson, TextTrack[].class);
        for (final VimeoPlayerReadyListener readyListener : readyListeners) {
            mainThreadHandler.post(() -> readyListener.onReady(title, duration, textTrackArray));
        }
    }

    @JavascriptInterface
    public void sendInitFailed() {
        for (final VimeoPlayerReadyListener readyListener : readyListeners) {
            mainThreadHandler.post(() -> readyListener.onInitFailed());
        }
    }


    @JavascriptInterface
    public void sendPlaying(final float duration) {
        playerState = PlayerState.PLAYING;
        for (final VimeoPlayerStateListener stateListener : stateListeners) {
            mainThreadHandler.post(() -> stateListener.onPlaying(duration));
        }
    }


    @JavascriptInterface
    public void sendPaused(final float seconds) {
        playerState = PlayerState.PAUSED;
        for (final VimeoPlayerStateListener stateListener : stateListeners) {
            mainThreadHandler.post(() -> stateListener.onPaused(seconds));
        }
    }

    @JavascriptInterface
    public void sendEnded(final float duration) {
        playerState = PlayerState.ENDED;
        for (final VimeoPlayerStateListener stateListener : stateListeners) {
            mainThreadHandler.post(() -> stateListener.onEnded(duration));
        }
    }


    @JavascriptInterface
    public void sendVolumeChange(final float volume) {
        this.volume = volume;
        for (final VimeoPlayerVolumeListener volumeListener : volumeListeners) {
            mainThreadHandler.post(() -> volumeListener.onVolumeChanged(volume));
        }
    }

    @JavascriptInterface
    public void sendTextTrackChange(final String kind, final String label, final String language) {
        for (final VimeoPlayerTextTrackListener textTrackListener : textTrackListeners) {
            mainThreadHandler.post(() -> textTrackListener.onTextTrackChanged(kind, label, language));
        }
    }
}
