package org.devconmyanmar.apps.devcon.sync;

import com.google.gson.JsonObject;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Ye Lin Aung on 14/11/11.
 */
public interface SyncScheduleService {
  @GET("/schedules") void getSchedules(Callback<JsonObject> schedules);
}
