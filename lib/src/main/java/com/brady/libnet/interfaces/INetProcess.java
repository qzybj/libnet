package com.brady.libnet.interfaces;


import java.util.Map;


public interface INetProcess {

	/**Get reuquest count times*/
	int getRequestCount();

	/**Reuquest result parse by gson*/
	<T> void request4Gson(int method, String url, Map<String, String> headers, Map<String, String> params,
                          Class<T> clazz, Object tag, IResponseListener<T> listener);
	/**Reuquest result source string ,no parse */
	void request4String(int method, String url, Map<String, String> headers, Map<String, String> params,
                        Object tag, IResponseListener<String> listener);
	/**Cancel all reuquest*/
	void cancelAll(Object tag);

	/**Get reuquest error info*/
	IErrorInfo getErrorInfo(Object error);
}
