package com.federation.funf_test.UI.TabView.DataCollectionView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataCollectionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DataCollectionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Data Collection fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
