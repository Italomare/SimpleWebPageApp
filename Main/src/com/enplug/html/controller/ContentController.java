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
    private static final String WEB_PAGE_DEFINITIONS = "WebURL";

    private final IAssetProvider _assetProvider;
    private final IAppStatusListener _appStateListener;
    private final IWebViewFactory _webViewFactory;
    private final ILog _log;
    private IWebView _view;

    public ContentController(IServiceProvider serviceProvider, ILog log)
    {
        _webViewFactory = serviceProvider.getWebViewFactory();
        _assetProvider = serviceProvider.getAssetProvider();
        _appStateListener = serviceProvider.getAppStatusListener();
        _log = log.getSubLog(this);
    }

    public IWebView initialize()
    {
        List<Map<String, Object>> pageDefinitions = _assetProvider.getList(WEB_PAGE_DEFINITIONS);

        if ((pageDefinitions != null) && (!pageDefinitions.isEmpty()))
        {
            Map<String, Object> pageDefinition = pageDefinitions.get(0);
            _log.debug("Received HTML asset.");
            WebPage page = new WebPage(pageDefinition);
            if (page.isValid())
            {
                _view = _webViewFactory.createView(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                _view.loadPage(page, _listener);
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
        return _view;
    }

    public void dispose()
    {
        if (_view != null)
        {
            _view.dispose();
        }
    }

    public void resize(int width, int height)
    {
        if (_view != null)
        {
            _view.resize(width, height);
        }
    }

    private IWebViewListener _listener = new IWebViewListener()
    {
        @Override
        public void onPageLoaded(String url)
        {
            _log.info("Successfully loaded (%s)", url);
            _view.show();
            _appStateListener.onStateChanged(AppState.Ready);
        }

        @Override
        public void onFailure(String url, int errorCode, String error)
        {
            _log.error("Failed to load (%s). Error (%s): %s", url, Integer.toString(errorCode), error);
        }
    };
}

