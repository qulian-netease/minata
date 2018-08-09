package cn.hyperchain.application.common.response;


import cn.hyperchain.application.common.constant.Code;

/**
 * Created by superlee on 2017/11/7.
 * baseResult 工程方法
 */
@SuppressWarnings("unchecked")
public final class BaseResultFactory {


    public static BaseResult produceEmptyResult(Code code) {
        return new BaseResult(code);
    }

    public static BaseResult produceEmptyResult(int codeInt, String msg) {
        return new BaseResult(codeInt, msg);
    }

    public static BaseResult produceResult(int codeInt, String msg, Object data) {
        return new BaseResult(codeInt, msg, data);
    }

    public static BaseResult produceResult(Code code, Object data) {
        return new BaseResult(code.getCode(), code.getMsg(), data);
    }

    /**
     * 构建成功返回的数据（应该比较常用）
     * @param data
     * @return
     */
    public static BaseResult creatSuccessReult(Object data){
        BaseResult result = new BaseResult(Code.SUCCESS);
        result.setData(data);
        return result;
    }

    public static BaseResult creatSuccessReult(){
        return new BaseResult(Code.SUCCESS);
    }

}
