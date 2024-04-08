package org.adt.algorithms.data;

/**
 * @author gengbushuang
 * @date 2024/2/19 10:52
 */
public class FloodData {
    /**
     * 食物名称
     */
    private String floodName;
    /**
     * 总量
     */
    private int weight;
    /**
     * 卡路里
     */
    private int calorie;

    public String getFloodName() {
        return floodName;
    }

    public void setFloodName(String floodName) {
        this.floodName = floodName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public double calorieAvg() {
        return (calorie / weight);
    }

    @Override
    public String toString() {
        return "{" + floodName + ",重量:" + weight + " kg,总卡路里:" + calorie + " cal,平均卡路里:" + calorieAvg() + " cal}";
    }


    public static FloodData create(String floodName, int weight, int calorie) {
        FloodData floodData = new FloodData();
        floodData.setFloodName(floodName);
        floodData.setWeight(weight);
        floodData.setCalorie(calorie);
        return floodData;
    }

}
