package com.eveningoutpost.dexdrip.ui.graphic;

import android.view.View;
import android.widget.ImageView;

import com.eveningoutpost.dexdrip.R;

// jamorham

// typically you should extend TrendArrowBase instead of JamTrendArrowImpl, but in this case we only
// want to change the image source and are happy to inherit whatever JamTrendArrow does in the future

class JamTrendArrow2Impl extends JamTrendArrowImpl {

    JamTrendArrow2Impl(ImageView v) {
        super(v);
        setImage(R.drawable.rounded_arrow_right_alt_white_24);
    }

    @Override
    public View getConfigurator() {
        return null;
    }

}
