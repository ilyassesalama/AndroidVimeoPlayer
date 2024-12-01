package com.ct7ct7ct7.androidvimeoplayersample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener;
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener;
import com.ct7ct7ct7.androidvimeoplayer.model.TextTrack;
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ActivityMainBinding;
import com.ct7ct7ct7.androidvimeoplayersample.examples.FullscreenActivity;
import com.ct7ct7ct7.androidvimeoplayersample.examples.MenuActivity;
import com.ct7ct7ct7.androidvimeoplayersample.examples.OriginalControlsActivity;
import com.ct7ct7ct7.androidvimeoplayersample.examples.RecyclerViewActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar();
        setupView();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        binding.toolbar.setNavigationOnClickListener(v -> {
            if (binding.drawerLayout.isDrawerOpen(binding.navigationView)) {
                binding.drawerLayout.closeDrawers();
            } else {
                binding.drawerLayout.openDrawer(binding.navigationView);
            }
        });

        binding.navigationView.setNavigationItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.fullscreenExampleItem) {
                startActivity(new Intent(MainActivity.this, FullscreenActivity.class));
            } else if (itemId == R.id.menuExampleItem) {
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
            } else if (itemId == R.id.recyclerViewExampleItem) {
                startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
            } else if (itemId == R.id.originalControlsExampleItem) {
                startActivity(new Intent(MainActivity.this, OriginalControlsActivity.class));
            }
            return false;
        });
    }

    private void setupView() {
        getLifecycle().addObserver(binding.vimeoPlayerView);
        binding.vimeoPlayerView.initialize(true, 830288008);
        // binding.vimeoPlayerView.initialize(true, {YourPrivateVideoId}, "SettingsEmbeddedUrl");
        // binding.vimeoPlayerView.initialize(true, {YourPrivateVideoId},"VideoHashKey", "SettingsEmbeddedUrl");

        binding.vimeoPlayerView.addTimeListener(second -> binding.playerCurrentTimeTextView.setText(
                getString(R.string.player_current_time, Float.toString(second))
        ));

        binding.vimeoPlayerView.addErrorListener((message, method, name) -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show());

        binding.vimeoPlayerView.addReadyListener(new VimeoPlayerReadyListener() {
            @Override
            public void onReady(String title, float duration, TextTrack[] textTrackArray) {
                binding.playerStateTextView.setText(getString(R.string.player_state, "onReady"));
            }

            @Override
            public void onInitFailed() {
                binding.playerStateTextView.setText(getString(R.string.player_state, "onInitFailed"));
            }
        });

        binding.vimeoPlayerView.addStateListener(new VimeoPlayerStateListener() {
            @Override
            public void onPlaying(float duration) {
                binding.playerStateTextView.setText(getString(R.string.player_state, "onPlaying"));
            }

            @Override
            public void onPaused(float seconds) {
                binding.playerStateTextView.setText(getString(R.string.player_state, "onPaused"));
            }

            @Override
            public void onEnded(float duration) {
                binding.playerStateTextView.setText(getString(R.string.player_state, "onEnded"));
            }
        });

        binding.volumeSeekBar.setProgress(100);
        binding.volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = (float) progress / 100;
                binding.vimeoPlayerView.setVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });

        binding.vimeoPlayerView.addVolumeListener(volume -> binding.playerVolumeTextView.setText(getString(R.string.player_volume, Float.toString(volume))));

        binding.playButton.setOnClickListener(v -> binding.vimeoPlayerView.play());

        binding.pauseButton.setOnClickListener(v -> binding.vimeoPlayerView.pause());

        binding.getCurrentTimeButton.setOnClickListener(v -> Toast.makeText(
                MainActivity.this,
                getString(
                        R.string.player_current_time,
                        Float.toString(binding.vimeoPlayerView.getCurrentTimeSeconds())
                ),
                Toast.LENGTH_LONG
        ).show());

        binding.loadVideoButton.setOnClickListener(v -> binding.vimeoPlayerView.loadVideo(19231868));

        binding.colorButton.setOnClickListener(v -> binding.vimeoPlayerView.setTopicColor(Color.GREEN));
    }
}
