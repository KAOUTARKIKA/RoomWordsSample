package com.example.roomwordssample.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.roomwordssample.data.db.WordDao;
import com.example.roomwordssample.data.db.WordRoomDatabase;
import com.example.roomwordssample.model.Word;

import java.util.List;

public class WordRepository {

    private final WordDao mWordDao;
    private final LiveData<List<Word>> mAllWords;

    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAlphabetizedWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    public void insert(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.insert(word));
    }

    public void update(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.update(word));
    }

    public void delete(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.delete(word));
    }
}