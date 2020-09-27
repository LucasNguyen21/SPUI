package com.federation.funf_test.UI.TabView.TestView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TestViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public TestViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Test fragment\n Put 2 tests and questionnaire here (include keystroke tracking)");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
