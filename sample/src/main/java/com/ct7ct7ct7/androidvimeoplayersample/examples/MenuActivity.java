
package com.ct7ct7ct7.androidvimeoplayersample.examples;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ct7ct7ct7.androidvimeoplayer.view.menu.VimeoMenuItem;
import com.ct7ct7ct7.androidvimeoplayersample.R;
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
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

        binding.vimeoPlayerView.setMenuVisibility(true);
        binding.vimeoPlayerView.addMenuItem(new VimeoMenuItem("settings", R.drawable.ic_settings, view -> {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show();
            binding.vimeoPlayerView.dismissMenuItem();
        }));
        binding.vimeoPlayerView.addMenuItem(new VimeoMenuItem("star", R.drawable.ic_star, view -> {
            Toast.makeText(this, "star clicked", Toast.LENGTH_SHORT).show();
            binding.vimeoPlayerView.dismissMenuItem();
        }));
    }
}