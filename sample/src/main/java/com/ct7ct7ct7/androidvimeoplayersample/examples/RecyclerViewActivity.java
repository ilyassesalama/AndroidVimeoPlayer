
package com.ct7ct7ct7.androidvimeoplayersample.examples;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.ct7ct7ct7.androidvimeoplayersample.R;
import com.ct7ct7ct7.androidvimeoplayersample.databinding.ActivityRecyclerviewBinding;
import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {

    private ActivityRecyclerviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecyclerviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar();
        setupView();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void setupView() {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(56282283);
        ids.add(318380844);
        ids.add(318173011);
        ids.add(318794329);
        ids.add(315632203);
        ids.add(19231868);

        ExampleRecyclerViewAdapter adapter = new ExampleRecyclerViewAdapter(getLifecycle(), ids);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }
}