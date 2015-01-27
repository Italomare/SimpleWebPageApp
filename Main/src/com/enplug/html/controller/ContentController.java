package com.enplug.html.controller;

import com.badlogic.gdx.Gdx;
import com.enplug.common.logging.ILog;
import com.enplug.html.model.*;
import com.enplug.html.model.WebPage;
import com.enplug.sdk.hosting.AppState;
import com.enplug.sdk.interfaces.*;

import java.util.List;
import java.util.Map;

public class ContentController
{
    private static final String TAG = "[HTML]:ContentController";
    private static final String WEB_PAGE_DEFINITIONS = "WebURL";

    private final Content _content;
    private final IAssetProvider _assetProvider;
    private final IAppStatusListener _appStateListener;
    private final IWebViewFactory _webCore;
    private final ILog _log;

    public ContentController(Content content,
                             IServiceProvider serviceProvider,
                             IWebViewFactory webCore,
                             ILog log)
    {
        _webCore = webCore;
        _content = content;

        _assetProvider = serviceProvider.getAssetProvider();
        _appStateListener = serviceProvider.getAppStatusListener();
        _log = log.getSubLog(TAG);
    }

    public void initialize()
    {
        List<Map> pageDefinitions = _assetProvider.getList(WEB_PAGE_DEFINITIONS);

        if (pageDefinitions != null)
        {
            _log.debug("Received %s HTML assets", Integer.toString(pageDefinitions.size()));
            preparePages(_webCore, pageDefinitions);
        }
        else
        {
            _log.warn("No HTML assets are available.");
        }
    }

    public void dispose()
    {
        _content.dispose();
    }

    private void preparePages(IWebViewFactory webViewFactory, Iterable<Map> pageDefinitions)
    {
        boolean hasValidPage = false;
        _log.debug("Preparing page...");

        for (Map pageDefinition : pageDefinitions)
        {
            WebPage page = new WebPage(pageDefinition);
            if (page.isValid())
            {
                page.load(webViewFactory, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                _content.addContent(page);
                _appStateListener.onStateChanged(AppState.Ready);
                hasValidPage = true;
                break;
            }
            else
            {
                _log.error("Webpage invalid: " + pageDefinition.toString());
                _appStateListener.onStateChanged(AppState.Error);
            }
        }

        if (!hasValidPage)
        {
            String reason = "No valid pages found.";
            _log.error(reason);
            throw new RuntimeException(reason);
        }
    }

    public void resize(int width, int height)
    {
        _content.resize(width, height);
    }
}

