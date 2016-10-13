package derpyhooves.dipvlom.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import derpyhooves.dipvlom.R;

public class PreferencesCategoryTitle extends PreferenceCategory {
  public PreferencesCategoryTitle(Context context) {
    super(context);
  }

  public PreferencesCategoryTitle(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PreferencesCategoryTitle(Context context, AttributeSet attrs,
                              int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onBindView(View view) {
      super.onBindView(view);
      TextView titleView = (TextView) view.findViewById(android.R.id.title);
      titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

      DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
      int left = Math.round(16 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
      int top = Math.round(20 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
      int bottom = Math.round(5 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

      titleView.setPadding(left,top,left,bottom);
      titleView.setTextSize(16);

  }
}