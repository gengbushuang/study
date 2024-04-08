package com.correlation.search.movie;

import com.correlation.search.movie.bean.VideoBean;
import com.correlation.search.movie.bean.VideoDataBean;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author gengbushuang
 * @date 2022/2/24 14:23
 */
public class VideoTest {
    public static void main(String[] args) throws IOException {
        String path = "/Users/gbs/tmp/data_0223.json";
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String data = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(data);
        VideoDataBean videoDataBean = JsonUtil.string2Obj(data, VideoDataBean.class);

        List<VideoBean> videoBeanList = videoDataBean.getData();
        for (VideoBean videoBean : videoBeanList) {
            String download_url = videoBean.getDownload_url();
            if (StringUtils.isBlank(download_url)) {
                continue;
            }
//            System.out.println(download_url);
            ///Users/gbs/tmp/tmp_python/ml/video/video_tag/data/mp4/tmp
            String[] split = download_url.split("/");
            String videoPath = "/Users/gbs/tmp/tmp_python/ml/video/video_tag/data/mp4/tmp/" + split[3] + ".mp4";
            File file = new File(videoPath);
//            System.out.println(videoPath+" exists "+file.exists());
            if (file.exists()) {
//                System.out.println(videoPath);
                System.out.println(videoBean.getVideo_type());
            }
        }
    }
}
