package com.ct7ct7ct7.androidvimeoplayer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ct7ct7ct7.androidvimeoplayer.databinding.ViewDefaultControlPanelBinding;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener;
import com.ct7ct7ct7.androidvimeoplayer.model.TextTrack;
import com.ct7ct7ct7.androidvimeoplayer.model.VimeoThumbnail;
import com.ct7ct7ct7.androidvimeoplayer.utils.Utils;
import com.ct7ct7ct7.androidvimeoplayer.view.menu.VimeoMenuItem;
import com.ct7ct7ct7.androidvimeoplayer.view.menu.VimeoPlayerMenu;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DefaultControlPanelView {
    private final ViewDefaultControlPanelBinding binding;
    private boolean ended = false;
    private final VimeoPlayerMenu vimeoPlayerMenu;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            binding.controlsRootView.setVisibility(View.GONE);
        }
    };

    public DefaultControlPanelView(final VimeoPlayerView vimeoPlayerView) {
        binding = ViewDefaultControlPanelBinding.inflate(
                LayoutInflater.from(vimeoPlayerView.getContext()), vimeoPlayerView, true);

        vimeoPlayerMenu = new VimeoPlayerMenu(vimeoPlayerView.getContext());

        binding.vimeoSeekBar.setVisibility(View.INVISIBLE);
        binding.vimeoPanelView.setVisibility(View.VISIBLE);
        binding.vimeoShadeView.setVisibility(View.VISIBLE);
        binding.vimeoThumbnailImageView.setVisibility(View.VISIBLE);
        binding.controlsRootView.setVisibility(View.GONE);

        binding.vimeoPauseButton.setOnClickListener(v -> {
            vimeoPlayerView.pause();
            dismissControls(4000);
        });
        binding.vimeoPlayButton.setOnClickListener(v -> vimeoPlayerView.play());
        binding.vimeoReplayButton.setOnClickListener(v -> {
            vimeoPlayerView.seekTo(0);
            vimeoPlayerView.play();
        });

        vimeoPlayerView.addTimeListener(second -> {
            binding.vimeoCurrentTimeTextView.setText(Utils.formatTime(second));
            binding.vimeoSeekBar.setProgress((int) second);
        });

        vimeoPlayerView.addReadyListener(new VimeoPlayerReadyListener() {
            @Override
            public void onReady(String title, float duration, TextTrack[] textTrackArray) {
                binding.vimeoSeekBar.setMax((int) duration);
                binding.vimeoTitleTextView.setText(title);
                binding.vimeoPanelView.setVisibility(View.VISIBLE);
                binding.controlsRootView.setVisibility(View.VISIBLE);
                binding.vimeoShadeView.setVisibility(View.GONE);
            }

            @Override
            public void onInitFailed() {

            }
        });

        vimeoPlayerView.addStateListener(new VimeoPlayerStateListener() {
            @Override
            public void onPlaying(float duration) {
                ended = false;
                binding.vimeoSeekBar.setVisibility(View.VISIBLE);
                binding.vimeoPanelView.setBackgroundColor(Color.TRANSPARENT);
                binding.vimeoPauseButton.setVisibility(View.VISIBLE);
                binding.vimeoPlayButton.setVisibility(View.GONE);
                binding.vimeoReplayButton.setVisibility(View.GONE);
                binding.vimeoThumbnailImageView.setVisibility(View.GONE);
                dismissControls(4000);
            }

            @Override
            public void onPaused(float seconds) {
                if (ended) {
                    binding.vimeoPanelView.setBackgroundColor(Color.BLACK);
                    binding.vimeoReplayButton.setVisibility(View.VISIBLE);
                    binding.vimeoPauseButton.setVisibility(View.GONE);
                    binding.vimeoPlayButton.setVisibility(View.GONE);
                } else {
                    binding.vimeoReplayButton.setVisibility(View.GONE);
                    binding.vimeoPauseButton.setVisibility(View.GONE);
                    binding.vimeoPlayButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onEnded(float duration) {
                ended = true;
                binding.vimeoThumbnailImageView.setVisibility(View.VISIBLE);
                showControls(false);
            }
        });

        binding.vimeoTitleTextView.setVisibility(
                vimeoPlayerView.defaultOptions.title ? View.VISIBLE : View.INVISIBLE);

        if (vimeoPlayerView.defaultOptions.color != vimeoPlayerView.defaultColor) {
            binding.vimeoSeekBar.getThumb().setColorFilter(
                    vimeoPlayerView.defaultOptions.color, PorterDuff.Mode.SRC_ATOP);
            binding.vimeoSeekBar.getProgressDrawable().setColorFilter(
                    vimeoPlayerView.defaultOptions.color, PorterDuff.Mode.SRC_ATOP);
        }

        binding.vimeoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.vimeoCurrentTimeTextView.setText(Utils.formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                vimeoPlayerView.pause();
                showControls(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vimeoPlayerView.seekTo(seekBar.getProgress());
                vimeoPlayerView.play();
                dismissControls(4000);
            }
        });

        binding.vimeoPanelView.setOnClickListener(v -> showControls(true));

        binding.vimeoMenuButton.setVisibility(
                vimeoPlayerView.defaultOptions.menuOption ? View.VISIBLE : View.GONE);
        binding.vimeoFullscreenButton.setVisibility(
                vimeoPlayerView.defaultOptions.fullscreenOption ? View.VISIBLE : View.GONE);

        binding.vimeoMenuButton.setOnClickListener(v -> vimeoPlayerMenu.show(binding.vimeoMenuButton));
    }

    public void dismissControls(final int duration) {
        handler.removeCallbacks(dismissRunnable);
        handler.postDelayed(dismissRunnable, duration);
    }

    public void showControls(final boolean autoMask) {
        handler.removeCallbacks(dismissRunnable);
        binding.controlsRootView.setVisibility(View.VISIBLE);
        if (autoMask) {
            dismissControls(3000);
        }
    }

    public void setFullscreenVisibility(int value) {
        binding.vimeoFullscreenButton.setVisibility(value);
    }

    public void setFullscreenClickListener(final View.OnClickListener onClickListener) {
        binding.vimeoFullscreenButton.setOnClickListener(onClickListener);
    }

    public void setMenuVisibility(int value) {
        binding.vimeoMenuButton.setVisibility(value);
    }

    public void addMenuItem(VimeoMenuItem menuItem) {
        vimeoPlayerMenu.addItem(menuItem);
    }

    public void removeMenuItem(int itemIndex) {
        vimeoPlayerMenu.removeItem(itemIndex);
    }

    public int getMenuItemCount() {
        return vimeoPlayerMenu.getItemCount();
    }

    public void dismissMenuItem() {
        vimeoPlayerMenu.dismiss();
    }

    public void setMenuClickListener(final View.OnClickListener onClickListener) {
        binding.vimeoMenuButton.setOnClickListener(onClickListener);
    }

    public void setTopicColor(int color) {
        binding.vimeoSeekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        binding.vimeoSeekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    @SuppressLint("StaticFieldLeak")
    protected void fetchThumbnail(final Context context, final int videoId) {
        new AsyncTask<Void, Void, VimeoThumbnail>() {
            @Override
            protected VimeoThumbnail doInBackground(Void... voids) {
                String url = "https://vimeo.com/api/oembed.json?url=https://player.vimeo.com/video/" + videoId;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                VimeoThumbnail vimeoThumbnail = null;
                try {
                    Response response = client.newCall(request).execute();
                    vimeoThumbnail = new Gson().fromJson(response.body().string(), VimeoThumbnail.class);
                } catch (Exception e) {
                    Log.e(context.getPackageName(), e.toString());
                }
                return vimeoThumbnail;
            }

            @Override
            protected void onPostExecute(VimeoThumbnail vimeoThumbnail) {
                super.onPostExecute(vimeoThumbnail);
                if (vimeoThumbnail != null && vimeoThumbnail.thumbnailUrl != null) {
                    RequestOptions options = new RequestOptions()
                            .priority(Priority.NORMAL)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

                    Glide.with(context)
                            .load(vimeoThumbnail.thumbnailUrl)
                            .apply(options)
                            .into(binding.vimeoThumbnailImageView);
                }
            }
        }.execute();
    }
}
