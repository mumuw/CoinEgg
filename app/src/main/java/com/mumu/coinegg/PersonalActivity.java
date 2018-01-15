package com.mumu.coinegg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mumu.coinegg.bean.TradeListResult;
import com.mumu.coinegg.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;

public class PersonalActivity extends AppCompatActivity {
    TextView hello;
    String[] names;
    private List<TradeListResult> mDatas;
    RecyclerView mRecyclerView;
    HomeAdapter homeAdapter;
    private long nonce = 170808230;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        names = getResources().getStringArray(R.array.coin);
        mDatas = new ArrayList<>();
        findViewById(R.id.id_recyclerview);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        //设置布局管理器
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(homeAdapter = new HomeAdapter());
        request();
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    PersonalActivity.this).inflate(R.layout.item_home, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mDatas.get(position).getCoin());
            holder.money.setText(mDatas.get(position).getData().get(0).getStatus());
            holder.all.setText(mDatas.get(position).getData().get(0).getType());
            holder.count.setText(mDatas.get(position).getData().get(0).getPrice()
            );
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            TextView money;
            TextView all;
            TextView count;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
                money = (TextView) view.findViewById(R.id.money);
                all = (TextView) view.findViewById(R.id.all);
                count = (TextView) view.findViewById(R.id.count);
            }
        }
    }


    String message;


    private void request() {
        String url = "https://api.coinegg.com/api/v1/trade_list/";

        for (final String name : names) {
            HashMap<String, String> map = new HashMap<>();
            map.put("key", PUBLIC_KEY);
            map.put("nonce", "170808230321");
            map.put("since", 0 + "");
            map.put("coin", name.toLowerCase());
            map.put("type", "open");
            String sin = getSignature(map);
            map.put("signature", sin);
            Log.e("linwl", "map....." + map.toString());
            OkHttpUtils
                    .post()
                    .url(url)
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            TradeListResult tradeListResult = JsonUtil.Gson2Class(response, TradeListResult.class);
                            if (tradeListResult.getData() != null && tradeListResult.getData().size() > 0) {
                                tradeListResult.setCoin(name);
                                mDatas.add(tradeListResult);
                                homeAdapter.notifyDataSetChanged();
                            }
                        }
                    });

        }
    }


    private String getSignature(Map name) {
        String encryptKey;
        StringBuilder data = new StringBuilder();
        for (Object key : name.keySet()) {
            data.append(key + "=" + name.get(key) + "&");
        }
        data.deleteCharAt(data.length() - 1);
        try {
            encryptKey = MD5Encrypt.getMessageDigest(PRIVATE_KEY);
            System.out.println(" encryptKey : " + encryptKey);
            String str = sha256_HMAC(data.toString(), encryptKey);
            return str;
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 利用java原生的摘要实现SHA256加密
     *
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static final String PUBLIC_KEY = "";
    public static final String PRIVATE_KEY = "";

    public static void main(String[] args) {

    }

    private static String sha256_HMAC(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;
    }

    /**
     * 将加密后的字节数组转换成字符串 * * @param b *字节数组 * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

}


