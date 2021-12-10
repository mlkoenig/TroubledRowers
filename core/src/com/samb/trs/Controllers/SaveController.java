package com.samb.trs.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.samb.trs.Model.Account;
import com.samb.trs.Resources.Saves;
import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.gamestate.ILoadGameStateResponseListener;
import de.golfgl.gdxgamesvcs.gamestate.ISaveGameStateResponseListener;

import java.io.*;

import static com.samb.trs.Resources.Preferences.FIXED_ROTATION;
import static com.samb.trs.Resources.Preferences.PREFERENCE_NAME;


public class SaveController extends BaseController{

	private IGameServiceClient gameServiceClient;
	private Preferences preferences;
	private Account account;
	private boolean isAccountLoaded;

	public SaveController(MainController mainController) {
		super(mainController);
		this.gameServiceClient = mainController.getGameServiceController().getGameServiceClient();

		initPreferences();
		initAccount();
	}

	private void initPreferences(){
		preferences = Gdx.app.getPreferences(PREFERENCE_NAME);
		preferences.putBoolean(FIXED_ROTATION, false);
		preferences.flush();
	}

	public Preferences getPreferences() {
		return preferences;
	}

	/**
	 * Initializes account for later use either from google drive or from local file if features are not supported.
	 */
	private void initAccount() {
		if (gameServiceClient.isFeatureSupported(IGameServiceClient.GameServiceFeature.GameStateStorage)) {
			loadGameState();
		} else {
			loadLocalGameState();
		}
	}

	/**
	 * Loads the account from local file or sets a new account if no account was found.
	 */
	private void loadLocalGameState() {
		if (Saves.ACCOUNT.exists())
			account = (Account) deserializeObject(Saves.ACCOUNT);
		if (account == null)
			account = new Account(getMain());
		isAccountLoaded = true;
	}


	// TODO: cleaner implementation for loading and writing account data (also syncing)

	/**
	 * Load account from google drive or from local file if features are not supported.
	 */
	public void loadGameState() {
		gameServiceClient.loadGameState(Saves.GAMESTATE, new ILoadGameStateResponseListener() {
			@Override
			public void gsGameStateLoaded(byte[] gameState) {
				if (gameState != null) account = (Account) deserializeFromByteArray(gameState);
				if (account == null) loadLocalGameState();
				saveGameState();
				isAccountLoaded = true;
			}
		});
	}

	/**
	 * Save account to google drive or save account to local file if features not supported
	 */
	public void saveGameState() {
		if (gameServiceClient.isFeatureSupported(IGameServiceClient.GameServiceFeature.GameStateStorage)) {
			gameServiceClient.saveGameState(Saves.GAMESTATE, serializeToByteArray(account), account.getPlayTime(), new ISaveGameStateResponseListener() {
				@Override
				public void onGameStateSaved(boolean success, String errorCode) {
					if (success) Gdx.app.log("SyncGameState", "syncing was successful!");
					else Gdx.app.log("SyncGamestate", "Error: " + errorCode);
					serializeObject(account, Saves.ACCOUNT);
				}
			});
		} else serializeObject(account, Saves.ACCOUNT);
	}

	public boolean isAccountLoaded() {
		return isAccountLoaded;
	}

	public Account getAccount() {
		return account;
	}

	private Object deserializeObject(FileHandle fileHandle) {
		Object o = null;
		try {
			InputStream inputStream = fileHandle.read();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			o = objectInputStream.readObject();
			objectInputStream.close();
			Gdx.app.log("SER", "Successfully deserialized " + fileHandle.name());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return o;
	}

	private void serializeObject(Serializable o, FileHandle fileHandle) {
		try {
			OutputStream outputStream = fileHandle.write(false);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(o);
			objectOutputStream.flush();
			objectOutputStream.close();
			Gdx.app.log("SER", "Successfully serialized " + fileHandle.name());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] serializeToByteArray(Object o) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] bytes = new byte[0];
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(o);
			out.flush();
			bytes = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}

		return bytes;
	}

	private Object deserializeFromByteArray(byte[] bytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		Object o = null;
		try {
			in = new ObjectInputStream(bis);
			o = in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		return o;
	}

	@Override
	public void dispose() {

	}
}
