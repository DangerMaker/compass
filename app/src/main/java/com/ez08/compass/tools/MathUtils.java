package com.ez08.compass.tools;

import java.text.DecimalFormat;

public class MathUtils {

    public static String getFormatPrice(int value, int decm) {
        return formatNum(getPriceInt2Float(value, decm),decm);
    }

    public static String getFormatUnit(double num) {
        String end;
        double abs_profit = Math.abs(num);
        if (abs_profit > 100000000) {
            end = "亿";
            num = num / 100000000D;
        } else if (abs_profit > 10000) {
            end = "万";
            num = num / 10000D;
        } else {
            end = "";
        }

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num) + end;
    }

    private static float getPriceInt2Float(int price, int exp) {
        switch (exp) {
            case 0:
                return (float) (price / 0.01);
            case 1:
                return (float) (price / 0.1);
            case 2:
                return (float) (price / 1.0);
            case 3:
                return (float) (price / 10.0);
            case 4:
                return (float) (price / 100.0);
            case 5:
                return (float) (price / 1000.0);
            case 6:
                return (float) (price / 10000.0);
            case 7:
                return (float) (price / 100000.0);
            default:
                return 0;
        }
    }

    // value num 进位
    public static String formatNum(float value, int exp) {
        String result;
        DecimalFormat df;
        switch (exp) {
            case 3:
                df = new DecimalFormat("0.0");
                result = df.format(value);
                break;
            case 4:
                df = new DecimalFormat("0.00");
                result = df.format(value);
                break;
            case 5:
                df = new DecimalFormat("0.000");
                result = df.format(value);
                break;
            case 6:
                df = new DecimalFormat("0.0000");
                result = df.format(value);
                break;
            case 7:
                df = new DecimalFormat("0.00000");
                result = df.format(value);
                break;
            default:
                return "";

        }
        return result;
    }
}
