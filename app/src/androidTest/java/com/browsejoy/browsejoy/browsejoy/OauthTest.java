package com.browsejoy.browsejoy.browsejoy;

import com.browsejoy.games.app.oauth.Oauth;
import com.robotium.solo.Solo;

import android.support.test.InstrumentationRegistry;

import android.support.test.runner.AndroidJUnit4;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import android.support.test.rule.ActivityTestRule;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class OauthTest {

    private static final String NOTE_1 = "Note 1";
    private static final String NOTE_2 = "Note 2  test 99 ";




    @Rule
    public ActivityTestRule<Oauth> activityTestRule = new ActivityTestRule<>(Oauth.class);

    private Solo solo;


    @Before
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityTestRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }


    @Test
    public void testAddNote() throws Exception {
        //Unlock the lock screen
     //   solo.unlockScreen();

        // 2131361807
        // 2131361822
        //2131362088
        //2131362520
        //2131362462


        //Click on action menu item add
        // solo.clickOnView(solo.getView(com.example.android..R.id.menu_add));
        //Assert that NoteEditor activity is opened
        solo.assertCurrentActivity("Expected NoteEditor Activity", Oauth.class);
        //In text field 0, enter Note 1
        // solo.enterText(android.R.id.textaav, NOTE_1);

     //   solo.enterText((EditText) solo.getView(2131362088),"test gygy");

        ArrayList<Integer> str = new ArrayList<Integer>();

        for( View v : solo.getCurrentViews()){
            // do something with the View object here
            int viewId = v.getId();
            int viewId1 = v.getId();

           // str.add(viewId);

        }
//[-1, 16908679, -1, 2131361807, 2131361822, 16908290, 2131362088, -1, 2131362520, 2131362462, -1, -1, 2131362564, 16908335, -1, -1, 16908679, -1, 2131361807, 2131361822, 16908290, 2131362036, 2131362037, -1, 2131362079, 16908335, -1]

        String dk = str.toString();
        String dsk = str.toString();

//[-1, 16908679, -1, 2131361807, 2131361822, 16908290, 2131362088, -1, 2131362520, 2131362462, -1, -1, 2131362564, 16908335, -1, -1, 16908679, -1, 2131361807, 2131361822, 16908290, 2131362036, 2131362037, -1, -1, 2131362079, 16908335, -1]
      // int js = R.id.textaav;
      //  int jss = R.id.buttonsss;
      //  int jsss = R.id.textaav;



    //    solo.clickOnButton("");
     //   solo.clickOnButton("");
     //   solo.clickOnButton("");
        // solo.clickOnView(solo.getView(2131230757));
        //  solo.clickOnView(solo.getView(2131230757));
        //solo.clickOnView(solo.getView(2131230757));
     //   //solo.clickOnView(solo.getView(2131230757));

 //       solo.enterText((EditText) solo.getView(R.id.com_auth0_lock_input),"test gygy");
      //  solo.enterText((EditText) solo.getView(com.browsejoy.games.app.oauth.Oauth.R.id),"elioprifti@gmail.com");
    //    solo.enterText((EditText) solo.getView(2131362054),"111111");

        //Click on action menu item Save@id/com_auth0_lock_icon
        // solo.clickOnView(solo.getView(com.example.android.notepad.R.id.menu_save));
        //Click on action menu item Add
      ///  solo.clickOnView(solo.getView(com.browsejoy.games.app.oauth.Oauth.R.id.com_auth0_lock_icon));
        //In text field 0, type Note 2
        // solo.typeText(0, NOTE_2);
        //Click on action menu item Save
        //  solo.clickOnView(solo.getView(com.example.android.notepad.R.id.menu_save));
        //Takes a screenshot and saves it in "/sdcard/Robotium-Screenshots/".
        //  solo.takeScreenshot();
        //Search for Note 1 and Note 2
        //  boolean notesFound = solo.searchText(NOTE_1) && solo.searchText(NOTE_2);
        //To clean up after the test case
        //   deleteNotes();
        //Assert that Note 1 & Note 2 are found
        //     assertTrue("Note 1 and/or Note 2 are not found", notesFound);


    }



}
