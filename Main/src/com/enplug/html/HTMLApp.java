package com.enplug.html;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.enplug.common.logging.ILog;
import com.enplug.html.controller.ContentController;
import com.enplug.html.model.Content;
import com.enplug.html.view.HTMLScreen;
import com.enplug.sdk.hosting.HostedGame;
import com.enplug.sdk.interfaces.IServiceProvider;
import com.enplug.sdk.interfaces.IWebViewFactory;
import com.enplug.sdk.model.social.SocialFeedDefinition;

import java.util.List;

public class HTMLApp extends HostedGame
{
    private static final String TAG = "[HTML]";
    private static final int APP_VERSION = 1;

    private ILog _log;
    private Content _content;
    private ContentController _contentController;

    public IWebViewFactory _webViewFactory;

    @Override
    public void initialize(IServiceProvider serviceProvider, List<SocialFeedDefinition> feeds, boolean isLandscape, String language)
    {
        try
        {
            // initialize external dependencies
            //
            _log = serviceProvider.getLog().getSubLog(TAG);
            _log.info("Initializing app.");

            _webViewFactory = serviceProvider.getWebViewFactory();

            _content = new Content();

            _screen = new HTMLScreen(_content);

            _contentController = new ContentController(_content,
                                                serviceProvider,
                                                _webViewFactory,
                                                _log);
            _contentController.initialize();
            _log.info("Initialized app.");
        }
        catch (Exception e)
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
        catch (Exception e)
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

        if (_content != null)
        {
             _content.dispose();
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
