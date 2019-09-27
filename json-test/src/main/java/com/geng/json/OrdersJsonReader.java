package com.geng.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.common.io.CharSource;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.geng.json.ObjectJsonUtil.*;

public class OrdersJsonReader extends AbstractJsonReader {

    protected OrdersJsonReader(ObjectJsonFactory factory) {
        super(factory);
    }

    public Orders readBidRequest(CharSequence chars) throws IOException {
        return readBidRequest(CharSource.wrap(chars).openStream());
    }

    public Orders readBidRequest(Reader reader) throws IOException {
        return readBidRequest(factory().getJsonFactory().createParser(reader));
    }

    public final Orders readBidRequest(JsonParser par) throws IOException {
        if (emptyToNull(par)) {
            return null;
        }
        Orders req = new Orders();
        for (startObject(par); endObject(par); par.nextToken()) {
            String fieldName = getCurrentName(par);
            if (par.nextToken() != JsonToken.VALUE_NULL) {
                readBidRequestField(par, req, fieldName);
            }
        }
        return req;
    }


    protected void readBidRequestField(JsonParser par, Orders req, String fieldName) throws IOException {
        switch (fieldName) {
            case "orderId":
                req.setOrderId(par.getIntValue());
                break;
            case "cId":
                req.setCustId(par.getIntValue());
                break;
            case "name":
                req.setName(par.getText());
                break;
            case "startTime":
                String text = par.getText();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    req.setStartTime(dateFormat.parse(text));
                } catch (ParseException e) {
                    throw new IOException(e);
                }
                break;
            case "endTime":
                String text1 = par.getText();
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    req.setEndTime(dateFormat1.parse(text1));
                } catch (ParseException e) {
                    throw new IOException(e);
                }
                break;
            case "userDebtId":
                req.setUserDebtId(par.getIntValue());
                break;
            case "salespersonId":
                req.setSalespersonId(par.getIntValue());
                break;
            case "saleDebtId":
                req.setSaleDebtId(par.getIntValue());
                break;
            default:
                readOther(req, par, fieldName);
        }
    }
}
