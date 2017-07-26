package com.brady.libnet;


import android.app.Application;

import com.brady.libnet.bean.BaseRequest;
import com.brady.libnet.bean.TagBean;
import com.brady.libnet.config.NetConstants;
import com.brady.libnet.interfaces.IErrorInfo;
import com.brady.libnet.interfaces.IProgressLoad;
import com.brady.libnet.interfaces.IRequestListener;
import com.brady.libnet.interfaces.ITag;
import com.brady.libnet.interfaces.IResponseListener;
import com.brady.libnet.interfaces.impl.volley.DefVolleyManager;
import com.brady.libnet.util.NetParamUtil;
import com.qzybj.libutil.CLog;
import com.qzybj.libutil.device.NetUtil;

/**请求数据封装类*/
public class RequestManager {
	private static Application mApplication;
	private static RequestManager mDataServer = null;

	public static final int DEFAULT_REQUEST_TAG = 11001;
	public static final int DEFAULT_REQUEST_CODE = 11002;

	private IProgressLoad mProgressLoadProcess;
	private RequestManager() {}

	/**Initialise Volley Request Queue.*/
	public static void init(Application application){
		mApplication = application;
	}


	public synchronized static RequestManager instance(){
		if (mDataServer == null) {
			mDataServer = new RequestManager();
		}
		return mDataServer;
	}

	public Application getContext(){
		return mApplication;
	}

	public  <T> void request(BaseRequest req, Class<T> cls, IRequestListener listener) {
		TagBean tag  = new TagBean(req);
		startRequest(tag);
		if (!NetUtil.isNetConnected(getContext())) {
			errorCommon(listener,tag);
			return;
		}
		requestData(req,cls,listener,tag);
	}

	private  <T> void requestData(BaseRequest req, Class<T> cls, final IRequestListener listener, final TagBean tag) {
		try {
			DefVolleyManager.instance().request4Gson(req.getMethod(),req.getUrlAddr(),req.obtainHeader(),
					req.obtainPostData(),cls,DEFAULT_REQUEST_TAG,
					new IResponseListener<T>() {
						@Override
						public void onResponse(T response) {
							stopRequest(tag);
							if (listener!=null) {
								listener.onResponse(tag.getRequstCode(),response);
							}
						}
						@Override
						public void onErrorResponse(IErrorInfo error) {
							stopRequest(tag);
							if (listener!=null) {
								listener.onErrorResponse(tag.getRequstCode(),error);
							}
						}
					});
		} catch (Exception e) {
			CLog.e(e.getMessage());
			errorCommon(listener,tag);
		}
	}

	/**Cancel all request*/
	public void cancelAll(){
		DefVolleyManager.instance().cancelAll(DEFAULT_REQUEST_TAG);
	}

	protected void errorCommon(IRequestListener listener, ITag tag) {
		if (listener!=null) {
			stopRequest(tag);
			listener.onErrorResponse(tag.getRequstCode(), NetParamUtil.getErrorInfo(NetConstants.NetStatusCode.CODE_NO_NET));
		}
	}

	public void bindProgressLoad(IProgressLoad load) {
		unbindProgressLoad();
		this.mProgressLoadProcess = load ;
	}

	public void unbindProgressLoad() {
		if (mProgressLoadProcess!=null) {
			mProgressLoadProcess.destroy();
			mProgressLoadProcess = null;
		}
	}

	protected void startRequest(ITag tag) {
		if (mProgressLoadProcess!=null) {
			mProgressLoadProcess.startRequest(tag);
		}
	}

	protected void stopRequest(ITag tag) {
		if (mProgressLoadProcess!=null) {
			mProgressLoadProcess.stopRequest(tag);
		}
	}
}