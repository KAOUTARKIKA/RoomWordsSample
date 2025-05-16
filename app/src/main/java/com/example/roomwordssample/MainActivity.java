package com.example.roomwordssample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.roomwordssample.adapter.WordListAdapter;
import com.example.roomwordssample.databinding.ActivityMainBinding;
import com.example.roomwordssample.model.Word;

public class MainActivity extends AppCompatActivity implements WordListAdapter.OnWordClickListener, WordDialogFragment.WordDialogListener {

    private ActivityMainBinding binding;
    private WordViewModel mWordViewModel;
    private WordListAdapter mAdapter;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialiser l'adaptateur avec l'écouteur de clics
        mAdapter = new WordListAdapter(this);
        binding.contentMain.recyclerview.setAdapter(mAdapter);
        binding.contentMain.recyclerview.setHasFixedSize(true);

        // Initialiser le ViewModel une seule fois
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        // Observer les changements de données
        mWordViewModel.getAllWords().observe(this, words -> {
            // Mettre à jour la copie en cache des mots dans l'adaptateur
            mAdapter.setWords(words);
        });

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onWordClick(Word word) {
        // Afficher la boîte de dialogue pour modifier/supprimer
        WordDialogFragment dialog = new WordDialogFragment(this, word);
        dialog.show(getSupportFragmentManager(), "WordDialog");
    }

    @Override
    public void onUpdateWord(Word word) {
        mWordViewModel.update(word);
        Toast.makeText(this, "Mot mis à jour", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteWord(Word word) {
        mWordViewModel.delete(word);
        Toast.makeText(this, "Mot supprimé", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}