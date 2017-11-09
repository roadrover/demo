package com.roadrover.demo.ui.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roadrover.demo.R;
import com.roadrover.sdk.car.CarManager;
import com.roadrover.sdk.car.IVICar;
import com.roadrover.sdk.car.Trip;
import com.roadrover.sdk.utils.Logcat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 车载信息activity.
 */

public class VehicleInfoActivity extends SDKActivity {

    /**
     * 车门提示文字信息类
     */
    public static class DoorInfo {

        public int mId = -1;
        public String mValue = "";

        public DoorInfo(int id, String value) {
            mId = id;
            mValue = value;
        }

        @Override
        public String toString() {
            return "id:" + getName(mId) + " " + mValue;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof DoorInfo) {
                DoorInfo other = (DoorInfo) o;
                if (other.mId == mId) { // id 一样认为相等
                    return true;
                }
            }
            return false;
        }

        public String getName(int id) {
            switch (id) {
                case IVICar.Door.Id.FRONT_LEFT:
                    return "FRONT_LEFT";

                case IVICar.Door.Id.FRONT_RIGHT:
                    return "FRONT_RIGHT";

                case IVICar.Door.Id.REAR_LEFT:
                    return "REAR_LEFT";

                case IVICar.Door.Id.REAR_RIGHT:
                    return "REAR_RIGHT";

                case IVICar.Door.Id.TRUNK:
                    return "TRUNK";

                case IVICar.Door.Id.HOOT:
                    return "BONNET";

                default:
                    return "UNKNWON";
            }
        }
    }

    // 原车管理类对象
    private CarManager mCarManager = null;

    private List<DoorInfo> mDoorInfos = new ArrayList<>();

    // 视图对象
    private View mRootView;

    private TextView mTv_remain_fuel, mTv_out_temperature, mTv_battery_voltage, mTv_seat_belt, mTv_trunk_state,
            mTv_brake_state, mTv_lean_water, mTv_motor_speed, mTv_speed, mTv_mileage;

    private TextView mTv_remain_fuel2, mTv_out_temperature2, mTv_battery_voltage2, mTv_seat_belt2, mTv_trunk_state2,
            mTv_brake_state2, mTv_lean_water2;

    private ImageView mIv_remain_fuel, mIv_out_temperature, mIv_battery_voltage, mIv_seat_belt, mIv_trunk_state,
            mIv_brake_state, mIv_lean_water;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoorChanged(IVICar.Door event) {
        updateDoorMessage(mCarManager, this);
        updateDoors();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandbrakeChanged(IVICar.Handbrake event) {
        updateHandbrake(event.mStatus);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTripChanged(Trip trip) {
        if (trip.mId == Trip.Id.TOTAL &&
                trip.mIndex == Trip.Index.DISTANCE) {
            updateTotalDistance(trip.mValue);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExtraStateChanged(IVICar.ExtraState state) {
        if (state.mId == IVICar.ExtraState.Id.REMAIN_FUEL) {
            updateRemainFuel(state.mValue);
        } else if (state.mId == IVICar.ExtraState.Id.BATTERY_VOLTAGE) {
            updateBatteryVoltage(state.mValue);
        } else if (state.mId == IVICar.ExtraState.Id.CLEAN_WATER) {
            updateCleanWater(state.mValue);
        } else if (state.mId == IVICar.ExtraState.Id.SEAT_BELT) {
            updateBelt(state.mValue);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRealTimeInfoChanged(IVICar.RealTimeInfo info) {
        if (info.mId == IVICar.RealTimeInfo.Id.ENGINE_RPM) {
            updateMotorSpeed(info.mValue);
        } else if (info.mId == IVICar.RealTimeInfo.Id.SPEED) {
            updateCarSpeed(info.mValue);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOutsideTempChanged(IVICar.OutsideTemp temp) {
        updateOutsideTemp(temp.mRawValue);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccChanged(IVICar.Acc acc) {
        if (acc.mOn) {
            updateDoorMessage(mCarManager, this);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_vehicle_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = findViewById(R.id.rootView);

        // 注册EventBus
        EventBus.getDefault().register(this);

        // 创建SDK Manager对象
        mCarManager = new CarManager(this, this, null);
    }

    @Override
    protected void onDestroy() {
        mRootView = null;
        if (null != mCarManager) {
            mCarManager.disconnect();
            mCarManager = null;
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onViewValid() {
        super.onViewValid();

        // 添加车载信息layout
        initViews();
        updateDoors();
        updateDriving();

        if (null != mRootView) {
            mRootView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onViewInvalid() {
        if (null != mRootView) {
            mRootView.setVisibility(View.GONE);
        }
        super.onViewInvalid();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        LinearLayout llayout = (LinearLayout) findViewById(R.id.vw_remain_fuel);
        mTv_remain_fuel = (TextView) llayout.findViewById(R.id.vw_info_item1_valume);
        mIv_remain_fuel = (ImageView) llayout.findViewById(R.id.vw_info_item1_icon);
        mTv_remain_fuel2 = (TextView) llayout.findViewById(R.id.vw_info_item1_title);
        mTv_remain_fuel2.setText(R.string.left_oil);
        mIv_remain_fuel.setImageResource(R.drawable.remain_fuel);

        llayout = (LinearLayout) findViewById(R.id.vw_out_temperature);
        mTv_out_temperature = (TextView) llayout.findViewById(R.id.vw_info_item1_valume);
        mIv_out_temperature = (ImageView) llayout.findViewById(R.id.vw_info_item1_icon);
        mTv_out_temperature2 = (TextView) llayout.findViewById(R.id.vw_info_item1_title);
        mTv_out_temperature2.setText(R.string.out_temp);
        mIv_out_temperature.setImageResource(R.drawable.out_temperature);

        llayout = (LinearLayout) findViewById(R.id.vw_battery_voltage);
        mTv_battery_voltage = (TextView) llayout.findViewById(R.id.vw_info_item1_valume);
        mIv_battery_voltage = (ImageView) llayout.findViewById(R.id.vw_info_item1_icon);
        mTv_battery_voltage2 = (TextView) llayout.findViewById(R.id.vw_info_item1_title);
        mTv_battery_voltage2.setText(R.string.battery_voltage);
        mIv_battery_voltage.setImageResource(R.drawable.battery_voltage);

        llayout = (LinearLayout) findViewById(R.id.vw_seat_belt);
        mTv_seat_belt = (TextView) llayout.findViewById(R.id.vw_info_item1_valume);
        mIv_seat_belt = (ImageView) llayout.findViewById(R.id.vw_info_item1_icon);
        mTv_seat_belt2 = (TextView) llayout.findViewById(R.id.vw_info_item1_title);
        mTv_seat_belt2.setText(R.string.seat_belt);
        mIv_seat_belt.setImageResource(R.drawable.seat_belt);

        llayout = (LinearLayout) findViewById(R.id.vw_trunk_state);
        mTv_trunk_state = (TextView) llayout.findViewById(R.id.vw_info_item1_valume);
        mIv_trunk_state = (ImageView) llayout.findViewById(R.id.vw_info_item1_icon);
        mTv_trunk_state2 = (TextView) llayout.findViewById(R.id.vw_info_item1_title);
        mTv_trunk_state2.setText(R.string.trunk_state);
        mIv_trunk_state.setImageResource(R.drawable.trunk_state);

        llayout = (LinearLayout) findViewById(R.id.vw_brake_state);
        mTv_brake_state = (TextView) llayout.findViewById(R.id.vw_info_item1_valume);
        mIv_brake_state = (ImageView) llayout.findViewById(R.id.vw_info_item1_icon);
        mTv_brake_state2 = (TextView) llayout.findViewById(R.id.vw_info_item1_title);
        mTv_brake_state2.setText(R.string.brake);
        mIv_brake_state.setImageResource(R.drawable.brake_state);

        llayout = (LinearLayout) findViewById(R.id.vw_lean_water);
        mTv_lean_water = (TextView) llayout.findViewById(R.id.vw_info_item1_valume);
        mIv_lean_water = (ImageView) llayout.findViewById(R.id.vw_info_item1_icon);
        mTv_lean_water2 = (TextView) llayout.findViewById(R.id.vw_info_item1_title);
        mTv_lean_water2.setText(R.string.lean_water);
        mIv_lean_water.setImageResource(R.drawable.lean_water);

        llayout = (LinearLayout) findViewById(R.id.vw_motor_speed);
        mTv_motor_speed = (TextView) llayout.findViewById(R.id.vw_info_item1_valume);
        TextView title = (TextView) llayout.findViewById(R.id.vw_info_item1_title);
        title.setTextSize(11);
        title.setText(R.string.motor_speed);
        llayout.setBackgroundResource(R.drawable.motor_speed);

        llayout = (LinearLayout) findViewById(R.id.vw_speed);
        mTv_speed = (TextView) llayout.findViewById(R.id.vw_info_item1_valume);
        title = (TextView) llayout.findViewById(R.id.vw_info_item1_title);
        title.setText(R.string.speed2);
        llayout.setBackgroundResource(R.drawable.speed);

        llayout = (LinearLayout) findViewById(R.id.vw_mileage);
        mTv_mileage = (TextView) llayout.findViewById(R.id.vw_info_item1_valume);
        title = (TextView) llayout.findViewById(R.id.vw_info_item1_title);
        title.setText(R.string.mileage);
        llayout.setBackgroundResource(R.drawable.mileage);
    }

    private void updateDoors() {
        updateDoorImage(mCarManager,
                (ImageView) findViewById(R.id.vw_front_left_door),
                (ImageView) findViewById(R.id.vw_front_right_door),
                (ImageView) findViewById(R.id.vw_rear_left_door),
                (ImageView) findViewById(R.id.vw_rear_right_door),
                (ImageView) findViewById(R.id.vw_trunk_door),
                null);

        TextView doorState = (TextView) findViewById(R.id.door_state);
        updateDoorText(doorState, this, true,
                R.drawable.vm_doors_closed, R.drawable.vm_icon_doors_closed,
                R.drawable.vw_btn_on, R.drawable.vw_e);

        mIv_trunk_state.setImageResource(R.drawable.trunk_state);
        mTv_trunk_state.setVisibility(View.VISIBLE);
        mTv_trunk_state2.setText(R.string.trunk_state);
        if (mCarManager != null && mCarManager.isDoorOpen(IVICar.Door.Id.TRUNK)) {
            mTv_trunk_state2.setText(R.string.state_trunk_on);
            mIv_trunk_state.setImageResource(R.drawable.trunk_state2);
            mTv_trunk_state.setVisibility(View.GONE);
        } else {
            mTv_trunk_state.setText(R.string.wg_door_off);
        }
    }

    private void updateDriving() {
        CarManager car = mCarManager;
        if (car == null) {
            return;
        }
        updateOutsideTemp(car.getOutsideTempRawValue());
        updateTotalDistance(car.getTrip(Trip.Id.TOTAL, Trip.Index.DISTANCE));
        updateRemainFuel(car.getExtraState(IVICar.ExtraState.Id.REMAIN_FUEL));
        updateBatteryVoltage(car.getExtraState(IVICar.ExtraState.Id.BATTERY_VOLTAGE));
        updateBelt(car.getExtraState(IVICar.ExtraState.Id.SEAT_BELT));
        updateCleanWater(car.getExtraState(IVICar.ExtraState.Id.CLEAN_WATER));
        updateHandbrake(car.getHandBrakeStatus());

        car.registerRealTimeInfoId(IVICar.RealTimeInfo.Id.ENGINE_RPM);
        car.registerRealTimeInfoId(IVICar.RealTimeInfo.Id.SPEED);
    }

    private void updateOutsideTemp(int rawValue) {
        if (mTv_out_temperature != null) {
            if (rawValue == IVICar.OUTSIDE_TEMP_UNKNOWN) {
                mTv_out_temperature.setText("--");
            } else {
                final int unit = IVICar.TemperatureUnit.C;
                mTv_out_temperature.setText(IVICar.OutsideTemp.getTemp(rawValue, unit) + " " + IVICar.TemperatureUnit.getString(unit));
            }
        }
    }

    private void updateTotalDistance(float distance) {
        Logcat.d("d = " + distance);
        if (mTv_mileage == null)
            return;

        if (distance != IVICar.DISTANCE_UNKNOWN) {
            String totalMileage = (int) distance + " km";
            Logcat.d(totalMileage);
            mTv_mileage.setText(totalMileage);
        } else {
            mTv_mileage.setText("--");
        }
    }

    private void updateRemainFuel(float remainFuel) {
        mIv_remain_fuel.setImageResource(R.drawable.remain_fuel);

        mTv_remain_fuel.setVisibility(View.VISIBLE);
        mTv_remain_fuel2.setText(R.string.left_oil);

        if (remainFuel == IVICar.FUEL_L_UNKNOWN) {
            mTv_remain_fuel.setText("--");
        } else if (remainFuel == IVICar.FUEL_L_LOW) {
            mTv_remain_fuel2.setText(R.string.state_fuel_low);
            mIv_remain_fuel.setImageResource(R.drawable.remain_fuel2);
            mTv_remain_fuel.setVisibility(View.GONE);
        } else {
            mTv_remain_fuel.setText(remainFuel + " L");
        }
    }

    private void updateBatteryVoltage(float voltage) {
        if (mTv_battery_voltage == null) {
            return;
        }

        if (voltage == IVICar.BATTERY_VOLTAGE_UNKNOWN) {
            mTv_battery_voltage.setText("--");
        } else {
            String s = String.format("%.2f", voltage);
            mTv_battery_voltage.setText(s + " V");
        }
    }

    private void updateMotorSpeed(float speed) {
        if (mTv_motor_speed == null) {
            return;
        }

        if (speed == IVICar.RPM_UNKNOWN) {
            mTv_motor_speed.setText("--");
        } else {
            mTv_motor_speed.setText((int) speed + " RPM");
        }
    }

    private void updateCarSpeed(float speed) {
        if (mTv_speed == null) {
            return;
        }

        if (speed == IVICar.SPEED_UNKNOWN) {
            mTv_speed.setText("--");
        } else {
            String s = String.format("%.2f", speed);
            mTv_speed.setText(s + " km/h");
        }
    }

    private void updateCleanWater(float value) {
        if (mTv_lean_water == null || mIv_lean_water == null) {
            return;
        }

        if (value == IVICar.CLEAN_WATER_OK) {
            mTv_lean_water.setText(R.string.state_ok);
            mIv_lean_water.setImageResource(R.drawable.lean_water);
        } else if (value == IVICar.CLEAN_WATER_LOW) {
            mTv_lean_water.setText(R.string.state_low);
            mIv_lean_water.setImageResource(R.drawable.lean_water2);
        } else {
            mTv_lean_water.setText("--");
            mIv_lean_water.setImageResource(R.drawable.lean_water);
        }
    }

    private void updateBelt(float state) {
        if (mTv_seat_belt == null || mIv_seat_belt == null) {
            return;
        }

        if (state == IVICar.SEAT_BELT_OK) {
            mTv_seat_belt.setText(R.string.state_ok);
            mIv_seat_belt.setImageResource(R.drawable.seat_belt);
        } else if (state == IVICar.SEAT_BELT_RELEASE) {
            mTv_seat_belt.setText(R.string.state_belt_off);
            mIv_seat_belt.setImageResource(R.drawable.seat_belt2);
        } else {
            mTv_seat_belt.setText("--");
            mIv_seat_belt.setImageResource(R.drawable.seat_belt);
        }
    }

    private void updateHandbrake(int status) {
        switch (status) {
            case IVICar.Handbrake.Status.RELEASE:
                mTv_brake_state.setText(R.string.state_brake_off2);
                mIv_brake_state.setImageResource(R.drawable.brake_state);
                mTv_brake_state2.setVisibility(View.VISIBLE);
                mIv_brake_state.setVisibility(View.VISIBLE);
                mTv_brake_state.setVisibility(View.VISIBLE);
                break;

            case IVICar.Handbrake.Status.HOLD:
                mTv_brake_state.setText(R.string.state_brake_hold);
                mIv_brake_state.setImageResource(R.drawable.brake_state2);
                mTv_brake_state2.setVisibility(View.VISIBLE);
                mIv_brake_state.setVisibility(View.VISIBLE);
                mTv_brake_state.setVisibility(View.VISIBLE);
                break;

            case IVICar.Handbrake.Status.UNKNOWN:
                mTv_brake_state2.setVisibility(View.INVISIBLE);
                mIv_brake_state.setVisibility(View.INVISIBLE);
                mTv_brake_state.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 刷新车门控件
     */
    public void updateDoorImage(CarManager car,
                                       ImageView FL, ImageView FR,
                                       ImageView RL, ImageView RR,
                                       ImageView trunk, ImageView hoot) {
        if (car == null) {
            return;
        }

        if (FL != null) {
            FL.setVisibility(car.isDoorOpen(IVICar.Door.Id.FRONT_LEFT) ?
                    View.VISIBLE : View.INVISIBLE);
        }

        if (FR != null) {
            FR.setVisibility(car.isDoorOpen(IVICar.Door.Id.FRONT_RIGHT) ?
                    View.VISIBLE : View.INVISIBLE);
        }

        if (RL != null) {
            RL.setVisibility(car.isDoorOpen(IVICar.Door.Id.REAR_LEFT) ?
                    View.VISIBLE : View.INVISIBLE);
        }

        if (RR != null) {
            RR.setVisibility(car.isDoorOpen(IVICar.Door.Id.REAR_RIGHT) ?
                    View.VISIBLE : View.INVISIBLE);
        }

        // 后备箱
        if (trunk != null) {
            trunk.setVisibility(car.isDoorOpen(IVICar.Door.Id.TRUNK) ?
                    View.VISIBLE : View.INVISIBLE);
        }

        // 发动机盖
        if (hoot != null) {
            hoot.setVisibility(car.isDoorOpen(IVICar.Door.Id.HOOT) ?
                    View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * 更新车门提示文字
     *
     * @param textView    控件
     * @param context     上下文
     * @param show        没有车门打开的时候，要不要显示文字框
     * @param bgNormalId  车门都关闭时的文字框背景图
     * @param imvNormalId 车门都关闭时的文字框左侧图案
     * @param bgErrorId   车门都未关时的文字框背景图
     * @param imvErrorId  车门都未关时的文字框左侧图案
     */
    public void updateDoorText(TextView textView, Context context, boolean show,
                                      int bgNormalId, int imvNormalId, int bgErrorId, int imvErrorId) {
        Logcat.d("updateDoorText ,  textView " + textView == null ? "NO" : "YES"
                + " , context " + context == null ? "NO" : "YES"
                + " , bgNormalId = " + bgNormalId
                + " , imvNormalId = " + imvNormalId
                + " , bgErrorId = " + bgErrorId
                + " , imvErrorId = " + imvErrorId);
        if (textView != null) {
            String text = getDoorMsg();

            if (show && context != null) {
                Drawable drawableStart = null;
                if (text != null && !text.equals("")) {
                    textView.setText(text);
                    textView.setVisibility(View.VISIBLE);

                    if (bgErrorId > 0) {
                        textView.setBackground(context.getResources().getDrawable(bgErrorId));
                    }

                    if (imvErrorId > 0) {
                        drawableStart = context.getResources().getDrawable(imvErrorId);
                    }
                } else {
                    textView.setText(context.getResources().getString(R.string.all_door_closed));
                    textView.setVisibility(View.VISIBLE);

                    if (bgNormalId > 0) {
                        textView.setBackground(context.getResources().getDrawable(bgNormalId));
                    }

                    if (imvNormalId > 0) {
                        drawableStart = context.getResources().getDrawable(imvNormalId);
                    }
                }
                if (drawableStart != null) {
                    drawableStart.setBounds(0, 0, drawableStart.getMinimumWidth(), drawableStart.getMinimumHeight());
                    textView.setCompoundDrawablesRelative(drawableStart, null, null, null);
                }
            } else {
                if (text != null && !text.equals("")) {
                    textView.setText(text);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 压入一个开着的车门提示，打开车门的时候调用
     *
     * @param id     当前是哪个车门是开着的
     * @param prompt 页面提示消息
     */
    public void pushDoorInfo(int id, String prompt) {
        DoorInfo doorInfo = new DoorInfo(id, prompt);
        if (!mDoorInfos.contains(doorInfo)) {
            mDoorInfos.add(doorInfo); // 压入最后
        }
    }

    /**
     * remove关闭掉的车门提示，车门状态是关的时候调用
     */
    public void removeDoorInfo(int id) {
        DoorInfo doorInfo = new DoorInfo(id, "");
        if (mDoorInfos.contains(doorInfo)) {
            mDoorInfos.remove(doorInfo);
        }
    }

    /**
     * 获取当前的车门提示文字信息
     */
    public String getDoorMsg() {
        if (mDoorInfos.size() > 0) {
            return mDoorInfos.get(mDoorInfos.size() - 1).mValue;
        }
        return "";
    }

    /**
     * 获取车门描述文字
     */
    public void updateDoorMessage(CarManager car, Context context) {
        if (car == null || context == null) {
            return;
        }

        if (car.isDoorOpen(IVICar.Door.Id.FRONT_LEFT)) {
            pushDoorInfo(IVICar.Door.Id.FRONT_LEFT, context.getString(R.string.front_left_door_open));
        } else {
            removeDoorInfo(IVICar.Door.Id.FRONT_LEFT);
        }

        if (car.isDoorOpen(IVICar.Door.Id.FRONT_RIGHT)) {
            pushDoorInfo(IVICar.Door.Id.FRONT_RIGHT, context.getString(R.string.front_right_door_open));
        } else {
            removeDoorInfo(IVICar.Door.Id.FRONT_RIGHT);
        }

        if (car.isDoorOpen(IVICar.Door.Id.REAR_LEFT)) {
            pushDoorInfo(IVICar.Door.Id.REAR_LEFT, context.getString(R.string.rear_left_door_open));
        } else {
            removeDoorInfo(IVICar.Door.Id.REAR_LEFT);
        }

        if (car.isDoorOpen(IVICar.Door.Id.REAR_RIGHT)) {
            pushDoorInfo(IVICar.Door.Id.REAR_RIGHT, context.getString(R.string.rear_right_door_open));
        } else {
            removeDoorInfo(IVICar.Door.Id.REAR_RIGHT);
        }

        if (car.isDoorOpen(IVICar.Door.Id.TRUNK)) {
            pushDoorInfo(IVICar.Door.Id.TRUNK, context.getString(R.string.trunk_door_open));
        } else {
            removeDoorInfo(IVICar.Door.Id.TRUNK);
        }

        if (car.isDoorOpen(IVICar.Door.Id.HOOT)) {
            pushDoorInfo(IVICar.Door.Id.HOOT, context.getString(R.string.trunk_door_open));
        } else {
            removeDoorInfo(IVICar.Door.Id.HOOT);
        }
    }
}
