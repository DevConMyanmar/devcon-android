package org.devconmyanmar.apps.devcon.utils;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.devconmyanmar.apps.devcon.db.FavoriteDao;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.db.TalkDao;
import org.devconmyanmar.apps.devcon.model.MySchedule;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.model.Talk;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/14.
 */
public class DataUtils {

  private static final String TAG = makeLogTag(DataUtils.class);
  private Context mContext;
  private SpeakerDao mSpeakerDao;
  private TalkDao mTalkDao;
  private FavoriteDao mFavDao;
  private Gson gson = new Gson();

  public DataUtils(Context context) {
    this.mContext = context;
    mSpeakerDao = new SpeakerDao(mContext);
    mTalkDao = new TalkDao(mContext);
    mFavDao = new FavoriteDao(mContext);
  }

  public void loadFromAssets() {

    try {
      InputStream json = mContext.getAssets().open("bootstrapdata.json");
      JsonParser parser = new JsonParser();
      JsonReader reader = new JsonReader(new InputStreamReader(json));
      reader.setLenient(true);

      JsonObject data = parser.parse(reader).getAsJsonObject();
      JsonArray sessionsArray = data.getAsJsonArray("sessions");
      JsonArray speakerArray = data.getAsJsonArray("speakers");
      JsonArray favArray = data.getAsJsonArray("my_schedules");
      LOGD(TAG, "sessions : " + sessionsArray.size());
      LOGD(TAG, "speakers : " + speakerArray.size());
      LOGD(TAG, "my_schedules : " + favArray.size());

      for (JsonElement k : speakerArray) {
        Speaker speaker = gson.fromJson(k, Speaker.class);
        mSpeakerDao.create(speaker);
      }

      for (JsonElement j : sessionsArray) {
        Talk talk = new Talk();
        talk.setId(j.getAsJsonObject().get("id").getAsInt());
        talk.setTitle(j.getAsJsonObject().get("title").getAsString());
        talk.setDescription(j.getAsJsonObject().get("description").getAsString());
        talk.setPhoto(j.getAsJsonObject().get("photo").getAsString());
        talk.setDate(j.getAsJsonObject().get("date").getAsString());
        talk.setFavourite(j.getAsJsonObject().get("favourite").getAsBoolean());
        talk.setTalk_type(j.getAsJsonObject().get("talk_type").getAsInt());
        talk.setRoom(j.getAsJsonObject().get("room").getAsString());
        talk.setFrom_time(j.getAsJsonObject().get("from_time").getAsString());
        talk.setTo_time(j.getAsJsonObject().get("to_time").getAsString());
        JsonArray speakers = j.getAsJsonObject().getAsJsonArray("speakers");
        talk.setSpeakers(speakers.toString());
        mTalkDao.create(talk);
      }

     for(JsonElement l:favArray ){
       MySchedule mySchedule = new MySchedule();
       mySchedule.setTitle(l.getAsJsonObject().get("title").getAsString());
       mySchedule.setSubTitle(l.getAsJsonObject().get("sub_title").getAsString());
       mySchedule.setStart(l.getAsJsonObject().get("start").getAsString());
       mySchedule.setEnd(l.getAsJsonObject().get("end").getAsString());
       mySchedule.setClickBlock(l.getAsJsonObject().get("click_block").getAsBoolean());
       mySchedule.setDate(l.getAsJsonObject().get("date").getAsInt());
       mySchedule.setId(l.getAsJsonObject().get("id").getAsInt());
       JsonArray talkIds = l.getAsJsonObject().getAsJsonArray("associated_talk");
       mySchedule.setAssociatedTalkId(talkIds.toString());
       mFavDao.create(mySchedule);
     }
      LOGD(TAG, "I am done ~ ");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
