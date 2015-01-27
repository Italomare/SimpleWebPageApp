package com.enplug.html;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.enplug.apps.example.SampleApp;
import com.enplug.apps.test.AppLoader;
import com.enplug.apps.test.TestBase;
import com.enplug.apps.test.TestServiceProviderFactory;
import com.enplug.player.hosting.launcher.AppDefinition;
import com.enplug.player.hosting.launcher.AppInstance;
import com.enplug.player.hosting.launcher.AppLauncher;
import com.enplug.player.hosting.launcher.AppSchedule;
import com.enplug.player.hosting.launcher.data.Asset;
import com.enplug.player.hosting.model.Platform;
import com.enplug.player.model.event.system.ImpressionEvent;
import com.enplug.sdk.hosting.AppState;
import com.enplug.sdk.hosting.HostedGame;
import com.enplug.sdk.model.social.SocialFeedDefinition;
import com.enplug.sdk.model.social.SocialNetwork;

import java.util.ArrayList;
import java.util.List;

public class TestSetup extends TestBase implements InputProcessor
{
    private HostedGame _app;

    @Override
    // Override this method if need different implementation for some base services
    //
    public void setup()
    {
        Gdx.input.setInputProcessor(this);

        _providerFactory = new TestServiceProviderFactory(_downloader, _videoPlayer, _webCore, _social, _notifications, null, _bus, _log);

        _schedule = new AppSchedule();
        AppDefinition rssApp = createApp();
        _schedule.getApps().add(rssApp);

        _appLoader = new AppLoader(rssApp, _bus);

        AppDefinition app = createSampleApp();
        _schedule.getApps().add(app);
        app.getInstance().setState(AppState.Loaded);

        _launcher = new AppLauncher(
                Platform.Desktop,
                _gameHost,
                _appLoader,
                _providerFactory,
                _callToActionService,
                _eventService,
                _socialService,
                null,
                _localizer,
                null,
                null,
                _metricProvider,
                _mapper,
                _bus,
                _log);

        _launcher.initialize(Gdx.graphics.getWidth() > Gdx.graphics.getHeight(), "English");
        _bus.register(_launcher);
        app.getInstance().setState(AppState.Ready);
    }

    @Override
    public AppDefinition createApp()
    {
        List<SocialFeedDefinition> feeds = new ArrayList<SocialFeedDefinition>();
        SocialFeedDefinition instagramFeed = new SocialFeedDefinition("InstagramFeed", SocialNetwork.Instagram);
        feeds.add(instagramFeed);

        AppDefinition app = createAppDefinition("RSSId", feeds);
        app.getData().setAssets(createAssets());

        AppInstance instance = new AppInstance();
        _app = new HTMLApp();
        instance.setApp(_app);
        app.setInstance(instance);

        return app;
    }

    public AppDefinition createSampleApp()
    {
        List<SocialFeedDefinition> feeds = new ArrayList<SocialFeedDefinition>();
        SocialFeedDefinition twitterFeed = new SocialFeedDefinition("TwitterFeed", SocialNetwork.Twitter);
        feeds.add(twitterFeed);

        AppDefinition app = createAppDefinition("SampleAppId", feeds);
        AppInstance instance = new AppInstance();
        instance.setApp(new SampleApp());
        app.setInstance(instance);

        return app;
    }

    private ArrayList<Asset> createAssets()
    {
        ArrayList<Asset> list = new ArrayList<Asset>();

        Asset a0 = new Asset();
        a0.setIsList(true);
        a0.setAssetType("Object");
        a0.setName("WebURL");
        a0.setValue("{ \"url\" : \"http://www.google.com\"}");
        list.add(a0);

        return list;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        switch(keycode)
        {
            case Input.Keys.I:
                Gdx.app.log("DEBUGGING INPUT", "Pressed I");
                if (_launcher != null)
                {
                    _launcher.onImpression(new ImpressionEvent());
                }
                break;
            case Input.Keys.P:
                Gdx.app.log("DEBUGGING INPUT", "Pressed P");
                if (_app != null)
                {
                    _app.getScreen();
                    _app.pause();
                }
                break;
            case Input.Keys.R:
                Gdx.app.log("DEBUGGING INPUT", "Pressed R");
                if (_app != null)
                {
                    _app.getScreen();
                    _app.resume();
                }
                break;
            case Input.Keys.ESCAPE:
                _app.dispose();
                Gdx.app.exit();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
