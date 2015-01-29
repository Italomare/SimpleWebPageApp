package com.enplug.html.controller;

import com.badlogic.gdx.Gdx;
import com.enplug.common.logging.ILog;
import com.enplug.sdk.hosting.AppState;
import com.enplug.sdk.interfaces.*;
import com.enplug.sdk.model.html.WebPage;

import java.util.List;
import java.util.Map;

public class ContentController
{
    private static final String TAG = "[HTML]:ContentController";
    private static final String WEB_PAGE_DEFINITIONS = "WebURL";

    private final IAssetProvider _assetProvider;
    private final IAppStatusListener _appStateListener;
    private final IWebViewFactory _webViewFactory;
    private final ILog _log;
    private WebPage _page;

    public ContentController(IServiceProvider serviceProvider, ILog log)
    {
        _webViewFactory = serviceProvider.getWebViewFactory();
        _assetProvider = serviceProvider.getAssetProvider();
        _appStateListener = serviceProvider.getAppStatusListener();
        _log = log.getSubLog(TAG);
    }

    public WebPage initialize()
    {
        List<Map> pageDefinitions = _assetProvider.getList(WEB_PAGE_DEFINITIONS);

        if ((pageDefinitions != null) && (!pageDefinitions.isEmpty()))
        {
            Map pageDefinition = pageDefinitions.get(0);
            _log.debug("Received HTML asset.");
            _page = new WebPage(pageDefinition);
            if (_page.isValid())
            {
                _page.load(_webViewFactory, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                _page.show();
                _appStateListener.onStateChanged(AppState.Ready);
            }
            else
            {
                _log.error("Webpage invalid: " + pageDefinition.toString());
                _appStateListener.onStateChanged(AppState.Error);
            }
        }
        else
        {
            _log.debug("No HTML assets are available.");
        }
        return _page;
    }

    public void dispose()
    {
        if (_page != null)
        {
            _page.dispose();
        }
    }

    public void resize(int width, int height)
    {
        if (_page != null)
        {
            _page.resize(width, height);
        }
    }
}

