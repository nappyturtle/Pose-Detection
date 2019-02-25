
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
    private String name;
    public Pose() {      
    }

    public Pose(List<KeyPoint> keypoints, String url, String name) {
        this.keypoints = keypoints;
        this.url = url;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
