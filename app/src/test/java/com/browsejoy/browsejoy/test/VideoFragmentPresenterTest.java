//package com.browsejoy.games;
//
//import android.content.Context;
//
//import com.browsejoy.games.app.data.model.response.DatasModel;
//import com.browsejoy.games.app.view.fragments.video.VideoFragment;
//import com.browsejoy.games.app.view.fragments.video.VideoFragmentPresenter;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import java.util.ArrayList;
//
//
///**
// * Created by Eljo on 8/30/2018.
// */
//
//@RunWith(MockitoJUnitRunner.class)
//public class VideoFragmentPresenterTest {
//
//
//    VideoFragmentPresenter videoFragmentPresenter;
//
//    @Mock
//    VideoFragment videoFragment;
//
//    @Mock
//    Context context;
//
//
//    @Before
//    public void setUp() throws Exception{
//        videoFragmentPresenter =  Mockito.spy(new VideoFragmentPresenter(videoFragment,context));
//    }
//
//    @Test
//    public void requestAd() throws Exception {
//
//        //Arrange
//        int currectPosition = 0;
//        ArrayList<String> arrayListAdTagUrl = new ArrayList<>();
//        arrayListAdTagUrl.add("http://ads.aerserv.com/as/?plc=1047125&cb=&appversion=&adid=&network=&carrier=&dnt=&ip=&make=&model=&os=&osv=&type=&lat=&long=&locationsource=&ua=&vpw=&vph=&vpaid=&coppa=&age=&yob=&gender=&gdpr=&gdpr_consent=");
//
//        //Act
//        videoFragmentPresenter.playAdTagURl(currectPosition,arrayListAdTagUrl);
//
//        //Assert
//        Mockito.verify(videoFragmentPresenter).startCountDown();
//
//    }
//
//    @Test
//    public void reRequestGif() throws Exception {
//
//        //Arrange
//        DatasModel d1 = new DatasModel();
//        d1.setId("1");
//        d1.setUrl("https://media.giphy.com/media/26tkmH4VvEh4m0jiU/giphy.gif");
//
//        ArrayList<DatasModel> feedItemListgGlobal = new ArrayList<>();
//        feedItemListgGlobal.add(d1);
//
//        //Act
//        videoFragmentPresenter.requestGif(feedItemListgGlobal);
//
//        //Assert
//        Mockito.verify(videoFragmentPresenter).requestAd();
//
//    }
//}
