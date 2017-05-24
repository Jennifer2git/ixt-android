package com.imax.ipt.ui.widget.picker;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ui.PickerEvent;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.iptevent.EventBus;

public class Picker_bake extends Fragment {
    public static String KEY_OPTIONS = "OPTIONS";

    private LinearLayout mLinearLayout;
    private LinearLayout rootLayout;
    private LayoutInflater mInflater;

    protected GestureDetector mGestureDetector;
    private EventBus mEventBus;

    /**
     * x and y coordinates within our side index
     */
    protected static float sideIndexX;
    protected static float sideIndexY;

    /**
     * number of items in the side index
     */
    private int padding = 0;
    private int textSize = 0;
    protected int sideIndexHeight;

    public static final String PADDING = "PADDING";

    public static final String TEXTSIZE = "TEXTSIZE";

    private Bundle mBundle;

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_widget_picker, null);
        mLinearLayout = (LinearLayout) rootLayout.findViewById(R.id.layoutButtons);
        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBundle = getArguments();
        padding = mBundle.getInt(PADDING);
        textSize = mBundle.getInt(TEXTSIZE);
        this.init();

        return rootLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;

    }

    private void init() {

        this.mEventBus = IPT.getInstance().getEventBus();
        if (getArguments() != null) {
            String[] options = mBundle.getStringArray(KEY_OPTIONS);
//         String[] options1 = mBundle.getStringArray(KEY_OPTIONS);
//         String[] options = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
//         padding=-100;
            if (options == null) {
                Log.d("cll" ,"the options is null" );
                return;
            }
            int i = 0;
            for (String option : options) {
                Log.d("cll" ,"the option of options is "+ option );
//                View inflateView = mInflater.inflate(R.layout.item_picker_selection, null);
                View inflateView = mInflater.inflate(R.layout.item_picker_selection_movie_sort, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                if (i != 0) {
                    layoutParams.leftMargin = getPadding();
                }
                final IPTTextView iptAlphaTextView = (IPTTextView) inflateView.findViewById(R.id.txtAlphaOption);
                final Button iptTextView = (Button) inflateView.findViewById(R.id.txtOption);
                final ImageView iptImageCircle = (ImageView) inflateView.findViewById(R.id.pickerCircle);
                iptTextView.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize);

                iptAlphaTextView.setText(option);
                iptAlphaTextView.setTextColor(getResources().getColor(R.color.gold));
                iptAlphaTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                iptAlphaTextView.getPaint().setAntiAlias(true);

                iptTextView.setText(option);
                iptTextView.setTextColor(getResources().getColor(R.color.blue));
//                iptTextView.setTextColor(getResources().getColor(R.color.white));
                iptTextView.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                Log.d("PICKER","subitem on touch event, do nothing");
//                                iptAlphaTextView.setVisibility(View.VISIBLE);
//                                iptImageCircle.setVisibility(View.VISIBLE);
//                                iptAlphaTextView.setTextColor(getResources().getColor(R.color.gold));
//                                iptAlphaTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//                                iptAlphaTextView.getPaint().setAntiAlias(true);
                                break;

                            case MotionEvent.ACTION_UP:
//                                iptAlphaTextView.setVisibility(View.INVISIBLE);
//                                iptImageCircle.setVisibility(View.INVISIBLE);

                                String option = iptTextView.getText().toString();
                                mEventBus.post(new PickerEvent(option));
                                break;
                        }
                        return true;
                    }
                });
                mLinearLayout.addView(inflateView, layoutParams);
                i++;
            }
        }
    }
}
