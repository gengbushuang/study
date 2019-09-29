import ad.Ad;
import com.google.protobuf.TextFormat;
import targeting.TargetingBe;

public class MainTest {

    public static void main(String[] args) {


        Ad.AdInfo.Builder builder = Ad.AdInfo.newBuilder()
                .setAdId(1)
                .setAdName("AdsHonor")
                .setSiteId(1)
                .setOrderId(1)
                .setAdStatusValue(1)
                .setClickUrl("http://www.qq.com")
                .setAdDesc("test")
                .setOrderName("")
                .setTagNames("testTag")
                .setPlatformTypeValue(2)
                .setIsOnlineValue(2)
                .setUpdateTime(0)
                .setLandingPage("http://www.qq.com")
                .setBidTypeValue(0)
                .setBidPrice(0)
                .setAdvertTypeValue(0)
                .setBeginTime(0)
                .setEndTime(0)
                .setAdvertTime("")
                .setPriorityWeight(0)
                .setUserShowLimit(0)
                .setIpShowLimit(0)
                .addShowRestrict(Ad.AdShowRestrict.newBuilder().build())
                .setBudgetLimit(0)
                .setImpressionLimit(0)
                .setClickLimit(0)
                .setDayBudgetLimit(0)
                .setDayImprLimit(0)
                .setDayClickLimit(0)
                .setAdCtr(0)
                .setStrategyLimitType(0)
                .setStrategyLimitNum(0)
                .setTerminalType(0)
                .setClientType(0)
                .setRegionType(0)
                .setPeopleType(0)
                .setTargetingLimit("")
                .setMonitorUrl("")
                .setTargetPageValue(0)
                .setClickMonitorUrl("")
                .setAdposType(0)
                .setImprMonitorUrl("")
                .setActionType(0)
                .setValidImpDuration(0)
                .addCreativeInfos(Ad.CreativeInfo.newBuilder().setCreativeId(1).setText("CreativeFirst"))
                .addCreativeInfos(Ad.CreativeInfo.newBuilder().setCreativeId(2).setText("CreativeSecond"))
                .setDownloadLimit(0)
                .setAuto(0)
                .setPlayType(0)
                .setAutoPlayCondition(0)
                .setTransLimit(0)
                .setTrackUrls("|||||||||||||||")
                .setOffReport(0)
                .setOffImpreUrl("")
                .setOffReportValid(0)
                .setShowCondition(0);

        Ad.AdInfo adInfo = builder.build();
        System.out.println(TextFormat.printToString(adInfo));

        TargetingBe.TargetingBE.Builder targetingBeBu = TargetingBe.TargetingBE.newBuilder();
        TargetingBe.BETree.Builder bETreeBu = TargetingBe.BETree.newBuilder();
    }
}
