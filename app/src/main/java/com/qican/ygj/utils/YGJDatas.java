/**
 * 与服务器交互的类，用于查询各种信息
 */
package com.qican.ygj.utils;

import android.content.Context;

import com.qican.ygj.bean.Msg;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.bean.User;
import com.qican.ygj.listener.BeanCallBack;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class YGJDatas {

    public static List<Pond> getPondsDatas() {
        List<Pond> mData = new ArrayList<>();
        Pond pond1 = new Pond();
        pond1.setImgUrl("http://img5.imgtn.bdimg.com/it/u=1481365631,1136133954&fm=21&gp=0.jpg");
        pond1.setName("池塘1");
        mData.add(pond1);

        Pond pond2 = new Pond();
        pond2.setImgUrl("http://img4.imgtn.bdimg.com/it/u=3849379134,1749767660&fm=21&gp=0.jpg");
        pond2.setName("池塘2");
        mData.add(pond2);

        Pond pond3 = new Pond();
        pond3.setImgUrl("http://img5.imgtn.bdimg.com/it/u=1432240924,2436835975&fm=21&gp=0.jpg");
        pond3.setName("池塘3");
        mData.add(pond3);
        return mData;
    }

    public static List<Msg> getMsgsDatas() {
        List<Msg> mData = new ArrayList<>();

        Msg msg = new Msg();
        msg.setMsg("池塘1机位1出现鱼群缺氧，请及时处理！");
        msg.setTime("下午5:30");
        msg.setImgUrl("http://img5.imgtn.bdimg.com/it/u=1481365631,1136133954&fm=21&gp=0.jpg");
        mData.add(msg);

        Msg msg1 = new Msg();
        msg1.setMsg("大明湖机位20出现鱼群缺氧，请及时处理！");
        msg1.setTime("下午10:30");
        msg1.setImgUrl("http://m2.quanjing.com/2m/age_foto29/llp-gig-0010359.jpg");
        mData.add(msg1);

        Msg msg2 = new Msg();
        msg2.setMsg("长寿湖1段机位5出现鱼群缺氧，请及时处理！");
        msg2.setTime("下午7:30");
        msg2.setImgUrl("http://pic17.nipic.com/20111109/620919_174148494141_2.jpg");
        mData.add(msg2);

        return mData;
    }

    /**
     * 更新用户信息到本地
     *
     * @param context
     */
    public static void updateInfoFromService(Context context) {
        final CommonTools myTool = new CommonTools(context);

        String url = ConstantValue.SERVICE_ADDRESS + "selectUser";

        // 更新用户信息到本地
        OkHttpUtils
                .post()
                .url(url)
                .addParams("userId", myTool.getUserId())
                .build()
                .execute(new BeanCallBack<User>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        myTool.showInfo("信息更新失败！");
                    }

                    @Override
                    public void onResponse(User user, int id) {
                        myTool.setUserHeadURL(user.getHeadImageUrl());
                        myTool.setAutograph(user.getSignature());
                        myTool.setNickName(user.getUserName());
                        myTool.setUserSex(user.getUserSex());
                    }
                });
    }
}
