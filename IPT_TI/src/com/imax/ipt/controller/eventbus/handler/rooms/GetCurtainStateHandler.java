package com.imax.ipt.controller.eventbus.handler.rooms;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.imax.ipt.conector.api.Request;
import com.imax.ipt.controller.eventbus.handler.Handler;
import com.imax.ipt.controller.eventbus.handler.input.SetVideoModeHandler.VideoMode;
import com.imax.iptevent.EventBus;

public class GetCurtainStateHandler extends Handler {
    public static final String TAG = "MoveCurtainHandler";
    public static final String METHOD_NAME = "getCurtainState";

    public enum CurtainState {
        Closed(0),
        Opened(1),
        NotAvailable(2);

        private int value;

        private CurtainState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static CurtainState getCurtainState(int value) {
            CurtainState[] values = values();
            for (CurtainState curtainState : values) {
                if (curtainState.value == value)
                    return curtainState;
            }
            throw new IllegalArgumentException("CurtainState does not support");
        }
    }

    private CurtainState curtainState;

    @Override
    public List<Object> getParameters() {
        return new ArrayList<Object>();
    }

    @Override
    public void onCreateEvent(EventBus eventBus, String sbResult) {
        Log.d(TAG, sbResult);
        try {
            JSONObject rootObject = new JSONObject(sbResult);
            JSONObject result = rootObject.getJSONObject("result");
            curtainState = CurtainState.getCurtainState(result.getInt("intValue"));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        eventBus.post(this);
    }

    @Override
    public Request getRequest() {
        return new Request(GET_STATE_CURTAINS, METHOD_NAME, getParameters(), this);
    }

    /**
     * Getters and Setters
     */
//   public int getStateCurtain()
//   {
//      return stateCurtain;
//   }
//
//   public void setStateCurtain(int stateCurtain)
//   {
//      this.stateCurtain = stateCurtain;
//   }
    public CurtainState getCurtainState() {
        return curtainState;
    }
}
