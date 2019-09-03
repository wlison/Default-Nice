package com.wlison.view.nice.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.utils.CalendarUtil;
import com.othershe.calendarview.weiget.CalendarView;
import com.wlison.view.nice.R;
import com.wlison.view.nice.base.BaseActivity;
import com.wlison.view.nicelibrary.widget.date.NiceCalenderView;
import com.wlison.view.nicelibrary.widget.date.NiceWeekView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: Simon
 * Date：5/17/2019 10:22
 * Desc:
 */
public class NiceCalenderActivity extends BaseActivity {

    @BindView(R.id.text)
    TextView mTxtDate;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.week)
    NiceWeekView week;
    @BindView(R.id.nice_calendar)
    CalendarView mNiceCalendarView;
    @BindView(R.id.calender)
    NiceCalenderView mCalendarView;

    @Override
    protected int getLayoutId() {
        return R.layout.nice_calender_activity;
    }

    @Override
    protected String setToolBarTitle() {
        return "NiceCalender";
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initCalendar();
        // 设置已选的日期
//        mCalendarView.setSelectDate(initDataSelect());
        // 指定显示的日期, 如当前月的下个月
        Calendar calendar = mCalendarView.getCalendar();
//        calendar.add(Calendar.MONTH, 1);
        mCalendarView.setCalendar(calendar);
        // 设置字体
        mCalendarView.setTypeface(Typeface.SERIF);
        // 设置是否能够改变日期状态
        mCalendarView.setChangeDateStatus(true);
        // 设置是否能够点击
        mCalendarView.setClickable(true);
        // 设置是否多选
        mCalendarView.setMultipleChoice(true);

//        setCurDate();

        mCalendarView.setOnNiceDateChangeListener(new NiceCalenderView.OnNiceDateChangeListener() {
            @Override
            public void onNiceSelectedDayChange(NiceCalenderView view, boolean select, int year, int month, int day) {
                if(select){
//                    ToastUtils.showLongToast("选中了：" + year + "年" + (month + 1) + "月" + day + "日");
                    Toast.makeText(getApplicationContext()
                            , "选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
                }else{
//                    ToastUtils.showLongToast("取消选中了：" + year + "年" + (month + 1) + "月" + day + "日");
                    Toast.makeText(getApplicationContext()
                            , "取消选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCalendarView.setOnNiceDataClickListener(new NiceCalenderView.OnNiceDataClickListener() {
            @Override
            public void onNiceDataClick(NiceCalenderView view, int year, int month, int day) {

            }
        });
    }

    private void initCalendar() {
        int[] cDate = CalendarUtil.getCurrentDate();
        HashMap<String, String> map = new HashMap<>();
        map.put("2017.10.30", "qaz");
        map.put("2017.10.1", "wsx");
        map.put("2017.11.12", "yhn");
        map.put("2017.9.15", "edc");
        map.put("2017.11.6", "rfv");
        map.put("2017.11.11", "tgb");
        mNiceCalendarView
//                .setSpecifyMap(map)
                .setStartEndDate("2016.1", "2028.12")  // 开始与结束年分
                .setDisableStartEndDate("2016.10.10", "2028.10.10") // 不能选择的年分
                .setInitDate(cDate[0] + "." + cDate[1])
                .setSingleDate(cDate[0] + "." + cDate[1] + "." + cDate[2])
                .init();

//       .setOnCalendarViewAdapter(R.layout.item_layout, new CalendarViewAdapter() {
//            @Override
//            public TextView[] convertView(View view, DateBean date) {
//                TextView solarDay = (TextView) view.findViewById(R.id.solar_day);
//                TextView lunarDay = (TextView) view.findViewById(R.id.lunar_day);
//                return new TextView[]{solarDay, lunarDay};
//            }
//        }).init();

        mTxtDate.setText("当前选中的日期：" + cDate[0] + "年" + cDate[1] + "月" + cDate[2] + "日");

        mNiceCalendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
//                title.setText(date[0] + "年" + date[1] + "月");
            }
        });

        mNiceCalendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
                if (date.getType() == 1) {
                    mTxtDate.setText("当前选中的日期：" + date.getSolar()[0] + "年" + date.getSolar()[1] + "月" + date.getSolar()[2] + "日");
                }
            }
        });
    }


    @OnClick({R.id.btn_last, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_last:
                mNiceCalendarView.lastMonth();
//                mCalendarView.lastMonth();
//                setCurDate();
                break;
            case R.id.btn_next:
                mNiceCalendarView.nextMonth();
//                mCalendarView.nextMonth();
//                setCurDate();
                break;
        }
    }

    private List<String> initDataSelect() {
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat sdf = new SimpleDateFormat(mCalendarView.getDateFormatPattern(), Locale.CHINA);
        sdf.format(calendar.getTime());
        dates.add(sdf.format(calendar.getTime()));
        return dates;
    }

    private void setCurDate() {
        mTxtDate.setText(mCalendarView.getYear() + "年" + (mCalendarView.getMonth() + 1) + "月");
    }

}
