package com.eveningoutpost.dexdrip.ui.classifier;

// jamorham

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.utilitymodels.HPointValue;
import com.eveningoutpost.dexdrip.utilitymodels.PointValueExtended;
import com.eveningoutpost.dexdrip.ui.helpers.BitmapLoader;
import com.eveningoutpost.dexdrip.ui.helpers.ColorUtil;

public class NoteClassifier {

    // TODO add to colorpickerx
    private static final int red = ColorUtil.blendColor(Color.parseColor("#FF2929"), Color.TRANSPARENT, 0.2f);
    private static final int amber = ColorUtil.blendColor(Color.parseColor("#FF9400"), Color.TRANSPARENT, 0.2f);
    private static final int green = ColorUtil.blendColor(Color.parseColor("#2eb82e"), Color.TRANSPARENT, 0.2f);
    private static final int grey = ColorUtil.blendColor(Color.parseColor("#666666"), Color.TRANSPARENT, 0.2f);

    public static HPointValue noteToPointValue(final String note) {

        final String haystack = note.toLowerCase();
        if (haystack.contains("battery low")) {
            return red(R.drawable.rounded_warning_24, note);

        } else if ((haystack.contains("stopped")) || (haystack.contains("detenido"))) {
            return red(R.drawable.round_flag_24, note);

        } else if (haystack.contains("paused")) {
            return amber(R.drawable.round_flag_24, note);

        } else if ((haystack.contains("started")) || (haystack.contains("iniciado"))) {
            return green(R.drawable.round_flag_24, note);

        } else if (haystack.contains("cartridge low")) {
            return red(R.drawable.rounded_warning_24, note);

        } else if (haystack.equals("connection timed out")) {
            return red(R.drawable.rounded_warning_24, note);

        } else if (haystack.startsWith("warning")) {
            return amber(R.drawable.rounded_warning_24, note);

        } else if (haystack.startsWith("maintenance")) {
            return grey(R.drawable.rounded_build_24, note);

        } else if (haystack.startsWith("reminder")) {
            return amber(R.drawable.twotone_sticky_note_2_24, note);

        } else if (haystack.startsWith("priming")) {
            return grey(R.drawable.rounded_syringe_24, note);
        }
        return grey(R.drawable.twotone_sticky_note_2_24, note);
    }


    private static HPointValue amber(@DrawableRes int id, String note) {
        return icon(id, amber, note);
    }

    private static HPointValue red(@DrawableRes int id, String note) {
        return icon(id, red, note);
    }

    private static HPointValue green(@DrawableRes int id, String note) {
        return icon(id, green, note);
    }

    private static HPointValue grey(@DrawableRes int id, String note) {
        return icon(id, grey, note);
    }

    private static HPointValue icon(@DrawableRes int id, @ColorInt int color, String note) {
        final PointValueExtended pv = new PointValueExtended(0, 0);
        BitmapLoader.loadAndSetKey(pv, id, 0);
        pv.setBitmapTint(color);
        pv.setBitmapScale(1f);
        pv.note = note;
        return pv;
    }

}
