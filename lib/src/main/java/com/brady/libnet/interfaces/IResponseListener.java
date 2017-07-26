package com.brady.libnet.interfaces;

/** Callback interface for delivering parsed、error responses.
 *
 * */
public interface IResponseListener<T> {
    /** Called when a response is received. */
    void onResponse(T response);
    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     */
    void onErrorResponse(IErrorInfo error);
}
