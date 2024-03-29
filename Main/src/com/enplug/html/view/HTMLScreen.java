package com.enplug.html.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.enplug.sdk.interfaces.IWebView;
import com.enplug.sdk.model.html.WebPage;

public class HTMLScreen implements Screen
{
    private final IWebView _view;
    private SpriteBatch _batch;

    public HTMLScreen(IWebView view)
    {
        _view = view;
    }

    @Override
    public void render(float v)
    {
        Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _batch.begin();
        try
        {
            renderContent(_view);
        }
        finally
        {
            _batch.end();
        }
    }

    private void renderContent(IWebView view)
    {
        if (view != null)
        {
            view.draw(_batch, 0, 0);
        }
    }

    @Override
    public void resize(int width, int height)
    {
        Camera camera = new OrthographicCamera(width, height);
        camera.position.set(width / 2.0f, height / 2.0f, 0.0f);
        camera.update();
        _batch.setProjectionMatrix(camera.combined);
        _view.resize(width, height);
    }

    @Override
    public void show()
    {
        _batch = new SpriteBatch();
    }

    @Override
    public void hide()
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }
}
