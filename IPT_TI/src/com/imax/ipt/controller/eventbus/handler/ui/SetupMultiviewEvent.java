package com.imax.ipt.controller.eventbus.handler.ui;

public class SetupMultiviewEvent {
    public enum MultiView {
        NotSet(0), SINGLE(1), FOUR_UP(2), THREE_TOP(3), THREE_RIGTH(4), THREE_BOTTOM(5);
        private int pipMode;

        public int getPipMode() {
            return pipMode;
        }

        public void setPipMode(int pipMode) {
            this.pipMode = pipMode;
        }

        private MultiView(int pipMode) {
            this.pipMode = pipMode;
        }

        /**
         * @param pipMode
         * @return
         */
        public static MultiView getMultiView(int pipMode) {
            MultiView[] values = values();
            for (MultiView multiView : values) {
                if (multiView.getPipMode() == pipMode) {
                    return multiView;
                }
            }
            throw new IllegalArgumentException("PipMode does not supported");
        }
    }

    private MultiView multiView;

    public SetupMultiviewEvent(MultiView multiView) {
        super();
        this.multiView = multiView;
    }

    public MultiView getMultiView() {
        return multiView;
    }

    public void setMultiView(MultiView multiView) {
        this.multiView = multiView;
    }
}
