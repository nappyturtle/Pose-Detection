package com.pdst.pdstserver.models;

import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Video implements Serializable {
    private int id;
    private String title;
    private String thumnailUrl;
    private String contentUrl;
    private Integer accountId;
    private Integer categoryId;
    private Integer numOfView;
    private String status;

    private String folderName;
    private String createdTime;
    private String updatedTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "thumnail_url")
    public String getThumnailUrl() {
        return thumnailUrl;
    }

    public void setThumnailUrl(String thumnailUrl) {
        this.thumnailUrl = thumnailUrl;
    }

    @Basic
    @Column(name = "content_url")
    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Basic
    @Column(name = "account_id")
    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "category_id")
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "num_of_view")
    public Integer getNumOfView() {
        return numOfView;
    }

    public void setNumOfView(Integer numOfView) {
        this.numOfView = numOfView;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return id == video.id &&
                Objects.equals(title, video.title) &&
                Objects.equals(thumnailUrl, video.thumnailUrl) &&
                Objects.equals(contentUrl, video.contentUrl) &&
                Objects.equals(accountId, video.accountId) &&
                Objects.equals(categoryId, video.categoryId) &&
                Objects.equals(numOfView, video.numOfView) &&
                Objects.equals(status, video.status) &&
                Objects.equals(createdTime, video.createdTime) &&
                Objects.equals(updatedTime, video.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, thumnailUrl, contentUrl, accountId, categoryId, numOfView, status, createdTime, updatedTime);
    }

    @Basic
    @Column(name = "folder_name")
    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @PrePersist
    public void prePersist() {
        System.out.println("pre persist!");
    }

    @PostPersist
    public void postPersist() {
        try {
            System.out.println("da vao de gui url");
            URL url = new URL("http://localhost:8090/sliceVideo");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            JSONObject fileInfo = new JSONObject();

            fileInfo.put("videoName","KietPT-456789/Video/1.mp4");
            fileInfo.put("videoUrl",this.getContentUrl());


            OutputStream os = con.getOutputStream();
            os.write(fileInfo.toString().getBytes("UTF-8"));
            //os.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                System.out.println("" + sb.toString());
            } else {
                System.out.println(con.getResponseMessage());
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("aaaaaaaaaaaaaaaaaaaa");
    }

    @Basic
    @Column(name = "created_time")
    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "updated_time")
    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}