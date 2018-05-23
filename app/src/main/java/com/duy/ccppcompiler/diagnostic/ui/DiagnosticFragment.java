/*
 * Copyright 2018 Mr Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ccppcompiler.diagnostic.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.duy.ccppcompiler.R;
import com.duy.ccppcompiler.diagnostic.Diagnostic;
import com.duy.ccppcompiler.diagnostic.DiagnosticClickListener;
import com.duy.ccppcompiler.diagnostic.DiagnosticContract;
import com.duy.ccppcompiler.diagnostic.suggestion.ISuggestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duy on 28-Apr-18.
 */

public class DiagnosticFragment extends Fragment implements DiagnosticContract.View, DiagnosticClickListener {
    private static final String KEY_DATA = "data";
    private DiagnosticContract.Presenter mPresenter;
    private DiagnosticAdapter mAdapter;

    private ViewFlipper mViewFlipper;
    private RecyclerView mDiagnosticView;
    private TextView mLogView;

    public static DiagnosticFragment newInstance() {

        Bundle args = new Bundle();

        DiagnosticFragment fragment = new DiagnosticFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diagnostic, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Diagnostic> diagnostics;
        if (savedInstanceState != null) {
            diagnostics = savedInstanceState.getParcelableArrayList(KEY_DATA);
        } else {
            diagnostics = new ArrayList<>();
        }

        mViewFlipper = view.findViewById(R.id.view_flipper);

        mLogView = view.findViewById(R.id.txt_log);
        mLogView.setMovementMethod(new ScrollingMovementMethod());

        mViewFlipper.setDisplayedChild(0);

        mDiagnosticView = view.findViewById(R.id.diagnostic_list_view);
        mDiagnosticView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDiagnosticView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new DiagnosticAdapter(diagnostics, getContext());
        mAdapter.setDiagnosticClickListener(this);
        mDiagnosticView.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Diagnostic> diagnostics = new ArrayList<>(mAdapter.getDiagnostics());
        outState.putParcelableArrayList(KEY_DATA, diagnostics);
    }

    @Override
    public void showDiagnostic(List<Diagnostic> diagnostics) {
        mAdapter.setData(diagnostics);
    }

    @Override
    public void showLog(CharSequence log) {
        mLogView.setText(log);
    }

    @Override
    public void remove(Diagnostic diagnostic) {
        mAdapter.remove(diagnostic);
    }

    @Override
    public void add(Diagnostic diagnostic) {
        mAdapter.add(diagnostic);
    }

    @Override
    public void clear() {
        mAdapter.clear();
        mLogView.setText("");
    }

    @Override
    public void setPresenter(DiagnosticContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDiagnosisClick(Diagnostic diagnostic, View view) {
        if (mPresenter != null) {
            mPresenter.onDiagnosticClick(view, diagnostic);
        }
    }

    @Override
    public void onSuggestionClick(View v, Diagnostic diagnostic, ISuggestion suggestion) {
        if (mPresenter != null) {
            mPresenter.onSuggestionClick(diagnostic, suggestion);
        }
    }
}
