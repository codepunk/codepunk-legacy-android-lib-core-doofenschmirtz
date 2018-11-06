/*
 * Copyright (C) 2007 The Android Open Source Project
 * Modifications copyright (C) 2018 Codepunk, LLC
 *               Author(s): Scott Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * The original work can be found at The Android Open Source Project at
 *
 *      https://github.com/aosp-mirror
 *
 * In the following location:
 *
 *      platform_frameworks_base/core/java/com/android/internal/preference/YesNoPreference.java
 *
 * Modifications:
 * July 2018: Updated to extend support version of DialogPreference
 *            Moved onDialogClosed logic to YesNoPreferenceDialogFragmentCompat as is
 *            consistent with support version of Preference framework
 */

package com.codepunk.doofenschmirtz.borrowed.android.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.codepunk.doofenschmirtz.R;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

/**
 * The {@link YesNoPreference} is a preference to show a dialog with Yes and No
 * buttons.
 * <p>
 * This preference will store a boolean into the SharedPreferences.
 */
public class YesNoPreference extends DialogPreference {

    // region Properties

    /**
     * Stores whether our last result was a positive one.
     */
    private boolean mWasPositiveResult;

    // endregion Properties

    // region Constructors

    @SuppressWarnings("WeakerAccess")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public YesNoPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("WeakerAccess")
    public YesNoPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.YesNoPreference);
    }

    @SuppressWarnings("WeakerAccess")
    public YesNoPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.yesNoPreferenceStyle);
    }

    @SuppressWarnings("unused")
    public YesNoPreference(Context context) {
        this(context, null);
    }

    // endregion Constructors

    // region Inherited methods

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getBoolean(index, false);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedBoolean(mWasPositiveResult) :
                (Boolean) defaultValue);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        setValue(defaultValue == null ? getPersistedBoolean(mWasPositiveResult) :
                (Boolean) defaultValue);
    }

    @Override
    public boolean shouldDisableDependents() {
        return !mWasPositiveResult || super.shouldDisableDependents();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.wasPositiveResult = getValue();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setValue(myState.wasPositiveResult);
    }

    // endregion Inherited methods

    // region Methods

    /**
     * Sets the value of this preference, and saves it to the persistent store
     * if required.
     *
     * @param value The value of the preference.
     */
    public void setValue(boolean value) {
        mWasPositiveResult = value;

        persistBoolean(value);

        notifyDependencyChange(!value);
    }

    /**
     * Gets the value of this preference.
     *
     * @return The value of the preference.
     */
    public boolean getValue() {
        return mWasPositiveResult;
    }

    // endregion methods

    // region Nested/inner classes

    private static class SavedState extends BaseSavedState {
        boolean wasPositiveResult;

        SavedState(Parcel source) {
            super(source);
            wasPositiveResult = source.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(wasPositiveResult ? 1 : 0);
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    // endregion Nested/inner classes

}
