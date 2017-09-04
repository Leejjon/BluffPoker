package net.leejjon.bluffpoker.ios;

import apple.foundation.NSURL;
import apple.uikit.UIApplication;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.net.*;

public class IOSNet2 implements Net {

    NetJavaImpl netJavaImpl = new NetJavaImpl();
    final UIApplication uiApp;

    public IOSNet2 (IOSApplication2 app) {
        uiApp = app.uiApp;
    }

    @Override
    public void sendHttpRequest (HttpRequest httpRequest, HttpResponseListener httpResponseListener) {
        netJavaImpl.sendHttpRequest(httpRequest, httpResponseListener);
    }

    @Override
    public void cancelHttpRequest (HttpRequest httpRequest) {
        netJavaImpl.cancelHttpRequest(httpRequest);
    }

    @Override
    public ServerSocket newServerSocket (Protocol protocol, String hostname, int port, ServerSocketHints hints) {
        return new NetJavaServerSocketImpl(protocol, hostname, port, hints);
    }

    @Override
    public ServerSocket newServerSocket (Protocol protocol, int port, ServerSocketHints hints) {
        return new NetJavaServerSocketImpl(protocol, port, hints);
    }

    @Override
    public Socket newClientSocket (Protocol protocol, String host, int port, SocketHints hints) {
        return new NetJavaSocketImpl(protocol, host, port, hints);
    }

    @Override
    public boolean openURI (String URI) {
        if (uiApp.canOpenURL(NSURL.URLWithString(URI))) {
            uiApp.openURL(NSURL.URLWithString(URI));
            return true;
        }
        return false;
    }
}
