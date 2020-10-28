package com.siinus;

import com.siinus.simpleGrafix.input.InputUtils;

public class Program extends com.siinus.simpleGrafix.Program {
    private static final int SPEED = 5;
    private int x, y;

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (InputUtils.isKeyPressed('W')) y+=SPEED;
        if (InputUtils.isKeyPressed('S')) y-=SPEED;
        if (InputUtils.isKeyPressed('A')) x+=SPEED;
        if (InputUtils.isKeyPressed('D')) x-=SPEED;
    }

    @Override
    public void render() {
        for (int y=0; y<Visualizer.drawImage.length; y++) {
            for (int x=0; x<Visualizer.drawImage[y].length; x++) {
                getRenderer().setPixel(x+this.x, y+this.y, Visualizer.colorOfBiome(Visualizer.drawImage[y][x]));
            }
        }
        String biomeID;
        try {
            biomeID = "B: "+Visualizer.drawImage[InputUtils.getMouseY()][InputUtils.getMouseX()];
        } catch (ArrayIndexOutOfBoundsException e) {
            biomeID = "";
        }
        getRenderer().drawText(biomeID, InputUtils.getMouseX(), InputUtils.getMouseY(), 0xff000000, null);
    }

    @Override
    public void stop() {

    }

    @Override
    public void init() {
        super.init();
    }
}
