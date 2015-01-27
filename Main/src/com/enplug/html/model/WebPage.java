package com.enplug.html.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.enplug.sdk.interfaces.IWebView;
import com.enplug.sdk.interfaces.IWebViewFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebPage
{
    private ScheduledExecutorService _executor;
    private IWebView _view;
    private String _url;
    private String _token;
    private String _username;
    private String _password;
    private String _javascriptOnload;
    private boolean _enableJavascript;
    private boolean _isHTML;
    private boolean _isValid;
    private int _refreshInterval;
    private int _showDelay;

    public WebPage(Map jsonObject)
    {
        _url = getUrlOrHtml(jsonObject);
        _token = (String)jsonObject.get("token");
        _username = (String)jsonObject.get("username");
        _password = (String)jsonObject.get("password");
        _javascriptOnload = (String)jsonObject.get("javascriptOnload");

        _enableJavascript = getValue(jsonObject, "enableJavascript", true);
        _refreshInterval = getValue(jsonObject, "refreshInterval", -1);
        if (_refreshInterval > 0)
        {
            _executor = Executors.newSingleThreadScheduledExecutor();
        }
        _showDelay = getValue(jsonObject, "showDelay", -1);

        _isValid = (_url != null);
    }

    private String getUrlOrHtml(Map jsonObject)
    {
        String url = (String)jsonObject.get("url");
        _isHTML = (url == null);
        if (_isHTML)
        {
            url = (String)jsonObject.get("html");
        }
        return url;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getValue(Map jsonObject, String key, T defaultValue)
    {
        T value = (T) jsonObject.get(key);
        if (value != null)
        {
            return value;
        }
        return defaultValue;
    }

    public final void load(IWebViewFactory webViewFactory, int width, int height/*, IOnLoadListener listener*/)
    {
        _view = webViewFactory.createView(width, height);
        setJavascript();
        setLoginDetails();
        loadData();
        if (_executor != null)
        {
            setRefreshInterval();
        }
    }

    public void setJavascript()
    {
        _view.enableJavascript(_enableJavascript);
        _view.setJavascript(_javascriptOnload);
    }

    public void setLoginDetails()
    {
        if (_token != null)
        {
            _view.setToken(_token);
        }

        if (_username != null || _password != null)
        {
            _view.setLogin(_username, _password);
        }
    }

    public void loadData()
    {
        if (_isHTML)
        {
            _view.loadHTML(_url);
        }
        else
        {
            _view.loadURL(_url);
        }
    }

    public void setRefreshInterval()
    {
        _executor.scheduleWithFixedDelay(new Runnable()
        {
            @Override
            public void run()
            {
                _view.refresh();
            }
        }, _refreshInterval, _refreshInterval, TimeUnit.SECONDS);
    }

    public void draw(Batch batch, int x, int y)
    {
        _view.draw(batch, x, y);
    }

    public void dispose()
    {
        _view.dispose();
    }

    public boolean isValid()
    {
        return _isValid;
    }

    public void render(Texture tex)
    {
        _view.render(tex);
    }

    public String getURL()
    {
        return _url;
    }

    public int getShowDelay()
    {
        return _showDelay;
    }

    public int getRefreshInterval()
    {
        return _refreshInterval;
    }

    public void resize(int width, int height)
    {
        _view.resize(width, height);
    }

    public void show()
    {
        _view.show();
    }

    public void hide()
    {
        _view.hide();
    }
}
