package com.brady.libnet.bean;


import com.brady.libnet.annotation.NoRequestArgs;
import com.brady.libnet.util.NetParamUtil;
import com.brady.libnet.annotation.RequestArgs;
import com.brady.libnet.config.NetConstants;
import com.qzybj.libutil.json.GsonUtil;

import java.util.HashMap;

/**用于继承处理的请求Base类*/
public class BaseRequest {

	@RequestArgs("method")
	private String methodName;

	@NoRequestArgs
	private String mUrlAddr;

	@NetConstants.Method
	@NoRequestArgs
	private int mMethod;

	@NoRequestArgs
	private int requestCode;

	/** True if this response was a soft-expired one and a second one MAY be coming. */
	@NetConstants.LoadingType
	@NoRequestArgs
	private int loadType = NetConstants.LoadingType.LOADING_NORMAL;

	public BaseRequest(){
		this.mUrlAddr = NetConstants.API_BASE_URL;
		this.mMethod = NetConstants.Method.POST;
	}

	protected BaseRequest setUrlAddr(String urladd) {
		this.mUrlAddr = urladd;
		return this;
	}

	protected BaseRequest setMethod(int method) {
		this.mMethod = method;
		return this;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setLoadType(int loadType) {
		this.loadType = loadType;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}

	public int getMethod() {
		return mMethod;
	}

	public String getUrlAddr() {
		return mUrlAddr;
	}

	@NetConstants.LoadingType
	public int getLoadType() {
		return loadType;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public String toJson() {
		return GsonUtil.toJson(this);
	}
	
	public HashMap<String, String> obtainPostData() {
		HashMap<String, String> res = NetParamUtil.getParams();
		res.putAll(NetParamUtil.getFieldsMap(this));
		res = NetParamUtil.encodeReqParams(res,NetConstants.APP_SECRET);
		return res;
	}
	
	public HashMap<String, String> obtainHeader() {
		HashMap<String, String> headers = NetParamUtil.getNetHeader();
		return headers;
	}
}