/*
 * 게임프로그래밍을 위한 캡스톤 디자인 (2021)
 * 7팀
 * <개발자가 되는거냥> 
 */
import java.awt.event.KeyEvent;
import loot.GameFrame;
import loot.GameFrameSettings;
import loot.graphics.DrawableObject;
import loot.graphics.Layer;

import java.util.ArrayList;
import java.util.Random;


@SuppressWarnings("serial")
public class MainFrame extends GameFrame {
	
	/*
	 * 1,2,3: 단계 설정
	 * 엔터키: 시작
	 * 좌우 방향키: 캐릭터 조종 
	 */	
	// page
	DrawableObject main_page;	// 게임 시작 페이지
	DrawableObject selection;	// 게임 레벨 선택 페이지 
	DrawableObject success;		// 게임 성공 페이지 
	DrawableObject failure;		// 게임 실패 페이지 
	DrawableObject background;	// 게임 내부 배경 
	
	// next level page 
	DrawableObject level1_page;	// 레벨 1 설명 페이지
	DrawableObject level2_page;	// 레벨 2 설명 페이지
	DrawableObject level3_page;	// 레벨 3 설명 페이지
	
	// cat
	DrawableObject original_cat;// 기본 고양이 
	DrawableObject happy_cat;	// 행복한 고양이  
	DrawableObject shock_cat;	// 충격받은 고양이
	DrawableObject smart_cat;	// 똑똑한 고양이 
	
	// good (점수 플러스되는 좋은 아이템)
	DrawableObject clang;		// c 언어 아이템
	DrawableObject cpp;			// c++언어 아이템
	DrawableObject chicken;		// 치킨 아이템
	DrawableObject ginseng;		// 인삼 아이템
	DrawableObject hot6;		// 핫식스 아이템
	DrawableObject java;		// java 언어 아이템
	DrawableObject pizza;		// 피자 아이템
	DrawableObject python;		// python 언어 아이템
	
	// bad (점수 마이너스 되는 나쁜 아이템)
	DrawableObject ddong;		// 똥 아이템 
	DrawableObject error;		// 에러 아이템 
	DrawableObject virus;		// 바이러스 아이템 
	DrawableObject soju;		// 소주 아이템 
	DrawableObject smoke;		// 담배 아이템 
	DrawableObject boom;		// 폭탄 아이템 
	DrawableObject stone;		// 돌 아이템 
	
	ArrayList<DrawableObject> cats;
	ArrayList<DrawableObject> goods;
	ArrayList<DrawableObject> bads;
	
	long startTime;				// 시작 시각
	double elapsedSeconds;		// 경과 시간 
	
	
	Random random = new Random();
	
	int score = 0;				// 점수
	
	// 점수 폭
	int plusScore = 100;
	int minusScore = -100;
	
	// 그림 크기 
	int cwidth = 64;			// cat
	int cheight = 90;			
	int gwidth = 90;			// good
	int gheight = 75;
	int bwidth = 90;			// bad
	int bheight = 75;
	
	// 위치 
	int center = (settings.canvas_width - cwidth) / 2;
	
	// 떨어지는 속력 
	int vdrop_g = 5;
	int vdrop_b = 7;
	
	// 주인공 속력 
	int v_cat = 5;
	
	// 한 레벨 당 게임 시간 
	int runningTime = 60;
	
	enum GameState
	{
		Ready,		// 메인 화면
		Select,		// 단계 선택 
		Stage1,		// 1단계 설명
		Running1,	// 엔터키 누르고 시작한 상태, 1단계 
		Stage2,		// 2단계 설명 
		Running2,	// 2단계 
		Stage3,		// 3단계 설명 
		Running3,	// 3단계 
		Success,	// 미션 성공 
		Fail		// 미션 실패 
	}
	
	GameState state = GameState.Ready;

	
	public MainFrame(GameFrameSettings settings) {
		super(settings);
		
		// 배경 이미지 가져오기 
		images.LoadImage("Images/page/background.jpg", "img_back");
		images.LoadImage("Images/page/failure.jpg", "img_fail");
		images.LoadImage("Images/page/main_page.jpg", "img_main");
		images.LoadImage("Images/page/selection.jpg", "img_select");
		images.LoadImage("Images/page/success.jpg", "img_success");
		
		images.LoadImage("Images/nextLevelPage/level1_page.jpg", "img_lv1");
		images.LoadImage("Images/nextLevelPage/level2_page.jpg", "img_lv2");
		images.LoadImage("Images/nextLevelPage/level3_page.jpg", "img_lv3");

		
		// 이미지 가져오기 
		images.LoadImage("Images/cat/happy_cat.png", "img_happy");
		images.LoadImage("Images/cat/shock_cat.png", "img_shock");
		images.LoadImage("Images/cat/original_cat.png", "img_original");
		images.LoadImage("Images/cat/smart_cat.png", "img_smart");

		
		images.LoadImage("Images/good/hot6.png", "img_hot6");
		images.LoadImage("Images/good/pizza.png", "img_pizza");
		images.LoadImage("Images/good/java.png", "img_java");
		images.LoadImage("Images/good/cpp.png", "img_cpp");
		images.LoadImage("Images/good/python.png", "img_python");
		images.LoadImage("Images/good/chicken.png", "img_chicken");
		images.LoadImage("Images/good/ginseng.png", "img_ginseng");

		
		images.LoadImage("Images/bad/ddong.png", "img_ddong");
		images.LoadImage("Images/bad/error.png", "img_error");
		images.LoadImage("Images/bad/virus.png", "img_virus");
		images.LoadImage("Images/bad/soju.png", "img_soju");
		images.LoadImage("Images/bad/smoke.png", "img_smoke");
		images.LoadImage("Images/bad/boom.png", "img_boom");
		images.LoadImage("Images/bad/stone.png", "img_stone");

		
		
		inputs.BindKey(KeyEvent.VK_LEFT, 0);
		inputs.BindKey(KeyEvent.VK_RIGHT, 1);
		inputs.BindKey(KeyEvent.VK_ENTER, 2);
		inputs.BindKey(KeyEvent.VK_1, 3);
		inputs.BindKey(KeyEvent.VK_2, 4);
		inputs.BindKey(KeyEvent.VK_3, 5);
		
		LoadFont("Consolas 23");
	}
	
	Layer layer;
		
	@Override
	public boolean Initialize() {
		layer = new Layer(0, 0, settings.canvas_width, settings.canvas_height);

		background = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("img_back"));
		failure = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("img_fail"));
		main_page = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("img_main"));
		selection = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("img_select"));
		success = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("img_success"));

		level1_page = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("img_lv1"));
		level2_page = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("img_lv2"));
		level3_page = new DrawableObject(0, 0, settings.canvas_width, settings.canvas_height, images.GetImage("img_lv3"));

		
		original_cat = new DrawableObject(center, settings.canvas_height-cheight-10, cwidth, cheight, images.GetImage("img_original"));
		happy_cat = new DrawableObject(center, settings.canvas_height-cheight-10, cwidth, cheight, images.GetImage("img_happy"));
		shock_cat = new DrawableObject(center, settings.canvas_height-cheight-10, cwidth, cheight, images.GetImage("img_shock"));
		smart_cat = new DrawableObject(center, settings.canvas_height-cheight-10, cwidth, cheight, images.GetImage("img_smart"));

		hot6 = new DrawableObject(random.nextInt(settings.canvas_width-gwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, gwidth, gheight, images.GetImage("img_hot6"));
		pizza = new DrawableObject(random.nextInt(settings.canvas_width-gwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, gwidth, gheight, images.GetImage("img_pizza"));
		cpp = new DrawableObject(random.nextInt(settings.canvas_width-gwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, gwidth, gheight, images.GetImage("img_cpp"));
		ginseng = new DrawableObject(random.nextInt(settings.canvas_width-gwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, gwidth, gheight, images.GetImage("img_ginseng"));
		java = new DrawableObject(random.nextInt(settings.canvas_width-gwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, gwidth, gheight, images.GetImage("img_java"));
		python = new DrawableObject(random.nextInt(settings.canvas_width-gwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, gwidth, gheight, images.GetImage("img_python"));
		chicken = new DrawableObject(random.nextInt(settings.canvas_width-gwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, gwidth, gheight, images.GetImage("img_chicken"));

		ddong = new DrawableObject(random.nextInt(settings.canvas_width-bwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, bwidth, bheight, images.GetImage("img_ddong"));
		error = new DrawableObject(random.nextInt(settings.canvas_width-bwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, bwidth, bheight, images.GetImage("img_error"));
		virus = new DrawableObject(random.nextInt(settings.canvas_width-bwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, bwidth, bheight, images.GetImage("img_virus"));
		soju = new DrawableObject(random.nextInt(settings.canvas_width-bwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, bwidth, bheight, images.GetImage("img_soju"));
		smoke = new DrawableObject(random.nextInt(settings.canvas_width-bwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, bwidth, bheight, images.GetImage("img_smoke"));
		boom = new DrawableObject(random.nextInt(settings.canvas_width-bwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, bwidth, bheight, images.GetImage("img_boom"));
		stone = new DrawableObject(random.nextInt(settings.canvas_width-bwidth), random.nextInt(settings.canvas_height+1)-settings.canvas_height, bwidth, bheight, images.GetImage("img_stone"));
		
		cats = new ArrayList<DrawableObject>();
		cats.add(happy_cat);
		cats.add(shock_cat);
		cats.add(original_cat);
		
		goods = new ArrayList<DrawableObject>();
		goods.add(hot6);
		goods.add(pizza);
		goods.add(cpp);
		goods.add(ginseng);
		goods.add(java);
		goods.add(python);
		goods.add(chicken);
		
		bads = new ArrayList<DrawableObject>();
		bads.add(ddong);
		bads.add(error);
		bads.add(virus);
		bads.add(soju);
		bads.add(smoke);
		bads.add(boom);
		bads.add(stone);

		
		return true;
	}
	
	
	ArrayList<Integer> scorelist = new ArrayList<Integer>();

		
	@Override
	public boolean Update(long timeStamp) {
		inputs.AcceptInputs();
		
		for (DrawableObject cats : cats) {
			// 방향키로 주인공 움직이기 
			if (inputs.buttons[0].IsPressedNow()) {
				cats.x -= v_cat + 5;
			}
			
			if (inputs.buttons[0].isPressed && inputs.buttons[0].isChanged==false) {
				cats.x -= v_cat;
			}
			
			if (inputs.buttons[1].IsPressedNow()) {
				cats.x += v_cat + 5;
			}
			
			if (inputs.buttons[1].isPressed && inputs.buttons[1].isChanged==false) {
				cats.x += v_cat;
			}
			
			// 주인공이 벽에 닿았을 때 
			if (happy_cat.x + cwidth >= settings.canvas_width) {
				cats.x = settings.canvas_width - cwidth;
			} 
			
			else if (happy_cat.x <= 0) {
				cats.x = 0;
			}
			
		}
		

		switch (state)
		{
		// 준비 화면 
		case Ready:
			if (inputs.buttons[2].IsPressedNow()) {
				state = GameState.Select;
			}
			
			else {
				break;
			}
			break;
			
		case Select:
			// 시간, 점수, 화면 초기화 
			startTime = 0;
			score = 0;
			scorelist.add(0);
			
			for (DrawableObject cats : cats) {
				cats.x = center;
			}

			for (DrawableObject goods : goods) {
				goods.y = random.nextInt(settings.canvas_height+1)-settings.canvas_height;
			}
			
			for (DrawableObject bads : bads) {
				bads.y = random.nextInt(settings.canvas_height+1)-settings.canvas_height;
			}
			
			// 단계 선택 (1, 2, 3 누르기) 
			if (inputs.buttons[3].IsPressedNow()) {
				state = GameState.Stage1;
			}
			
			else if (inputs.buttons[4].IsPressedNow()) {
				state = GameState.Stage2;
			}
			
			else if (inputs.buttons[5].IsPressedNow()) {
				state = GameState.Stage3;
			}
			
			else if (inputs.buttons[2].IsPressedNow()) {
				state = GameState.Ready;
			}
			
			else {
				break;
			}
			break;
			
		// 단계별 설명 (Enter키 누르면 시작) 
		case Stage1:
			if (inputs.buttons[2].IsPressedNow()) {
				state = GameState.Running1;
			}
			
			else {
				break;
			}
			
			break;
			
		case Stage2:
			if (inputs.buttons[2].IsPressedNow()) {
				state = GameState.Running2;
			}
			
			else {
				break;
			}
			
			break;
			
		case Stage3:
			if (inputs.buttons[2].IsPressedNow()) {
				state = GameState.Running3;
			}
			
			else {
				break;
			}
			
			break;
		
		// 1단계 게임 시작 
		case Running1:
			running(timeStamp);
			for (DrawableObject bads : bads.subList(0,3)) {
				bads.y += vdrop_b;
			}
			break;
			
		// 2단계 
		case Running2:
			v_cat = 8;
			running(timeStamp);
			for (DrawableObject bads : bads.subList(0,5)) {
				bads.y += vdrop_b + 2;
			}
			break;
			
		// 3단계 
		case Running3:
			v_cat = 11;
			running(timeStamp);
			for (DrawableObject bads : bads) {
				bads.y += vdrop_b + 4;
			}
			break;
			
		// 미션 성공 
		case Success:
			if (inputs.buttons[2].IsPressedNow()) {
				state = GameState.Select;
			}
			
			else {
				break;
			}
			break;
			
		// 미션 실패 
		case Fail:
			if (inputs.buttons[2].IsPressedNow()) {
				state = GameState.Select;
			}
			
			else {
				break;
			}
			break;
		}
		
		return true;
	}


	@Override
	public void Draw(long timeStamp) {
		BeginDraw();
		
		ClearScreen();
		
		switch (state)
		{
		case Ready:
//			DrawString(350, 500, "1,2,3 단계 선택");
			main_page.Draw(g);
			break;
		
		case Select:
			selection.Draw(g);
			break;
		
		case Stage1:
//			DrawString(350, 500, "Enter키를 누르세요.");
			level1_page.Draw(g);
			break;
			
		case Stage2:
//			DrawString(350, 500, "Enter키를 누르세요.");
			level2_page.Draw(g);
			break;
			
		case Stage3:
//			DrawString(350, 500, "Enter키를 누르세요.");
			level3_page.Draw(g);
			break;
		
		case Running1:
			runningDraw();
			for (DrawableObject bads : bads.subList(0,3)) {
				bads.Draw(g);
			}
			DrawString(125, 75, "LEVEL1");
			break;
			
		case Running2:
			runningDraw();
			for (DrawableObject bads : bads.subList(0,5)) {
				bads.Draw(g);
			}
			DrawString(125, 75, "LEVEL2");
			break;

		case Running3:
			runningDraw();
			for (DrawableObject bads : bads) {
				bads.Draw(g);
			}
			DrawString(125, 75, "LEVEL3");
			break;
		
		case Success:
			success.Draw(g);
			DrawString(settings.canvas_width/2-160, 220, "%4d points | %.2f seconds", score, elapsedSeconds);
			break;
			
		case Fail:
			failure.Draw(g);
			DrawString(settings.canvas_width/2-160, 220, "%4d points | %.2f seconds", score, elapsedSeconds);
			break;
		}
		
		EndDraw();
	}

	private void running(long timeStamp) {
		// 물체 떨어지는 속도 조절
		for (DrawableObject goods : goods) {
			goods.y += vdrop_g;
		}
		
		// 좋은거 받았을 때 
		for (DrawableObject goods : goods) {
			if ((happy_cat.x - gwidth) <= goods.x && goods.x <= (happy_cat.x + cwidth) && 
				(happy_cat.y - gheight + 10) < goods.y && goods.y <= (happy_cat.y + cheight / 2)) {
				goods.x = random.nextInt(layer.width - gwidth);
				goods.y = random.nextInt(settings.canvas_height+1)-settings.canvas_height;
				score += plusScore;
				scorelist.add(score);
			}
			
			else if (goods.y >= settings.canvas_height) {
				goods.x = random.nextInt(layer.width - gwidth);
				goods.y = random.nextInt(settings.canvas_height+1)-settings.canvas_height;
			}
		}
		
		// 나쁜거 받았을 때 
		for (DrawableObject bads : bads) {
			if ((happy_cat.x - bwidth) <= bads.x && bads.x <= (happy_cat.x + cwidth) && 
				(happy_cat.y - bheight + 10) < bads.y && bads.y <= (happy_cat.y + cheight / 2)) {
				bads.x = random.nextInt(layer.width - gwidth);
				bads.y = random.nextInt(settings.canvas_height+1)-settings.canvas_height;
				score += minusScore;
				scorelist.add(score);
			}
			
			else if (bads.y >= settings.canvas_height) {
				bads.x = random.nextInt(settings.canvas_width-bwidth);
				bads.y = random.nextInt(settings.canvas_height+1)-settings.canvas_height;
			}
		}
		
		// 시작 시각 기록
		if ( startTime == 0 )
			startTime = timeStamp;
				
		// 이번 프레임의 시작 시각 기록
		if (startTime != 0) {
			elapsedSeconds = (timeStamp - startTime) / 1000.0;
		}
		
		if (elapsedSeconds == runningTime) {
			if (score >= 1000) {
				state = GameState.Success;
			}
			
			else {
				state = GameState.Fail;
			}
		}
	}

	private void runningDraw() {
		background.Draw(g);
		// 점수, 시간 표시 
		DrawString(650, 70, "%.2f seconds | score: %d", elapsedSeconds, score);
		
		// 주인공 그리기 (나쁜거 받으면 슬픈 고양이 됨)
		for (int i = 1; i < scorelist.size(); i++) {
			if (scorelist.get(i) > scorelist.get(i-1)) {
				happy_cat.Draw(g);
			} 
			
			else if (scorelist.get(i) < scorelist.get(i-1)) {
				shock_cat.Draw(g);
			}
			
			else {
				original_cat.Draw(g);
			}
		}
		
		// 떨어지는것들 그리기  
		for (DrawableObject goods : goods) {
			goods.Draw(g);
		}
	}
}
