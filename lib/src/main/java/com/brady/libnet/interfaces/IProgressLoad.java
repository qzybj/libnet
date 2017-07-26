package com.brady.libnet.interfaces;



public interface IProgressLoad {

	void startRequest(ITag tag);

	void stopRequest(ITag tag);

	void destroy();
}
