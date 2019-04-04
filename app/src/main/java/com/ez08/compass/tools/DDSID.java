package com.ez08.compass.tools;

/**
 * 判断是否是指数的重要class
 */
public class DDSID {
	/*
	#define PID_DDS_PY_CN_LIST_20160826			(6010)
	#define PID_DDS_PY_CN_LIST					(6011)
	*/

    public final static int ID_OK = 100;// 心跳包
    public final static int ID_MarketDate = 6000;// 行情日期
    //public final static int ID_CodeList = 6010;// 码表
    public final static int ID_CodeList = 6011;// 新码表，增加名称长度
    public final static int ID_OpenInfo = 6015;// 基本信息
    public final static int ID_HQ_All = 6020;// 全量快照
    public final static int ID_HQ_Push = 6030;// 增量快照
    public final static int ID_HQ_DYN = 6040;// 分时图补数据包，资金等等
    public final static int ID_HQ_Price = 6080;// 分价图补数据包

    // 股票种类
    public final static int _STOCK_CATEGORY_OTHER = 0;
    //
    public final static int _STOCK_CATEGORY_RAIN_000001 = 50;
    public final static int _STOCK_CATEGORY_RAIN_399001 = 51;
    //
    public final static int _STOCK_CATEGORY_ZNZ_0 = 100;
    public final static int _STOCK_CATEGORY_ZNZ_0A01 = 101;
    //
    public final static int _STOCK_CATEGORY_SH_Z_OTHER = 200;
    public final static int _STOCK_CATEGORY_SH_Z_A = 201;
    public final static int _STOCK_CATEGORY_SH_Z_B = 202;
    public final static int _STOCK_CATEGORY_SH_Z_J = 203;
    // 上证
    public final static int _STOCK_CATEGORY_SH_A = 301;
    public final static int _STOCK_CATEGORY_SH_B = 302;
    public final static int _STOCK_CATEGORY_SH_J = 303;
    public final static int _STOCK_CATEGORY_SH_G = 304;
    public final static int _STOCK_CATEGORY_SH_Q = 305;// 权证
    public final static int _STOCK_CATEGORY_SH_KJ = 306;// 配合开放式基金
    //
    public final static int _STOCK_CATEGORY_SZ_Z_OTHER = 400;
    public final static int _STOCK_CATEGORY_SZ_Z_A = 401;
    public final static int _STOCK_CATEGORY_SZ_Z_B = 402;
    public final static int _STOCK_CATEGORY_SZ_Z_J = 403;
    // 深证
    public final static int _STOCK_CATEGORY_SZ_A = 501;
    public final static int _STOCK_CATEGORY_SZ_B = 502;
    public final static int _STOCK_CATEGORY_SZ_J = 503;
    public final static int _STOCK_CATEGORY_SZ_G = 504;
    public final static int _STOCK_CATEGORY_SZ_KJ = 505;// 配合开放式基金
    public final static int _STOCK_CATEGORY_SZ_Q = 506;// 权证
    // 配合中小企业上市
    public final static int _STOCK_CATEGORY_SZ_C = 550;// 中小企业股，“创业板”的首拼音“C”标记
    public final static int _STOCK_CATEGORY_SZ_CY = 560;// 创业板
    public final static int _STOCK_CATEGORY_BOARD_Z = 600;// 板块指数
    //指南针指数
    public final static int _STOCK_CATEGORY_Z_SK = 901;// 特殊指数

    public final static String markBoardList = "BR01B04001,BR01B04002,BR01B04003,BR01B04004,BR01B04005,BR01B04006,"
            + "BR01B04007,BR01B04008,BR01B04009,BR01B04010,BR01B04011,BR01B04012"
            + "BR01B04013,BR01B04014,BR01B04015,BR01B04016,BR01B04017,BR01B04018,BR010Z";

    //zhishu(指数和板块)
    public static boolean isZ(String szCode) {
        int type = GetStockCategory(szCode);
        if ((type >= 200 && type <= 203) || (type >= 400 && type <= 403) || type == _STOCK_CATEGORY_BOARD_Z || type == _STOCK_CATEGORY_Z_SK) {
            return true;
        } else
            return false;
    }

    public static boolean hasChangeList(String szCode){
        if(isZ(szCode) &&  szCode.startsWith("BR01") && !markBoardList.contains(szCode)){
            return true;
        }
        return false;
    }

    public static boolean hasGlobalNews(String szCode){
        return isZ(szCode);
    }

    public static boolean hasCapital(String szCode){
        if (szCode.equals("SHHQ000001") || szCode.equals("SZHQ399001")) {
            return true;
        }

        if (!isZ(szCode)) {
            return true;
        }

        if (isZ(szCode) && szCode.startsWith("BR01")) {
            return true;
        }

        return false;
    }

    // 获取股票类别,包含市场标记的代码
    public static int GetStockCategory(String szCode) {
        if (szCode.startsWith("BR01")) {
            return _STOCK_CATEGORY_BOARD_Z;
        } else if (szCode.startsWith("Z_SK")) {
            return _STOCK_CATEGORY_Z_SK;
        } else if (szCode.startsWith("SHHQ")) {
            if (szCode.length() == 10) {
                String code = szCode.substring(4);
                // 特殊国债
                if ((code.equals("000696")) || (code.equals("000896")))
                    return _STOCK_CATEGORY_SH_G;
                // 指数
                if (code.startsWith("000")) {
                    if (code.equals("000002"))
                        return _STOCK_CATEGORY_SH_Z_A;
                    if (code.equals("000003"))
                        return _STOCK_CATEGORY_SH_Z_B;
                    if (code.equals("000011"))
                        return _STOCK_CATEGORY_SH_Z_J;
                    //
                    return _STOCK_CATEGORY_SH_Z_OTHER;
                }
                // 国债
                // 00/01 国债
                // 10/11 可转债
                // 12 企业债、金融债
                // 20 债券回购
                if (code.startsWith("001") || code.startsWith("002"))
                    return _STOCK_CATEGORY_SH_G;
                //
                if (code.startsWith("5")) {
                    // 基金？权证？
                    if (code.charAt(1) == '8')
                        return _STOCK_CATEGORY_SH_Q;
                    else if ((code.charAt(1) == '1') && (code.charAt(1) == '9'))
                        return _STOCK_CATEGORY_SH_KJ;
                    else
                        return _STOCK_CATEGORY_SH_J;
                }
                // A股
                // 600xxx 601xxx
                if ((code.charAt(0) == '6') && (code.charAt(1) == '0'))
                    return _STOCK_CATEGORY_SH_A;
                // B股 900xxx
                if ((code.charAt(0) == '9') && (code.charAt(1) == '0') && (code.charAt(2) == '0')) {
                    return _STOCK_CATEGORY_SH_B;
                }
                return _STOCK_CATEGORY_OTHER;
            } else
                return _STOCK_CATEGORY_OTHER;

        } else if (szCode.startsWith("SZHQ")) {
            if (szCode.length() == 10) {
                String code = szCode.substring(4);
                // 00-0000 00-9999 A股，
                // 000xxx一项和001696,001896有用，老A股
                // 002xxx中小企业板
                if ((code.charAt(0) == '0') && (code.charAt(1) == '0')) {
                    if (code.charAt(2) == '0')
                        return _STOCK_CATEGORY_SZ_A;
                    else if (code.charAt(2) == '1')
                        return _STOCK_CATEGORY_SZ_A;
                    else if (code.charAt(2) == '2') // 配合中小企业上市 add by sober
                        // 20040528
                        return _STOCK_CATEGORY_SZ_C;
                    else
                        return _STOCK_CATEGORY_OTHER;
                }
                // 权证
                if ((code.charAt(0) == '0') && (code.charAt(1) == '3'))
                    return _STOCK_CATEGORY_SZ_Q;
                // 20-0000 20-9999 B股 目前只有200000-200999有用
                if ((code.charAt(0) == '2') && (code.charAt(1) == '0'))
                    return _STOCK_CATEGORY_SZ_B;
                // 10XXXX, 国债现货
                // 11XXXX, 国债
                // 12XXXX, 转债
                // 13XXXX, 国债回购
                // 15XXXX,16XXXX, 开放式基金
                // 18XXXX, 基金
                if ((code.charAt(0) == '1')) {
                    if (code.charAt(1) == '0')
                        return _STOCK_CATEGORY_SZ_G;
                    if (code.charAt(1) == '1')
                        return _STOCK_CATEGORY_SZ_G;
                    if (code.charAt(1) == '2')
                        return _STOCK_CATEGORY_SZ_G;
                    if (code.charAt(1) == '3')
                        return _STOCK_CATEGORY_SZ_G;
                    if (code.charAt(1) == '5')
                        return _STOCK_CATEGORY_SZ_KJ;
                    if (code.charAt(1) == '6')
                        return _STOCK_CATEGORY_SZ_KJ;
                    if (code.charAt(1) == '8')
                        return _STOCK_CATEGORY_SZ_J;
                }
                // 30####，创业板
                if ((code.charAt(0) == '3') && (code.charAt(1) == '0'))
                    return _STOCK_CATEGORY_SZ_CY;
                // 399XXX 指数
                if ((code.charAt(0) == '3') && (code.charAt(1) == '9') && ((code.charAt(2) == '9') || (code
                        .charAt(2) == '5')/* add by sober 20041210 深交所发布成交量指数 */)) {
                    if (code.equals("399002") || code.equals("399107"))
                        return _STOCK_CATEGORY_SZ_Z_A;
                    if (code.equals("399003") || code.equals("399108"))
                        return _STOCK_CATEGORY_SZ_Z_B;
                    if (code.equals("399305"))
                        return _STOCK_CATEGORY_SZ_Z_J;
                    return _STOCK_CATEGORY_SZ_Z_OTHER;
                }
                return _STOCK_CATEGORY_OTHER;
            } else
                return _STOCK_CATEGORY_OTHER;
        } else {
            return _STOCK_CATEGORY_OTHER;
        }
    }
}
