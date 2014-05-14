package org.matheusdev.ror.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.matheusdev.ror.ResourceLoader;
import org.matheusdev.ror.RuinsOfRevenge;
import org.matheusdev.ror.screens.gui.TouchUpListener;
import org.matheusdev.util.ExceptionUtils;

/**
 * @author matheusdev
 */
public class ScreenError extends AbstractScreen
{

	private final InputListener backListener = new TouchUpListener()
	{
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			game.popScreen();
		}
	};
	private final InputListener submitListener = new TouchUpListener()
	{
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button)
		{
			game.pushScreen(new ScreenError(resources, game, "Feature not implemented!", null));
		}
	};

	private final RuinsOfRevenge game;
	private final ResourceLoader resources;

	private final Skin skin;

	public ScreenError(final ResourceLoader resources, final RuinsOfRevenge game, String message, final Exception e)
	{
		super(new Stage(), game);
		this.resources = resources;
		this.game = game;

		skin = resources.getSkin("uiskin");

		final Dialog dialog = new Dialog("Error", skin);

		TextButton back = new TextButton("Back", skin);
		TextButton submit = new TextButton("Sumbit", skin);

		back.addListener(backListener);
		submit.addListener(submitListener);

		dialog.getContentTable().pad(8);
		dialog.getButtonTable().pad(8);

		dialog.getContentTable().add(message).space(8);
		dialog.getContentTable().row();
		dialog.getContentTable().add("Details: ").space(8);
		dialog.getContentTable().row();
		if(e != null)
		{
			Label excText = new Label(ExceptionUtils.stackTraceToString(e), skin);
			ScrollPane pane = new ScrollPane(excText, skin);
			pane.setFadeScrollBars(false);
			pane.setOverscroll(false, true);
			dialog.getContentTable().add(pane).height(100).space(8);
			dialog.getContentTable().row();
		}
		dialog.getButtonTable().add(back).size(160, 32).space(8);
		dialog.getButtonTable().add(submit).size(160, 32).space(8);

		dialog.setKeepWithinStage(true);
		dialog.show(stage);

		dialog.getContentTable().debug();
	}

	@Override
	public void tick(float delta)
	{
		stage.act(delta);
	}

	@Override
	public void draw(Batch batch)
	{
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public boolean isParentVisible()
	{
		return true;
	}

}
