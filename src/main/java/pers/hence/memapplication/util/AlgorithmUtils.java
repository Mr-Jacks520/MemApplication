package pers.hence.memapplication.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/29 14:29
 * @description 算法组件
 */
public class AlgorithmUtils {

    /**
     * 日期偏移量定义
     */
    private static final int[] OFFSETS;

    /**
     * 日期格式
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    static {
        OFFSETS = new int[]{1, 2, 4, 7, 15};
    }

    /**
     * 艾宾浩斯算法-根据当前日期生成复习时间列表
     * @param date 当前日期
     * @return 复习时间列表 json
     */
    public static String ebenhausCurve(String date) {
        Date parse = DateUtil.parse(date, DATE_FORMAT);
        List<String> timePoints = new ArrayList<>(OFFSETS.length);
        for (int offset : OFFSETS) {
            DateTime timePoint = DateUtil.offset(parse, DateField.DAY_OF_MONTH, offset);
            timePoints.add(timePoint.toString(DATE_FORMAT));
        }
        Gson gson = new Gson();
        return gson.toJson(timePoints);
    }

    /**
     * 获取下次复习时间
     * @param date 当前日期
     * @return 下次复习日期
     */
    public static String getNextReviewTime(String date) {
        Date parse = DateUtil.parse(date, DATE_FORMAT);
        return DateUtil.offset(parse, DateField.DAY_OF_MONTH, OFFSETS[0]).toString(DATE_FORMAT);
    }
}
