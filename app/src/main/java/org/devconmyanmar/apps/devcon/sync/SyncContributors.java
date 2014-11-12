package org.devconmyanmar.apps.devcon.sync;

import org.devconmyanmar.apps.devcon.model.Contributor;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Ye Lin Aung on 14/11/12.
 */
public interface SyncContributors {
  @GET("/repos/{org}/{repo_name}/contributors")
  void listRepos(
      @Path("org") String org,
      @Path("repo_name") String repo,
      Callback<Contributor> contributors
  );
}
