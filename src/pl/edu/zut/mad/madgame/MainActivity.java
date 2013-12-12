package pl.edu.zut.mad.madgame;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final int POINTS_TO_WIN = 5;
	private static final int MAX_IMAGE_TIME_SEC = 2;
	private static final int MAX_LOGO_NUM = 9;
	private static final int WIN_LOGO_NUMBER = 5;

	private ImageView image;

	private int logoNumber = 0;
	private Button btnPlayerOne;
	private Button btnPlayerTwo;
	private TextView txtPlayerOnePoints;
	private TextView txtPlayerTwoPoints;

	private int playerOnePoints = 0;
	private int playerTwoPoints = 0;
	private boolean allowClick = true;
	private boolean oneClicked = false;
	private boolean twoClicked = false;

	private Handler backHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		backHandler = new Handler();

		txtPlayerOnePoints = (TextView) findViewById(R.id.txtPlayerOnePoints);
		txtPlayerTwoPoints = (TextView) findViewById(R.id.txtPlayerTwoPoints);

		btnPlayerOne = (Button) findViewById(R.id.btnPlayerOne);
		btnPlayerTwo = (Button) findViewById(R.id.btnPlayerTwo);

		btnPlayerOne.setOnClickListener(this);
		btnPlayerTwo.setOnClickListener(this);

		image = (ImageView) findViewById(R.id.image);
		startGame();
	}

	private void startGame() {
		resetPoints();
		backHandler.postDelayed(new Runnable() {
			public void run() {

				int logoNumber = new Random().nextInt(MAX_LOGO_NUM) + 1;
				MainActivity.this.logoNumber = logoNumber;
				resetClickState();

				Drawable drawable = getResources().getDrawable(
						getResources().getIdentifier("logo_" + logoNumber, "drawable", getPackageName()));
				image.setImageDrawable(drawable);

				int delayedTime = new Random().nextInt(MAX_IMAGE_TIME_SEC) + 1;
				backHandler.postDelayed(this, delayedTime * 1000);
			}
		}, 100);
	}

	private void resetPoints() {
		playerOnePoints = 0;
		playerTwoPoints = 0;

		txtPlayerOnePoints.setText(String.valueOf(playerOnePoints) + "/" + POINTS_TO_WIN);
		txtPlayerTwoPoints.setText(String.valueOf(playerTwoPoints) + "/" + POINTS_TO_WIN);
	}

	private void stopGame() {
		backHandler.removeCallbacksAndMessages(null);
	}

	private void resetClickState() {
		allowClick = true;
		oneClicked = false;
		twoClicked = false;
	}

	@Override
	public void onClick(View v) {
		if (allowClick) {

			switch (v.getId()) {

			case R.id.btnPlayerOne:
				if (!oneClicked) {
					oneClicked = true;
					if (logoOk()) {
						allowClick = false;
						playerOnePoints += 1;
					} else {
						playerOnePoints -= 1;
					}

					txtPlayerOnePoints.setText(String.valueOf(playerOnePoints) + "/" + POINTS_TO_WIN);
					checkWinner();
				}
				break;

			case R.id.btnPlayerTwo:
				if (!twoClicked) {
					twoClicked = true;
					if (logoOk()) {
						allowClick = false;
						playerTwoPoints += 1;
					} else {
						playerTwoPoints -= 1;
					}

					txtPlayerTwoPoints.setText(String.valueOf(playerTwoPoints) + "/" + POINTS_TO_WIN);
					checkWinner();
				}
				break;
			}
		}

	}

	private void checkWinner() {
		if (playerOnePoints >= POINTS_TO_WIN) {
			stopGame();
			showWinDialog(1);
		} else if (playerTwoPoints >= POINTS_TO_WIN) {
			stopGame();
			showWinDialog(2);
		}

	}

	private boolean logoOk() {
		if (logoNumber == WIN_LOGO_NUMBER)
			return true;
		else
			return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new_game:
			stopGame();
			startGame();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showWinDialog(int playerNumber) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.win_title);
		alertDialogBuilder.setMessage(getString(R.string.win_msg) + playerNumber + getString(R.string.win_msg_2))
				.setCancelable(false).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						startGame();
					}
				}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						MainActivity.this.finish();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
