package com.imax.ipt.ui.widget.picker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ui.PickerEvent;
import com.imax.ipt.ui.widget.horizontallistview.HListViewPickerAdapter;
import com.imax.ipt.ui.widget.horizontallistview.HorizontalListView;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;

public class Picker extends Fragment {
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

    private Context mContext;
    private HorizontalListView listPicker;
    private List<String> list= new ArrayList<String>();

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_widget_picker, null);
//        mLinearLayout = (LinearLayout) rootLayout.findViewById(R.id.layoutButtons);
        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBundle = getArguments();
        padding = mBundle.getInt(PADDING);
        textSize = mBundle.getInt(TEXTSIZE);

        mContext = container.getContext();
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
            if (options == null) {
                return;
            }
            for(String option:options){
                list.add(option);
            }
            listPicker =(HorizontalListView) rootLayout.findViewById(R.id.hlist_picker);
            HListViewPickerAdapter pickerAdapter = new HListViewPickerAdapter(mContext,list);
            listPicker.setAdapter(pickerAdapter);
            listPicker.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    listPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mEventBus.post(new PickerEvent(list.get(position)));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            //do nothing.
                        }
                    });
                    pickerAdapter.setSelectIndex(position);
                    pickerAdapter.notifyDataSetChanged();

            }
            });

//            int i = 0;
//            for (String option : options) {
////                View inflateView = mInflater.inflate(R.layout.item_picker_selection, null);
//                View inflateView = mInflater.inflate(R.layout.item_picker_selection_movie_sort, null);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//                if (i != 0) {
//                    layoutParams.leftMargin = getPadding();
//                }
////                final IPTTextView iptAlphaTextView = (IPTTextView) inflateView.findViewById(R.id.txtAlphaOption);
//                final Button iptTextView = (Button) inflateView.findViewById(R.id.txtOption);
////                final ImageView iptImageCircle = (ImageView) inflateView.findViewById(R.id.pickerCircle);
//                iptTextView.setTextSize(TypedValue.COMPLEX_UNIT_PT, textSize);
//
////                iptAlphaTextView.setText(option);
////                iptAlphaTextView.setTextColor(getResources().getColor(R.color.gold));
////                iptAlphaTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
////                iptAlphaTextView.getPaint().setAntiAlias(true);
//                Typeface face = Typeface.createFromAsset(mContext.getAssets(), Constants.FONT_LIGHT_PATH);
//                iptTextView.setTypeface(face);
//                iptTextView.setText(option);
////                iptTextView.setTextColor(getResources().getColor(R.color.gray_light));
////                iptTextView.setTextColor(getResources().getDrawable(R.drawable.selector_picker_btntxt_color));
//                iptTextView.setOnTouchListener(new OnTouchListener() {
//
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//
//                        switch (event.getAction()) {
//                            case MotionEvent.ACTION_DOWN:
//                                Log.d("PICKER", "subitem on touch event, do nothing");
////                                iptAlphaTextView.setVisibility(View.VISIBLE);
////                                iptImageCircle.setVisibility(View.VISIBLE);
////                                iptAlphaTextView.setTextColor(getResources().getColor(R.color.gold));
////                                iptAlphaTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
////                                iptAlphaTextView.getPaint().setAntiAlias(true);
//                                break;
//
//                            case MotionEvent.ACTION_UP:
////                                iptAlphaTextView.setVisibility(View.INVISIBLE);
////                                iptImageCircle.setVisibility(View.INVISIBLE);
////                                iptTextView.setTextColor(getResources().getColor(R.color.gold));
////                                String option = iptTextView.getText().toString();
////                                mEventBus.post(new PickerEvent(option));
//                                break;
//                        }
//
////                        iptTextView.setTextColor(getResources().getColor(R.color.gold));
//                        String option = iptTextView.getText().toString();
//                        mEventBus.post(new PickerEvent(option));
//                        return true;
//                    }
//                });
//                mLinearLayout.addView(inflateView, layoutParams);
//                i++;
//            }
        }
    }
}
