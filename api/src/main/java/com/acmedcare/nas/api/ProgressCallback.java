package com.acmedcare.nas.api;

/**
 * Callback interface for reporting upload progress for a request.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
public interface ProgressCallback {

  /** Default ProgressCallback */
  ProgressCallback DEFAULT =
      new ProgressCallback() {
        public void onProgress(long uploaded, long total) {}
      };

  /**
   * Callback invoked as data is process by the request.
   *
   * @param uploaded The number of bytes already process
   * @param total The total number of bytes that will be process or -1 if the length is unknown.
   */
  void onProgress(long uploaded, long total);
}
