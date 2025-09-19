package com.sustria.codcoz.ui.historico;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistoricoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HistoricoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}