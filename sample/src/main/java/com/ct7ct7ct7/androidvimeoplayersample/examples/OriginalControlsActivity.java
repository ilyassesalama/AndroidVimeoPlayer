
package com.ct7ct7ct7.androidvimeoplayersample.examples;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ct7ct7ct7.androidvimeoplayersample.R;
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ActivityOriginalControlsBinding;

public class OriginalControlsActivity extends AppCompatActivity {

    private ActivityOriginalControlsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOriginalControlsBinding.inflate(getLayoutInflater());
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
    }
}