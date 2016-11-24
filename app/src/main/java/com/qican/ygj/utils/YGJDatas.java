package com.qican.ygj.utils;

import com.qican.ygj.bean.Pond;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/20 0020.
 */
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
}
