
package com.group9.pdst.model;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author VU
 */
public class Pose {
    private List<KeyPoint> keypoints;
    private String url;

    public Pose() {      
    }

    public Pose(List<KeyPoint> keypoints, String url) {
        this.keypoints = keypoints;
        this.url = url;
    }

    public List<KeyPoint> getKeypoints() {
        if(keypoints == null) {
            keypoints = new ArrayList<>();
        }
        return keypoints;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
