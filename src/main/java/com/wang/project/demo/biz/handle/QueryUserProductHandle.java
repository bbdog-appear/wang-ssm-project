package com.wang.project.demo.biz.handle;

import com.google.common.util.concurrent.FutureCallback;
import com.wang.project.demo.entity.WcProductEO;
import com.wang.project.demo.entity.WcUserEO;
import com.wang.project.demo.vo.UserProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *      查询用户产品处理类
 * </p>
 *
 * @author wangcheng
 * @version Id：QueryUserProductHandle.java Date：2020/10/12 19:03 Version：1.0
 */
@Component
@Slf4j
public class QueryUserProductHandle {

    /**
     * 查询产品信息回调函数
     *
     * @param userProductVO 用户产品VO
     * @return 产品回调类
     */
    public FutureCallback<List<WcProductEO>> productInfoCallback(UserProductVO userProductVO){
        return new FutureCallback<List<WcProductEO>>() {
            @Override
            public void onSuccess(List<WcProductEO> wcProductEOS) {
                userProductVO.setProductCode(wcProductEOS.get(0).getProductCode());
                userProductVO.setProductName(wcProductEOS.get(0).getProductName());
                userProductVO.setProductNum(wcProductEOS.get(0).getProductNum());
                userProductVO.setInsertTime(wcProductEOS.get(0).getInsertTime());
                userProductVO.setUpdateTime(wcProductEOS.get(0).getUpdateTime());
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.error("guava线程池查询异常");
            }
        };
    }

    /**
     * 查询用户信息回调函数
     *
     * @param userProductVO 用户产品VO
     * @return 用户回调类
     */
    public FutureCallback<List<WcUserEO>> userInfoCallback(UserProductVO userProductVO){
//        return new FutureCallback<List<WcUserEO>>() {
//            @Override
//            public void onSuccess(List<WcUserEO> users) {
//                userProductVO.setCode(users.get(0).getCode());
//                userProductVO.setName(users.get(0).getName());
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//                log.error("guava线程池查询异常");
//            }
//        };
        // 另一种写法
        return new MyFutureCallback(userProductVO);
    }


}

class MyFutureCallback implements FutureCallback<List<WcUserEO>>{

    private UserProductVO userProductVO;

    public MyFutureCallback(UserProductVO userProductVO) {
        this.userProductVO = userProductVO;
    }

    @Override
    public void onSuccess(List<WcUserEO> users) {
        userProductVO.setCode(users.get(0).getCode());
        userProductVO.setName(users.get(0).getName());
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
