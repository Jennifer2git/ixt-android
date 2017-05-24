package com.imax.ipt.controller.inputs;

import android.util.Log;
import com.imax.ipt.IPT;
import com.imax.ipt.controller.BaseController;
import com.imax.ipt.controller.eventbus.handler.input.SetSelectedInputHandler;
import com.imax.ipt.model.Input;
import com.imax.ipt.ui.activity.input.InputActivity;
import com.imax.ipt.ui.util.FactoryDeviceTypeDrawable.DeviceKind;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public class InputController extends BaseController {
    public static final String TAG = "InputController";

    private InputActivity mInputActivity;

    private DeviceKind mDeviceKind;

    public InputController(InputActivity inputActivity) {
        this.mInputActivity = inputActivity;
//      this.mEventBus.register(this);
    }

    public InputController() {
    }

    @SuppressWarnings("unchecked")
    public List<Input> getInputs(DeviceKind deviceKind) {
        this.mDeviceKind = deviceKind;

        Map<DeviceKind, Vector<Input>> inputsByDeviceKind = (Map<DeviceKind, Vector<Input>>) IPT.getInstance().getIPTContext().get(IPT.INPUTS_BY_DEVICE_KIND);
        if (inputsByDeviceKind == null || inputsByDeviceKind.size() == 0) {
            Log.e(TAG, "get input by device kind error");
            return null;
        }

        return inputsByDeviceKind.get(deviceKind);


//      if (containsValue(DEVICE_TYPES))
//      {
//         List<Input> inputs = (List<Input>) getValue(DEVICE_TYPES);
//         return getInputsByDeviceKind(inputs, deviceKind);
//      }
//      else
//      {
//         this.mEventBus.post(new GetInputsByDeviceKindHandler(deviceKind.getDeviceKind()).getRequest());
//      }
//      return new ArrayList<Input>();
    }

    @Override
    public void onDestroy() {
//      this.mEventBus.unregister(this);
    }

    /**
     * @param inputId
     */
    public void onSelectedInput(int inputId) {
        this.mEventBus.post(new SetSelectedInputHandler(0, inputId).getRequest());
    }

//   /**
//    * 
//    * @param inputs
//    * @param deviceKind
//    * @return
//    */
//   private List<Input> getInputsByDeviceKind(List<Input> inputs, DeviceKind deviceKind)
//   {
//      List<Input> types = new ArrayList<Input>();
//      for (Input input : inputs)
//      {
//         if(input.getDeviceKind()==deviceKind || deviceKind == DeviceKind.ALL)
//         {
//            types.add(input);   
//         }
//      }
//      return types;
//   }
//
//   /**
//    * 
//    * @param getActiveDeviceTypesHandler
//    */
//   public void onEvent(GetInputsByDeviceKindHandler getInputsByDeviceKindHandler)
//   {
//      Input[] inputs = getInputsByDeviceKindHandler.getInputs();
//      if (inputs != null && inputs.length != 0)
//      {
//         final List<Input> types = Arrays.asList(inputs);
//         mInputActivity.runOnUiThread(new Runnable() 
//         {
//            @Override
//            public void run()
//            {
//               mInputActivity.setupView(getInputsByDeviceKind(types,mDeviceKind));
//            }
//         });
//      }
//   }
}
