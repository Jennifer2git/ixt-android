package com.imax.ipt.ui.activity.input;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.R.layout;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.remote.SetInputNameHandler;
import com.imax.ipt.model.DeviceType;
import com.imax.ipt.model.Input;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.util.DisplayUtil;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable;
import com.imax.ipt.ui.util.VibrateUtil;
import com.imax.iptevent.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link DialogSetInputNameFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the
 * {@link DialogSetInputNameFragment#newInstance} factory method to create an
 * instance of this fragment.
 */
public class DialogSetInputNameFragment extends DialogFragment implements OnClickListener {
    public static final String TAG = DialogSetInputNameFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "DeviceKind";
    private static final String ARG_PARAM2 = "Index";

    private String mDeviceKind;
    private int mIndex;
    private Context mContext;
    private EventBus mEventBus;
    private Button mBtnOK;
    private EditText mEditInputName;
    private IPTTextView tv_DisplayName;
    private Button mBtnCancel;

    private OnFragmentInteractionListener mListener;

    public static DialogSetInputNameFragment newInstance(int index, String deviceKind) {
        DialogSetInputNameFragment fragment = new DialogSetInputNameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, deviceKind);
        args.putInt(ARG_PARAM2, index);
        fragment.setArguments(args);
        return fragment;
    }

    public DialogSetInputNameFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getActivity();
        if (getArguments() != null) {
            mDeviceKind = getArguments().getString(ARG_PARAM1);
            mIndex = getArguments().getInt(ARG_PARAM2);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mViewDialogFragment = inflater.inflate(layout.fragment_dialog_set_input_name, container, false);
        Dialog d = getDialog();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = DisplayUtil.dip2px(inflater.getContext(), 450);
        lp.height = DisplayUtil.dip2px(inflater.getContext(), 450);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.night_blue));
        d.show();
        d.getWindow().setAttributes(lp);

        this.init(mViewDialogFragment);
        return mViewDialogFragment;
    }

    private void init(View view) {
        this.mEventBus = IPT.getInstance().getEventBus();

        mBtnOK = (Button) view.findViewById(R.id.btnOk);
        mBtnOK.setOnClickListener(this);
        mEditInputName = (EditText) view.findViewById(R.id.edit_input_name);
        tv_DisplayName = (IPTTextView) ((GridView) getActivity().findViewById(R.id.grid_inputs))
                .getChildAt(mIndex).findViewById(R.id.txt_input_name);

//        mBtnCancel = (Button) view.findViewById(R.id.btnCancel);
//        mBtnCancel.setOnClickListener(this);  // NOTE remove cancel btn.
    }

    @Override
    public void onDestroy() {
        this.mEventBus.unregister(this);
        super.onDestroy();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                String inputName = mEditInputName.getText().toString();
                setCustomDisplayName(mIndex, mDeviceKind, inputName);
                dismiss();
                VibrateUtil.vibrate(mContext, 100);
                break;
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.btnClose:
                dismiss();
                break;
            default:
        }
    }

    private void setCustomDisplayName(int index, String deviceKind, String displayName) {
        if (displayName.isEmpty()) {
            return;
        }

        tv_DisplayName.setText(displayName.toUpperCase());
        setCustomName2Server(deviceKind, displayName);
        setCustomName2Preference(index, deviceKind, displayName);
        setCustomName2InputsMap(deviceKind, displayName);

    }

    private void setCustomName2InputsMap(String deviceKind, String displayName){
        Map<FactoryDeviceTypeDrawable.DeviceKind, Vector<Input>>
                map = ((Map<FactoryDeviceTypeDrawable.DeviceKind, Vector<Input>>) IPT.getInstance()
                .getIPTContext().get(IPT.INPUTS_BY_DEVICE_KIND));
        ((Vector<Input>) map.get(FactoryDeviceTypeDrawable.DeviceKind.getDeviceKindEnum(deviceKind)))
                .get(0).setDisplayName(displayName);
    }

    private void setCustomName2Server(String deviceKind, String displayName){
        mEventBus.post(new SetInputNameHandler(FactoryDeviceTypeDrawable.DeviceKind.getDeviceKindEnum(deviceKind), displayName).getRequest());
    }

    private void setCustomName2Preference(int index, String deviceKind, String displayName) {
        SharedPreferences sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        String strJson = sp.getString(Constants.CONFIG_KEY_ACTIVE_DEVICE, null);
        try {
            JSONArray jsonArrayDeviceTypes = new JSONArray(strJson);
            JSONObject jsonObject = jsonArrayDeviceTypes.getJSONObject(index);

            if (jsonObject.getString(DeviceType.KEY_DEVICE_KIND).equals(deviceKind)) {
                jsonObject.put(DeviceType.KEY_DISPLAY_NAME, displayName);
            }

            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Constants.CONFIG_KEY_ACTIVE_DEVICE, jsonArrayDeviceTypes.toString());
            editor.commit();

            Log.d(TAG, "CLL reset the displayname json string " + sp.getString(Constants.CONFIG_KEY_ACTIVE_DEVICE, null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
