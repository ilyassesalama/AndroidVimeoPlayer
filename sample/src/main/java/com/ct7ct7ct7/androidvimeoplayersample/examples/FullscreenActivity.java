
package com.ct7ct7ct7.androidvimeoplayersample.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState;
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity;
import com.ct7ct7ct7.androidvimeoplayersample.R;
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ActivityFullscreenBinding;

public class FullscreenActivity extends AppCompatActivity {

    private ActivityFullscreenBinding binding;
    private static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar();
        setupView();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void setupView() {
        getLifecycle().addObserver(binding.vimeoPlayerView);
        binding.vimeoPlayerView.initialize(true, 59777392);
        binding.vimeoPlayerView.setFullscreenVisibility(true);

        binding.vimeoPlayerView.setFullscreenClickListener(view -> {
            String requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_AUTO;
            Intent intent = VimeoPlayerActivity.createIntent(this, requestOrientation, binding.vimeoPlayerView);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            float playAt = data.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f);
            binding.vimeoPlayerView.seekTo(playAt);

            String playerStateStr = data.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE);
            if (playerStateStr != null) {
                PlayerState playerState = PlayerState.valueOf(playerStateStr);
                if (playerState == PlayerState.PLAYING) {
                    binding.vimeoPlayerView.play();
                } else if (playerState == PlayerState.PAUSED) {
                    binding.vimeoPlayerView.pause();
                }
            }
        }
    }
}