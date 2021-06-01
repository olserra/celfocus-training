package com.training.springbootbuyitem.error;


import com.training.springbootbuyitem.constant.BuyItemConstant;

public class StockNotAvailableException extends RuntimeException {

    public StockNotAvailableException(String name) {
        this(String.format(BuyItemConstant.STOCK_NOT_AVAILABLE_MSG, name));
    }

    private StockNotAvailableException(String message) {
        super(message);
    }

}
