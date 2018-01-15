package com.mumu.coinegg.bean;

import java.util.List;

/**
 * Created by lin on 2018/1/14.
 */

public class TradeListResult extends BaseResult{
    private List<Result> data;
    private String coin = "";
    public class Result{
        String id;
        String coin;
        String datetime;
        String type;
        String status;

        String price;
        String amount_original;
        String amount_outstanding;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAmount_original() {
            return amount_original;
        }

        public void setAmount_original(String amount_original) {
            this.amount_original = amount_original;
        }

        public String getAmount_outstanding() {
            return amount_outstanding;
        }

        public void setAmount_outstanding(String amount_outstanding) {
            this.amount_outstanding = amount_outstanding;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "id='" + id + '\'' +
                    ", coin='" + coin + '\'' +
                    ", datetime='" + datetime + '\'' +
                    ", type='" + type + '\'' +
                    ", status='" + status + '\'' +
                    ", price='" + price + '\'' +
                    ", amount_original='" + amount_original + '\'' +
                    ", amount_outstanding='" + amount_outstanding + '\'' +
                    '}';
        }
    }

    public List<Result> getData() {
        return data;
    }

    public void setData(List<Result> data) {
        this.data = data;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    @Override
    public String toString() {
        return "TradeListResult{" +
                "data=" + data +
                '}';
    }
}
