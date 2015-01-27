package com.enplug.html.model;

import com.badlogic.gdx.graphics.g2d.Batch;

public class Content
{
    private WebPage _page;

    public void dispose()
    {
        _page.dispose();
    }

    public void addContent(final WebPage page)
    {
        _page = page;
        _page.show();
    }

    public void render(Batch batch)
    {
        _page.draw(batch, 0, 0);
    }

    public void resize(int width, int height)
    {
        _page.resize(width, height);
    }

    public void hide()
    {
        if (_page != null)
        {
            _page.hide();
        }
    }

    public void show()
    {
        if (_page != null)
        {
            _page.show();
        }
    }
}
