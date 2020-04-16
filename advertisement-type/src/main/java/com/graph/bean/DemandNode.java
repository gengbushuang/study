package com.graph.bean;

import com.Util;
import com.utils.SortUtil;

import java.util.ArrayList;
import java.util.List;

public class DemandNode {
    private long id;

    private long adId;

    private float demandCnt;

    private SupplyNodeslice supplys;

    private float maxSupplyCnt;

    private float totalRemain;

    private float servingRate;

    private List<Point> keyPoints;

    public DemandNode(long id, float demandCnt, SupplyNodeslice supplyNodeslice) {
        this.id = id;
        this.demandCnt = demandCnt;
        this.supplys = supplyNodeslice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public float getDemandCnt() {
        return demandCnt;
    }

    public void setDemandCnt(float demandCnt) {
        this.demandCnt = demandCnt;
    }

    public SupplyNodeslice getSupplys() {
        return supplys;
    }

    public void setSupplys(SupplyNodeslice supplys) {
        this.supplys = supplys;
    }

    public float getMaxSupplyCnt() {
        return maxSupplyCnt;
    }

    public void setMaxSupplyCnt(float maxSupplyCnt) {
        this.maxSupplyCnt = maxSupplyCnt;
    }

    public float getTotalRemain() {
        return totalRemain;
    }

    public void setTotalRemain(float totalRemain) {
        this.totalRemain = totalRemain;
    }

    public float getServingRate() {
        return servingRate;
    }

    public void setServingRate(float servingRate) {
        this.servingRate = servingRate;
    }

    public void totalRemain() {
        long totalRemain_ = 0;
        long maxSupplyCnt_ = 0;
        for (int i = 0; i < supplys.len(); i++) {
            SupplyNode supplyNode = supplys.get(i);
            totalRemain_ += supplyNode.getRemainCnt();
            maxSupplyCnt_ += supplyNode.getSupplyCnt();
        }
        this.totalRemain = totalRemain_;
        this.maxSupplyCnt = maxSupplyCnt_;
    }

    public void resolveServingRate() {
        this.filterZeroRemains();
        SortUtil.sort(supplys);
        this.totalRemain();
        this.generatePoints();
        this.servingRate = Util.piecewiseResolve(keyPoints.toArray(new Point[0]),demandCnt);
        this.updateRemains();
    }

    public void filterZeroRemains() {
        SupplyNodeslice supplys_ = new SupplyNodeslice(supplys.len());
        for (int i = 0; i < supplys.len(); i++) {
            if (supplys.get(i).getRemainCnt() > 0) {
                supplys_.add(supplys.get(i));
            }
        }
        this.supplys = supplys_;
    }

    public void generatePoints() {
        float last_rate = 0.0F;
        float demand_sum = 0.0F;
        float y = 0.0F;
        this.keyPoints = new ArrayList<>();
        this.keyPoints.add(new Point(0.0F,0.0F));
        for (int i = 0; i < supplys.len(); i++) {
            float rate = supplys.get(i).getRemainCnt() / supplys.get(i).getSupplyCnt();
            float incr_rate = rate - last_rate;
            y += incr_rate * (maxSupplyCnt - demand_sum);
            this.keyPoints.add(new Point(rate, y));
            demand_sum += supplys.get(i).getSupplyCnt();
            last_rate = rate;
        }
    }

    public void updateRemains(){
        for (int i = 0; i < supplys.len(); i++) {
            supplys.get(i).updateRemainCnt(servingRate);
        }
    }

    @Override
    public String toString() {
        return "{\"id\":" + id + ",\"adId\":" + adId + ",\"demandCnt\":" + demandCnt +
                ",\"supplys\":" + supplys +
                ",\"maxSupplyCnt\":" + maxSupplyCnt +
                ",\"totalRemain\":" + totalRemain +
                ",\"servingRate\":" + servingRate +
                '}';
    }
}
