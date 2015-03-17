package com.enplug.html;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.enplug.common.logging.ILog;
import com.enplug.html.controller.ContentController;
import com.enplug.html.view.HTMLScreen;
import com.enplug.sdk.hosting.HostedGame;
import com.enplug.sdk.interfaces.IServiceProvider;
import com.enplug.sdk.interfaces.IWebView;
import com.enplug.sdk.model.html.WebPage;
import com.enplug.sdk.model.social.SocialFeedDefinition;

import java.util.List;

public class HTMLApp extends HostedGame
{
    private static final String TAG = "[HTML]";
    private static final int APP_VERSION = 1;

    private ContentController _contentController;
    private ILog _log;

    @Override
    public void initialize(IServiceProvider serviceProvider)
    {
        try
        {
            // initialize external dependencies
            //
            _log = serviceProvider.getLog().getSubLog(TAG);
            _log.info("Initializing app.");

            _contentController = new ContentController(serviceProvider, _log);

            IWebView view = _contentController.initialize();
            _screen = new HTMLScreen(view);

            _log.info("Initialized app.");
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
            _log.exception(e, "initialize");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create()
    {
        try
        {
            setScreen(_screen);
        }
        catch (RuntimeException e)
        {
            _log.exception(e, "create");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose()
    {
        if (_screen != null)
        {
            _screen.dispose();
        }

        if (_contentController != null)
        {
            _contentController.dispose();
        }
    }

    @Override
    public int getVersion()
    {
        return APP_VERSION;
    }

    @Override
    public void resize(int width, int height)
    {
        _log.info("Resizing to " + width + " x " + height);
        super.resize(width, height);
        _contentController.resize(width, height);
    }

    @Override
    public Screen getScreen()
    {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        resize(width, height);
        return super.getScreen();
    }
}
