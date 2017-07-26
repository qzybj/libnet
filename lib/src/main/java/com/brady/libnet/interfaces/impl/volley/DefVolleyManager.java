package com.brady.libnet.interfaces.impl.volley;

import android.app.Application;
import android.net.Uri;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.brady.libnet.util.NetParamUtil;
import com.brady.libnet.config.NetConstants;
import com.brady.libnet.interfaces.IErrorInfo;
import com.brady.libnet.interfaces.INetProcess;
import com.brady.libnet.interfaces.IResponseListener;
import com.brady.libnet.interfaces.impl.volley.request.GsonRequest;
import com.brady.libnet.interfaces.impl.volley.request.StringCustomRequest;
import com.qzybj.libutil.CLog;
import com.qzybj.libutil.data.MapUtil;

import java.util.Map;
import java.util.Map.Entry;


/**Volley工具类*/
public class DefVolleyManager implements INetProcess {
	private final String TAG = DefVolleyManager.class.getSimpleName();

	private static Application mApplication;
	private static DefVolleyManager instance;
	private RequestQueue mVolleyQueue;

	private final boolean SHOWLOG = true;

	private DefVolleyManager() {
		mVolleyQueue = Volley.newRequestQueue(mApplication);
	}

	/**Initialise Volley Request Queue.*/
	public static void init(Application application){
		mApplication = application;
	}

	public static DefVolleyManager instance() {
		if (instance==null) {
			instance = new DefVolleyManager();
			return instance;
		} else {
			return instance;
		}
	}

	private Application getContext(){
		return mApplication;
	}

	public int getRequestCount(){
		return getRequestQueue().getSequenceNumber();
	}

	private RequestQueue getRequestQueue(){
		if(mVolleyQueue==null){
			init(getContext());
		}
		return mVolleyQueue;
	}

	/**
	 * (class)request net data ,parse json to class
	 * @param method			Request.Method.GET  Request.Method.POST
	 * @param url				Url
	 * @param params 			请求参数
	 * @param cls				解析用类
	 * @param tag				用于全部取消,请求分组
	 * @param listener   		请求成功、失败回调监听
	 */
	@Override
	public <T> void request4Gson(int method, String url, Map<String, String> headers, Map<String, String> params,
                                 Class<T> cls, Object tag, final IResponseListener<T> listener) {
		int methodTmp = getMethod(method);
		String requestUrl = methodTmp== Request.Method.GET?getUrl(url,params):url;
		GsonRequest<T> request = new GsonRequest<T>(getMethod(method),requestUrl,cls,null,
				new Response.Listener<T>() {
					@Override
					public void onResponse(T response) {
						if (listener!=null) {
							listener.onResponse(response);
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (listener!=null) {
							listener.onErrorResponse(getErrorInfo(error));
						}
					}
				}
		);
		if(MapUtil.isNotEmpty(headers)){
			try {
				request.setHeaders(headers);
			} catch (AuthFailureError authFailureError) {
				CLog.e(authFailureError);
			}
		}
		if(methodTmp == Request.Method.GET&&MapUtil.isNotEmpty(params)){
			try {
				request.setParams(params);
			} catch (AuthFailureError authFailureError) {
				CLog.e(authFailureError);
			}
		}
		request.setTag(tag);
		getRequestQueue().add(request);
		startQueue();
	}

	/**
	 * (string)request net data ,get json string
	 * @param method	Request.Method.GET  Request.Method.POST
	 * @param url
	 * @param params 请求参数
	 * @param tag	用于全部取消,请求分组
	 * @param listener   		请求成功、失败回调监听
	 */
	@Override
	public void request4String(int method, String url, Map<String, String> headers, Map<String, String> params, Object tag,
                               final IResponseListener<String> listener){
		int methodTmp = getMethod(method);
		String requestUrl = methodTmp== Request.Method.GET?getUrl(url,params):url;

		StringCustomRequest request = new StringCustomRequest(getMethod(method),requestUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if (listener!=null) {
							listener.onResponse(response);
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (listener!=null) {
							listener.onErrorResponse(getErrorInfo(error));
						}
					}
				}
		);
		if(MapUtil.isNotEmpty(headers)){
			try {
				request.setHeaders(headers);
			} catch (AuthFailureError authFailureError) {
				CLog.e(authFailureError);
			}
		}
		if(methodTmp == Request.Method.GET&&MapUtil.isNotEmpty(params)){
			try {
				request.setParams(params);
			} catch (AuthFailureError authFailureError) {
				CLog.e(authFailureError);
			}
		}
		request.setTag(tag);
		getRequestQueue().add(request);
		startQueue();
	}
	public void addRequest(Request request){
		request.setTag(1);
		getRequestQueue().add(request);
		startQueue();
	}


		/**开始任务队列*/
	private void startQueue(){
		getRequestQueue().start();
	}

	@Override
	/**取消所有任务队列*/
	public void cancelAll(Object tag) {
		getRequestQueue().cancelAll(tag);
	}

	private void showLog(String msg) {
		if (SHOWLOG) {
			CLog.e(TAG,msg);
		}
	}

	private int getMethod(int method){
		switch (method){
			case NetConstants.Method.GET:
				return Request.Method.GET;
			case NetConstants.Method.POST:
				return Request.Method.POST;
			default:
				return Request.Method.POST;
		}
	}
	private String getUrl(String url, Map<String, String> QueryParameter){
		Uri.Builder builder = Uri.parse(url).buildUpon();
		if (QueryParameter!=null&&QueryParameter.size()>0) {
			for (Entry<String, String> element : QueryParameter.entrySet()) {
				builder.appendQueryParameter(element.getKey(),element.getValue());
			}
		}
		return builder.toString();
	}

	@Override
	public IErrorInfo getErrorInfo(Object error){
		if (error instanceof ParseError) {
			return NetParamUtil.getErrorInfo(NetConstants.NetStatusCode.CODE_NO_JSON,((VolleyError)error).getMessage());
		}else if (error instanceof NetworkError ||error instanceof ServerError ||
				error instanceof AuthFailureError ||error instanceof TimeoutError) {
			return NetParamUtil.getErrorInfo(NetConstants.NetStatusCode.CODE_NO_NET,((VolleyError)error).getMessage());
		}else{
			return NetParamUtil.getErrorInfo(NetConstants.NetStatusCode.CODE_NO_READ,((VolleyError)error).getMessage());
		}
	}
}