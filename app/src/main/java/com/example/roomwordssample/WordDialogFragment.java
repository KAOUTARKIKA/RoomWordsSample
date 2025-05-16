package com.example.roomwordssample;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.roomwordssample.model.Word;

public class WordDialogFragment extends DialogFragment {

    public interface WordDialogListener {
        void onUpdateWord(Word word);
        void onDeleteWord(Word word);
    }

    private WordDialogListener listener;
    private Word word;

    public WordDialogFragment(WordDialogListener listener, Word word) {
        this.listener = listener;
        this.word = word;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.word_dialog, null);

        EditText editWord = view.findViewById(R.id.edit_word);
        Button updateButton = view.findViewById(R.id.button_update);
        Button deleteButton = view.findViewById(R.id.button_delete);

        editWord.setText(word.getWord());

        builder.setView(view)
                .setTitle("Modifier le mot");

        AlertDialog dialog = builder.create();

        updateButton.setOnClickListener(v -> {
            String newWordText = editWord.getText().toString().trim();
            if (!TextUtils.isEmpty(newWordText)) {
                word.setWord(newWordText);
                listener.onUpdateWord(word);
                dialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(v -> {
            listener.onDeleteWord(word);
            dialog.dismiss();
        });

        return dialog;
    }
}