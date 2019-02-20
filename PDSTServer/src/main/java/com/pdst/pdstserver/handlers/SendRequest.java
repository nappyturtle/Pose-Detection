package com.pdst.pdstserver.handlers;

import com.pdst.pdstserver.models.Video;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendRequest {
    public void sendRequestToCreateDataset(Video videoRequest){
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

            fileInfo.put("foldernameTrainer", videoRequest.getFolderName());
            fileInfo.put("videoUrl", videoRequest.getContentUrl());


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
        }catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void sendRequestToSuggest(Video videoRequest,String foldernameTrainee,int suggestionId,
                                     String traineeVideoUrl){
        try {
            System.out.println("da vao de gui url");
            URL url = new URL("http://localhost:8090/sliceVideoToSuggest");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            JSONObject fileInfo = new JSONObject();

            fileInfo.put("foldernameTrainer", videoRequest.getFolderName());
            fileInfo.put("foldernameTrainee", foldernameTrainee);
            fileInfo.put("suggestionId", suggestionId);
            fileInfo.put("videoUrl", traineeVideoUrl);
            fileInfo.put("headWeight",videoRequest.getHeadWeight());
            fileInfo.put("bodyWeight",videoRequest.getBodyWeight());
            fileInfo.put("legWeight",videoRequest.getLegWeight());


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
        }catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
