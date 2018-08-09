package cn.hyperchain.application.common.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * @author sunligang
 */
public class Logger {

    public static class Builder {
        public static Logger getLogger(Class<?> clazz) {
            return new Logger(clazz);
        }
    }

    private static final int COMPLETE_DISPLAY_PACKAGE_NAME = 2;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private StringBuilder simpleClazzName = new StringBuilder();

    private Calendar calendar = Calendar.getInstance();

    Logger(Class<?> clazz) {
        String[] packageNameArray = clazz.getName().split("\\.");
        for (int i = 0; i < packageNameArray.length; i++) {
            if (i < packageNameArray.length - COMPLETE_DISPLAY_PACKAGE_NAME) {
                simpleClazzName.append(packageNameArray[i], 0, 1);
            } else {
                simpleClazzName.append(packageNameArray[i]);
            }
            if (i < packageNameArray.length - 1) {
                simpleClazzName.append(".");
            }
        }
    }

    public void info(Object info) {
        System.out.println(simpleDateFormat.format(calendar.getTime()) + "[" + simpleClazzName + "] info :" + info);
    }

    public void info(Object... info) {
        System.out.print(simpleDateFormat.format(calendar.getTime()) + "[" + simpleClazzName + "] info :");
        Arrays.stream(info).forEach(e -> {
            System.out.print(" " + e);
        });
        System.out.println();
    }


    public void blackLine() {
        System.out.println();
    }

    public void err(Object info) {
        System.err.println(simpleDateFormat.format(calendar.getTime()) + "[" + simpleClazzName + "]  err :" + info);
    }

    public void warn(Object info) {
        System.err.println(simpleDateFormat.format(calendar.getTime()) + "[" + simpleClazzName + "] warm :" + info);
    }

}
