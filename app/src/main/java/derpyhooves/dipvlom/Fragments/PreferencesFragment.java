package derpyhooves.dipvlom.Fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import derpyhooves.dipvlom.R;

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Налаштування");

        ListView lv = (ListView) v.findViewById(android.R.id.list);
        Resources r = getResources();
        float left = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
        float top = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 62, r.getDisplayMetrics());
        lv.setPadding((int) left, (int) top, (int) left, (int) left);

        return v;
    }
}
