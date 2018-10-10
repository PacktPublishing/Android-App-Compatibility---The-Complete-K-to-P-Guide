package com.example.jonnd.fuelfinder.adapters;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

public class BindingAdapters {

    @BindingAdapter("enabledGone")
    /**
     * Binding adapter for controlling the visibility of a {@link FloatingActionButton}.
     */
    public static void enabledGone(FloatingActionButton button, boolean enabled) {
        if (enabled) {
            button.show();
        }
        else {
            button.hide();
        }
    }

    @BindingAdapter("enabledGone")
    /**
     * Binding adapter for controlling the visibility of a {@link View}.
     */
    public static void enabledGone(View view, boolean enabled) {
        view.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("adapter")
    /**
     * Binding adapter for setting an {@link ArrayAdapter} on an {@link AutoCompleteTextView}.
     * This is necessary because the method {@link AutoCompleteTextView#setAdapter(ListAdapter)}
     * expects generic type that is inconvient to define with data binding.
     */
    public static void setAdapter(AutoCompleteTextView textView, ArrayAdapter arrayAdapter) {
        textView.setAdapter(arrayAdapter);
    }

    @BindingAdapter("onPageChangeListener")
    /**
     * Binding adapter for setting a {@link android.support.v4.view.ViewPager.OnPageChangeListener}
     * on a ViewPager.
     */
    public static void setOnPageChangeListener(ViewPager pager, ViewPager.OnPageChangeListener listener) {
        pager.addOnPageChangeListener(listener);
    }
}
