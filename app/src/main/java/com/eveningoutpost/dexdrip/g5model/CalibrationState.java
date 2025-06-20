package com.eveningoutpost.dexdrip.g5model;

// created by jamorham

import android.util.SparseArray;

import com.eveningoutpost.dexdrip.R;
import com.eveningoutpost.dexdrip.models.UserError;
import com.google.common.collect.ImmutableSet;

import lombok.Getter;

import static com.eveningoutpost.dexdrip.services.G5CollectionService.TAG;
import static com.eveningoutpost.dexdrip.xdrip.gs;

public enum CalibrationState {

    // TODO i18n

    Unknown(0x00, "Unknown"),
    Stopped(0x01, gs(R.string.stopped)),
    WarmingUp(0x02, gs(R.string.warming_up)),
    ExcessNoise(0x03, "Excess Noise"),
    NeedsFirstCalibration(0x04, "Needs Initial Calibration"),
    NeedsSecondCalibration(0x05, "Needs Second Calibration"),
    Ok(0x06, "OK"),
    NeedsCalibration(0x07, "Needs Calibration"),
    CalibrationConfused1(0x08, "Confused Calibration 1"),
    CalibrationConfused2(0x09, "Confused Calibration 2"),
    NeedsDifferentCalibration(0x0a, "Needs More Calibration"),
    SensorFailed(0x0b, "Sensor Failed"),
    SensorFailed2(0x0c, "Sensor Failed 2"),
    UnusualCalibration(0x0d, "Unusual Calibration"),
    InsufficientCalibration(0x0e, "Insufficient Calibration"),
    Ended(0x0f, gs(R.string.ended)),
    SensorFailed3(0x10, "Sensor Failed 3"),
    TransmitterProblem(0x11, "Transmitter Problem"),
    Errors(0x12, gs(R.string.sensor_errors)),
    SensorFailed4(0x13, "Sensor Failed 4"),
    SensorFailed5(0x14, "Sensor Failed 5"),
    SensorFailed6(0x15, "Sensor Failed 6"),
    SensorFailedStart(0x16, gs(R.string.sensor_failed_start)),
    SensorFailedStart2(0x17, "Sensor Failed Start 2"),
    SensorExpired(0x18, gs(R.string.sensor_expired_2)),
    SensorFailed7(0x19, "Sensor Failed 7"), // apparently not a failure state
    SensorStopped2(0x1A, "Sensor Stopped 2"),
    SensorFailed8(0x1B, "Sensor Failed 8"),
    SensorFailed9(0x1C, "Sensor Failed 9"),
    SensorFailed10(0x1D, "Sensor Failed 10"),
    SensorFailed11(0x1E, "Sensor Failed 11"),
    SensorStarted(0xC1, gs(R.string.sensor_started)),
    SensorStopped(0xC2, gs(R.string.sensor_stopped)),
    CalibrationSent(0xC3, gs(R.string.calibration_sent));

    @Getter
    byte value;
    @Getter
    String text;


    private static final SparseArray<CalibrationState> lookup = new SparseArray<>();
    private static final ImmutableSet<CalibrationState> failed = ImmutableSet.of(SensorFailed, SensorFailed2, SensorFailed3, SensorFailed4, SensorFailed5, SensorFailed6, SensorFailedStart);
    private static final ImmutableSet<CalibrationState> stopped = ImmutableSet.of(Stopped, Ended, SensorExpired, SensorFailed, SensorFailed2, SensorFailed3, SensorFailed4, SensorFailed5, SensorFailed6, SensorFailedStart, SensorStopped);
    private static final ImmutableSet<CalibrationState> transitional = ImmutableSet.of(WarmingUp, SensorStarted, SensorStopped, CalibrationSent);


    CalibrationState(int value, String text) {
        this.value = (byte) value;
        this.text = text;
    }

    static {
        for (CalibrationState state : values()) {
            lookup.put(state.value, state);
        }
    }

    public static CalibrationState parse(byte state) {
        final CalibrationState result = lookup.get(state);
        if (result == null) UserError.Log.e(TAG, "Unknown calibration state: " + state);
        return result != null ? result : Unknown;
    }

    public static CalibrationState parse(int state) {
        return parse((byte) state);
    }

    public boolean usableGlucose() {
        return this == Ok
                || this == NeedsCalibration
                || this == SensorFailed7;
    }

    public boolean insufficientCalibration() {
        return this == InsufficientCalibration;
    }

    public boolean readyForCalibration() {
        return this == Ok
                || needsCalibration();
    }

    public boolean needsCalibration() {
        return this == NeedsCalibration
                || this == NeedsFirstCalibration
                || this == NeedsSecondCalibration
                || this == NeedsDifferentCalibration;
    }

    public boolean sensorStarted() {
        return !stopped.contains(this);
    }

    public boolean sensorFailed() {
        return failed.contains(this);
    }

    public boolean ended() {
        return this == Ended;
    }

    public boolean warmingUp() {
        return this == WarmingUp;
    }

    public boolean transitional() {
        return transitional.contains(this);
    }

    public boolean ok() {
        return this == Ok;
    }

    public boolean readyForBackfill() {
        return this != WarmingUp && this != Stopped && this != Unknown && this != NeedsFirstCalibration && this != NeedsSecondCalibration && this != Errors;
    }

    // TODO i18n
    public String getExtendedText() {
        switch (this) {
            case Ok:
                if (DexSessionKeeper.isStarted()) {
                    return getText() + " " + DexSessionKeeper.prettyTime();
                } else {
                    return getText() + " time?";
                }
            case WarmingUp:
                if (DexSessionKeeper.isStarted()) {
                    if (DexSessionKeeper.warmUpTimeValid()) {
                        return getText() + "\n" + DexSessionKeeper.prettyTime() + " " + gs(R.string.left);
                    } else {
                        return getText();
                    }
                } else {
                    return getText();
                }

            default:
                return getText();
        }
    }
}
