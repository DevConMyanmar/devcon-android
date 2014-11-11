package org.devconmyanmar.apps.devcon.sync;

import java.util.List;
import org.devconmyanmar.apps.devcon.model.Speaker;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Ye Lin Aung on 14/11/09.
 */
public interface SyncSpeakerService {
  @GET("/speakers")
  void getSpeakers(Callback<List<Speaker>> speakers);
}
