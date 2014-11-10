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
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.db.TalkDao;
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
  private Gson gson = new Gson();

  public DataUtils(Context context) {
    this.mContext = context;
    mSpeakerDao = new SpeakerDao(mContext);
    mTalkDao = new TalkDao(mContext);
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
      LOGD(TAG, "sessions : " + sessionsArray.size());
      LOGD(TAG, "speakers : " + speakerArray.size());

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

      LOGD(TAG, "I am done ~ ");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
