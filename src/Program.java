//7팀 
import loot.GameFrameSettings;

public class Program {

	public static void main(String[] args) {
		
		GameFrameSettings settings = new GameFrameSettings();
		settings.window_title = "개발자가 되는거냥";
		settings.canvas_width = 1070;
		settings.canvas_height = 780;
		
		settings.gameLoop_interval_ns = 16666666;
		settings.gameLoop_use_virtualTimingMode = true;
		
		settings.numberOfButtons = 6;
		
		MainFrame mainFrame = new MainFrame(settings);
		
		mainFrame.setVisible(true);

	}

}
