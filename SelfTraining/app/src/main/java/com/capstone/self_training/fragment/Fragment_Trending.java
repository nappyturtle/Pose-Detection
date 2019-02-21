
package com.capstone.self_training.fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.self_training.R;
import com.capstone.self_training.adapter.HomeVideoAdapter;
import com.capstone.self_training.dto.VideoDTO;
import com.capstone.self_training.model.Account;
import com.capstone.self_training.model.Video;
import com.capstone.self_training.service.dataservice.VideoService;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Trending extends Fragment {

    /***** HARD DATA *****/

    /*private static final String[] thumbnail_list_demo = {
            "https://kenh14cdn.com/zoom/700_438/2018/11/27/5b4083abbcf59-blackpink-instagram-photo-music-core-win-white-outfit-2-1543314360824254878270-crop-1549777736947858310470.jpg",
            "https://toquoc.mediacdn.vn/2019/1/7/yq-blackpink-040120192x2x-1546846556026543083655.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/0/00/%EB%B8%94%EB%9E%99%ED%95%91%ED%81%AC%28BlackPink%29_-_%EB%A7%88%EC%A7%80%EB%A7%89%EC%B2%98%EB%9F%BC_171001_%EC%BD%94%EB%A6%AC%EC%95%84%EB%AE%A4%EC%A7%81%ED%8E%98%EC%8A%A4%ED%8B%B0%EB%B2%8C.jpg",
            "https://photo-2-baomoi.zadn.vn/w1000_r1/2018_09_22_329_27832729/035959bf66fe8fa0d6ef.jpg",
            "https://img.jakpost.net/c/2016/08/09/2016_08_09_9562_1470736144._large.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcpfXOvNQE3kBrDVi0i3VDlXTB_SeLN_iTMWn2RXk5v20nuYML",
            "https://allaboutkpop.net/wp-content/uploads/2019/01/4f5f4e4d41744da1929583307b6d3e78.jpeg",
            "https://1.bp.blogspot.com/-rqwR3IAKmuw/WyXVE1_8lJI/AAAAAAAAxcA/QUcSuDzoHbUerNlLFl_eF-HQ5zCLQRobQCLcBGAs/s1600/201806170922773667_5b25afa248729.jpg",
            "https://akns-images.eonline.com/eol_images/Entire_Site/20181028/rs_600x600-181128235449-e-asia-blackpink-fave-fashion-trends-thumbnail.jpg?fit=around|700:700&crop=700:700;center,top&output-quality=90",
            "http://www.kpoppro.com/wp-content/uploads/2018/06/BLACKPINK10-1.jpg"
    };
    private static final String[] video_list_demo = {
            "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
            "http://techslides.com/demos/sample-videos/small.mp4",
            "https://thepaciellogroup.github.io/AT-browser-tests/video/ElephantsDream.mp4",
            "http://www.html5videoplayer.net/videos/toystory.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/jellyfish-25-mbps-hd-hevc.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/lion-sample.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/Panasonic_HDC_TM_700_P_50i.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/dolbycanyon.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/star_trails.mp4",
            "http://mirrors.standaloneinstaller.com/video-sample/TRA3106.mp4"
    };

    private static final String[] account_demo = {
            "Ngoc",
            "Hau",
            "Minh",
            "Sang",
            "Hoang",
            "Kiet",
            "Vu",
            "Phat",
            "Thinh",
            "Hieu"
    };
    private static final Account account = new Account(
            1,
            "hoangtlt",
            "acbxyz123",
            "hoang@gmail.com",
            "0123456789",
            "Nam",
            "https://scontent.fsgn5-4.fna.fbcdn.net/v/t1.0-9/10600429_359175494240981_2588390037092242108_n.jpg?_nc_cat=102&_nc_oc=AQlg2VkCpatLNfOJdMqfBxEvoEq200uTJF0adkKZpuIsE_ew6RGgRdcfKNiloh1Kf0c&_nc_ht=scontent.fsgn5-4.fna&oh=ea12de05ba33e04e6ce4ed95091b663f&oe=5CE453BF",
            "1358/28/14 Quang Trung",
            null,
            null,
            null,
            null,
            null
    );*/


    /***************************/

    private View view;
    private RecyclerView trending_video_list;
    private RecyclerView.Adapter trendingVideoAdapter;
    private List<Video> videos;
    private List<Account> accounts;
    private VideoService videoService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trending, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        trending_video_list = (RecyclerView) view.findViewById(R.id.trending_video_list);
        trending_video_list.setHasFixedSize(true);
        trending_video_list.setLayoutManager(new LinearLayoutManager(getContext()));

        videos = new ArrayList<>();
        accounts = new ArrayList<>();

        videoService = new VideoService();
        List<VideoDTO> videoDTOS = videoService.getVideosByDate();

        for (VideoDTO dto : videoDTOS) {
            videos.add(dto.getVideo());
            Account account = new Account();
            account.setUsername(dto.getUsername());
            account.setImgUrl(dto.getImgUrl());
            accounts.add(account);
        }

        /** SET HARD CODE **/
        /*for (int i = 0; i < 10; i++) {
            Video video = new Video(
                    1,
                    "Demo display long name video about yoga thumbnail in list " + i,
                    thumbnail_list_demo[i],
                    video_list_demo[i],
                    1,
                    20,
                    107812,
                    "enable",
                    account_demo[i],
                    "",
                    "",
                    "",
                    "",
                    null,
                    null,
                    null
            );
            videos.add(video);
            accounts.add(account);
        }*/
        /****************************/


        trendingVideoAdapter = new HomeVideoAdapter(videos, getContext(), accounts);

        trending_video_list.setAdapter(trendingVideoAdapter);

        return view;
    }

}
